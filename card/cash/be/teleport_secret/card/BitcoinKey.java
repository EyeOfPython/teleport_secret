package cash.be.teleport_secret.card;

import javacard.framework.ISOException;
import javacard.framework.Util;
import javacard.security.*;

public class BitcoinKey {
    private ECPrivateKey privateKey = null;
    private byte[] publicKeyCompressed = new byte[PK_COMPRESSED_SIZE];
    private KeyAgreement agreement = null;
    private ECPublicKey publicKey;
    private Signature signature;
    private byte mode;

    public static final short SK_SIZE = 32;
    public static final short PK_COMPRESSED_SIZE = 33;
    public static final short PK_UNCOMPRESSED_SIZE = 65;
    public static final short MAX_SIG_SIZE = 73;

    public static final byte BITCOIN_KEY_UNINITIALIZED = 0x00;
    public static final byte BITCOIN_KEY_MODE_VERIFY = 0x01;
    public static final byte BITCOIN_KEY_MODE_SIGN = 0x02;

    public static final short SW_WRONG_MODE_FOR_SIGNING = 0x1201;
    public static final short SW_INVALID_UNCOMPRESSED_PK_SIZE = 0x1202;

    public BitcoinKey() {
        this.publicKey = (ECPublicKey) buildKey(KeyBuilder.TYPE_EC_FP_PUBLIC);
        this.signature = Signature.getInstance(Signature.ALG_ECDSA_SHA_256, false);
    }

    public void generate(byte[] tmp) {
        if (this.privateKey == null) {
            this.privateKey = (ECPrivateKey) buildKey(KeyBuilder.TYPE_EC_FP_PRIVATE);
        }
        KeyPair keyPair = new KeyPair(this.publicKey, this.privateKey);
        keyPair.genKeyPair();
        short pubKeyLength = this.publicKey.getW(tmp, (short) 0);
        if (pubKeyLength != PK_UNCOMPRESSED_SIZE) {
            ISOException.throwIt(SW_INVALID_UNCOMPRESSED_PK_SIZE);
            return;
        }
        this.compressPubKey(tmp, (short) 0, pubKeyLength);
        this.signature.init(privateKey, Signature.MODE_SIGN);
        this.mode = BITCOIN_KEY_MODE_SIGN;
    }

    public void setPublicKey(byte[] pk, short offset) {
        this.publicKey.setW(pk, offset, PK_UNCOMPRESSED_SIZE);
        this.compressPubKey(pk, offset, PK_UNCOMPRESSED_SIZE);
        this.signature.init(this.publicKey, Signature.MODE_VERIFY);
        this.mode = BITCOIN_KEY_MODE_VERIFY;
    }

    public void setPrivateKey(byte[] sk, short offset) {
        if (this.privateKey == null) {
            this.privateKey = (ECPrivateKey) buildKey(KeyBuilder.TYPE_EC_FP_PRIVATE);
        }
        this.privateKey.setS(sk, offset, SK_SIZE);
        this.signature.init(privateKey, Signature.MODE_SIGN);
        this.mode = BITCOIN_KEY_MODE_SIGN;
    }

    public void reset(byte[] tmp) {
        Util.arrayFillNonAtomic(tmp, (short) 0, PK_UNCOMPRESSED_SIZE, (byte) 0);
        this.privateKey.setS(tmp, (short) 0, SK_SIZE);
        this.publicKey.setW(tmp, (short) 0, PK_UNCOMPRESSED_SIZE);
        this.mode = BITCOIN_KEY_UNINITIALIZED;
    }

    private static ECKey buildKey(byte keyType) {
        ECKey key = (ECKey) KeyBuilder.buildKey(keyType, KeyBuilder.LENGTH_EC_FP_256, false);
        Secp256k1.setCommonCurveParameters(key);
        return key;
    }

    private void compressPubKey(byte[] pk, short pkOffset, short pkLength) {
        byte parity = (byte) (pk[(short) (pkOffset + pkLength - 1)] & 0x01);
        this.publicKeyCompressed[0] = (byte) (parity | 0x02);
        Util.arrayCopyNonAtomic(pk, (short) (pkOffset + 1), this.publicKeyCompressed, (short) 1, (short) 32);
    }

    public short getPublicKeyCompressed(byte[] pk, short pkOffset) {
        Util.arrayCopy(publicKeyCompressed, (short)0, pk, pkOffset, PK_COMPRESSED_SIZE);
        return (short)(pkOffset + PK_COMPRESSED_SIZE);
    }

    public short getPublicKeyUncompressed(byte[] pk, short pkOffset) {
        return this.publicKey.getW(pk, pkOffset);
    }

    public short getPrivateKey(byte[] sk, short skOffset) {
        return this.privateKey.getS(sk, skOffset);
    }

    public void initKeyAgreement() {
        if (agreement == null) {
            agreement = KeyAgreement.getInstance(KeyAgreement.ALG_EC_SVDP_DH_PLAIN, false);
        }
        agreement.init(this.privateKey);
    }

    public short shareSecret(byte[] pk, short pkOffset, short pkLength, byte[] secret, short secretOffset) {
        return agreement.generateSecret(pk, pkOffset, pkLength, secret, secretOffset);
    }

    public short sign(byte[] msg, short msgOffset, short msgLength, byte[] result, short resultOffset) {
        if (this.mode != BITCOIN_KEY_MODE_SIGN) {
            ISOException.throwIt(SW_WRONG_MODE_FOR_SIGNING);
            return 0;
        }
        return signature.sign(msg, msgOffset, msgLength, result, resultOffset);
    }

    public boolean verify(byte[] inBuff,
                          short inOffset,
                          short inLength,
                          byte[] sigBuff,
                          short sigOffset,
                          short sigLength) {
        if (this.mode != BITCOIN_KEY_MODE_VERIFY) {
            ISOException.throwIt(SW_WRONG_MODE_FOR_SIGNING);
            return false;
        }
        return signature.verify(inBuff, inOffset, inLength, sigBuff, sigOffset, sigLength);
    }
}
