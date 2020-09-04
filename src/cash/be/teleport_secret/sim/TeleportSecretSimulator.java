package cash.be.teleport_secret.sim;

import cash.be.nfc.Card;
import cash.be.nfc.ISO7816Exception;
import cash.be.nfc.Nfc;
import cash.be.teleport_secret.SecretTeleporter;

public class TeleportSecretSimulator {
    public static void main(String[] args) {
        Card merchantCard = Nfc.makeSimulatedCard();
        Card customerCard = Nfc.makeSimulatedCard();

        SecretTeleporter teleporter = new SecretTeleporter();

        try {
            teleporter.registerMerchantCard(merchantCard);
            teleporter.moveSecretFromCard(customerCard);
            teleporter.redeemSecret(merchantCard);
        } catch (ISO7816Exception e) {
            e.printStackTrace();
        }
    }
}
