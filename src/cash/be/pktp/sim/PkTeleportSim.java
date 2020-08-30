package cash.be.pktp.sim;

import cash.be.pktp.card.PkTeleportApplet;
import com.licel.jcardsim.smartcardio.CardSimulator;
import com.licel.jcardsim.utils.AIDUtil;
import javacard.framework.AID;

import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import java.nio.charset.Charset;

public class PkTeleportSim {
    public static void main(String[] args) {
        CardSimulator simulator = new CardSimulator();
        AID aid = AIDUtil.create("0102030405060708");
        simulator.installApplet(aid, PkTeleportApplet.class);
        simulator.selectApplet(aid);

        CommandAPDU commandAPDU = new CommandAPDU(0x00, PkTeleportApplet.INS_SAY_HELLO, 0x00, 0x00);
        ResponseAPDU response = simulator.transmitCommand(commandAPDU);

        System.out.println(Utils.bytesToHex(response.getData()));
        System.out.println(new String(response.getData(), Charset.defaultCharset()));
    }
}
