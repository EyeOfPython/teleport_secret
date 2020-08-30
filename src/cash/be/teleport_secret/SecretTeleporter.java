package cash.be.teleport_secret;

import cash.be.nfc.Card;
import cash.be.nfc.ISO7816Exception;
import cash.be.nfc.Nfc;

import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import java.nio.charset.Charset;

public class SecretTeleporter {
    public void onFirstCard(Card card) throws ISO7816Exception {
        card.selectApplet(Nfc.AID);
        ResponseAPDU response = card.transmit(new CommandAPDU(0x00, 0x01, 0x00, 0x00));
        System.out.println(new String(response.getData(), Charset.defaultCharset()));
    }

    public void onSecondCard(Card card) throws ISO7816Exception {

    }
}
