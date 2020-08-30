package cash.be.nfc.simulator;

import cash.be.nfc.Card;
import cash.be.nfc.ISO7816Exception;
import com.licel.jcardsim.smartcardio.CardSimulator;
import javacard.framework.AID;
import javacard.framework.Applet;

import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

public class SimulatedCard implements Card {
    private CardSimulator cardSimulator;

    public SimulatedCard(AID aid, Class<? extends Applet> cls) {
        CardSimulator simulator = new CardSimulator();
        simulator.installApplet(aid, cls);
        cardSimulator = simulator;
    }

    @Override
    public void selectApplet(AID aid) {
        this.cardSimulator.selectApplet(aid);
    }

    @Override
    public ResponseAPDU transmit(CommandAPDU data) throws ISO7816Exception {
        ResponseAPDU response = this.cardSimulator.transmitCommand(data);
        if (response.getSW() != 0x9000) {
            throw new ISO7816Exception(String.format("APDU failed: %04x", response.getSW()));
        }
        return response;
    }
}
