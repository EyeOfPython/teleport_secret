package cash.be.teleport_secret;

import cash.be.nfc.Card;
import cash.be.nfc.ISO7816Exception;
import cash.be.nfc.Nfc;
import cash.be.teleport_secret.card.TeleportSecretApplet;

import javax.smartcardio.CommandAPDU;
import java.nio.charset.Charset;

public class SecretTeleporter {
    private byte[] merchantPk;

    public void registerMerchantCard(Card merchantCard) throws ISO7816Exception {
        merchantCard.selectApplet(Nfc.AID);
        merchantPk = merchantCard.transmit(
                new CommandAPDU(0x00, TeleportSecretApplet.INS_GET_INTERNAL_PUBKEY, 0x00, 0x00)
        ).getData();
        System.out.println("Merchant PK: " + Utils.bytesToHex(merchantPk));
    }

    public void moveSecretFromCard(Card customerCard) throws ISO7816Exception {
        customerCard.selectApplet(Nfc.AID);
        byte[] encryptedCoinSk = customerCard.transmit(
                new CommandAPDU(0x00, TeleportSecretApplet.INS_MOVE_SECRET, 0x00, 0x00, merchantPk)
        ).getData();
        System.out.println("Encrypted coinPk: " + Utils.bytesToHex(encryptedCoinSk));
    }

    public void redeemSecret(Card merchantCard) throws ISO7816Exception {
        // TODO
    }
}
