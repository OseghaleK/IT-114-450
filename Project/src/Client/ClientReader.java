package Client;

import Common.Payload;
import Common.PayloadType;

import java.io.ObjectInputStream;

// UCID: oka
// Date: 2025-07-24
// Summary: Background thread that prints messages from the server.
public class ClientReader extends Thread {
    private final ObjectInputStream in;

    public ClientReader(ObjectInputStream in) { this.in = in; }

    @Override
    public void run() {
        try {
            while (true) {
                Payload p = (Payload) in.readObject();
                if (p.getType() == PayloadType.MESSAGE) {
                    System.out.println(p.getData());
                } else {
                    System.out.println("[DEBUG] " + p);
                }
            }
        } catch (Exception e) {
            System.out.println("[SYSTEM] Disconnected from server.");
        }
    }
}
