package cash.be.nfc;

import javacard.framework.AID;

import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

public interface Card {
    void selectApplet(AID aid);
    ResponseAPDU transmit(CommandAPDU data) throws ISO7816Exception;
}
