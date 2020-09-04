package cash.be.teleport_secret;

import cash.be.nfc.Card;
import cash.be.nfc.ISO7816Exception;
import cash.be.nfc.Nfc;
import cash.be.teleport_secret.card.TeleportSecretApplet;

import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import java.nio.charset.Charset;

public class SecretTeleporter {
    public void teleport(Card card1, Card card2) throws ISO7816Exception {
        card1.selectApplet(Nfc.AID);
        ResponseAPDU response = card1.transmit(
                new CommandAPDU(0x00, TeleportSecretApplet.INS_SAY_HELLO, 0x00, 0x00)
        );
        System.out.println(new String(response.getData(), Charset.defaultCharset()));
    }
}
