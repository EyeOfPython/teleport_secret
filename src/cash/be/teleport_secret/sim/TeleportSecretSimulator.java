package cash.be.teleport_secret.sim;

import cash.be.nfc.Card;
import cash.be.nfc.ISO7816Exception;
import cash.be.nfc.Nfc;
import cash.be.teleport_secret.SecretTeleporter;

public class TeleportSecretSimulator {
    public static void main(String[] args) {
        Card card1 = Nfc.makeSimulatedCard();
        Card card2 = Nfc.makeSimulatedCard();

        SecretTeleporter teleporter = new SecretTeleporter();

        try {
            teleporter.teleport(card1, card2);
        } catch (ISO7816Exception e) {
            e.printStackTrace();
        }
    }
}
