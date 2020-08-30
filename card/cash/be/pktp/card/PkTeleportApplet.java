package cash.be.pktp.card;

import javacard.framework.*;

public class PkTeleportApplet extends Applet {
    public static final byte INS_SAY_HELLO = 1;
    private static byte[] HELLO_MSG = new byte[]{72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 32, 33};
    private final byte[] mem;
    private static final short LENGTH_ECHO_BYTES = 256;

    protected PkTeleportApplet(byte[] bArray, short bOffset, byte bLength) {
        this.mem = JCSystem.makeTransientByteArray(LENGTH_ECHO_BYTES, JCSystem.CLEAR_ON_DESELECT);
        this.register();
    }

    public static void install(byte[] bArray, short bOffset, byte bLength) throws ISOException {
        new PkTeleportApplet(bArray, bOffset, bLength);
    }

    public void process(APDU apdu) {
        if (!this.selectingApplet()) {
            byte[] buffer = apdu.getBuffer();
            switch (buffer[ISO7816.OFFSET_INS]) {
                case INS_SAY_HELLO: {
                    byte[] hello = this.mem;
                    short helloLength = (short) HELLO_MSG.length;
                    Util.arrayCopyNonAtomic(HELLO_MSG, (short) 0, hello, (short) 0, helloLength);

                    apdu.setOutgoing();
                    apdu.setOutgoingLength(helloLength);
                    apdu.sendBytesLong(hello, (short) 0, helloLength);
                    return;
                }
                default:
                    ISOException.throwIt((short) 27904);
            }
        }
    }
}
