package cash.be.teleport_secret.card;

import javacard.framework.*;

public class TeleportSecretApplet extends Applet {
    public static final byte INS_SAY_HELLO = 1;
    // everything that we allocate with `new` will be in NVM (EEPROM)
    private static byte[] HELLO_MSG = new byte[]{72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 32, 33};
    private final byte[] mem;
    private final byte[] response;
    private static final short LENGTH_MEM = 256;
    private static final short LENGTH_RESPONSE = 256;

    protected TeleportSecretApplet(byte[] bArray, short bOffset, byte bLength) {
        // everything allocated with `makeTransientByteArray` will be in RAM
        this.mem = JCSystem.makeTransientByteArray(LENGTH_MEM, JCSystem.CLEAR_ON_DESELECT);
        this.response = JCSystem.makeTransientByteArray(LENGTH_RESPONSE, JCSystem.CLEAR_ON_DESELECT);
        this.register();
    }

    public static void install(byte[] bArray, short bOffset, byte bLength) throws ISOException {
        new TeleportSecretApplet(bArray, bOffset, bLength);
    }

    public void process(APDU apdu) {
        if (!this.selectingApplet()) {
            byte[] buffer = apdu.getBuffer();
            switch (buffer[ISO7816.OFFSET_INS]) {
                case INS_SAY_HELLO: {
                    short helloLength = (short) HELLO_MSG.length;
                    Util.arrayCopyNonAtomic(HELLO_MSG, (short) 0, this.response, (short) 0, helloLength);
                    sendResponse(apdu, this.response, (short) 0, helloLength);
                    return;
                }
                default:
                    ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
            }
        }
    }

    private static void sendResponse(APDU apdu, byte[] array, short offset, short length) {
        apdu.setOutgoing();
        apdu.setOutgoingLength(length);
        // we use (array, offset, length) pattern all over the place to emulate slices
        apdu.sendBytesLong(array, offset, length);
    }
}
