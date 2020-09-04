package cash.be.teleport_secret;

import cash.be.nfc.Card;
import cash.be.nfc.ISO7816Exception;
import cash.be.nfc.Nfc;
import cash.be.teleport_secret.card.TeleportSecretApplet;

import javax.smartcardio.CommandAPDU;
import java.nio.charset.Charset;

public class SecretTeleporter {
    public void registerMerchantCard(Card merchantCard) throws ISO7816Exception {
        merchantCard.selectApplet(Nfc.AID);
        byte [] response = merchantCard.transmit(
                new CommandAPDU(0x00, TeleportSecretApplet.INS_SAY_HELLO, 0x00, 0x00)
        ).getData();
        System.out.println(new String(response, Charset.defaultCharset()));
    }

    public void moveSecretFromCard(Card customerCard) throws ISO7816Exception {
        // TODO
    }

    public void redeemSecret(Card merchantCard) throws ISO7816Exception {
        // TODO
    }
}
