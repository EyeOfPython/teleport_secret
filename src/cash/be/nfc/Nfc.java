package cash.be.nfc;

import cash.be.nfc.simulator.SimulatedCard;
import cash.be.teleport_secret.card.TeleportSecretApplet;
import com.licel.jcardsim.utils.AIDUtil;
import javacard.framework.AID;

public class Nfc {
    public static AID AID = AIDUtil.create("0102030405060708");
    public static Card makeSimulatedCard() {
        return new SimulatedCard(AID, TeleportSecretApplet.class);
    }
}
