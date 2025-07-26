package Client;
import Client.PayloadHandler;

import Common.Payload;

import java.io.ObjectInputStream;

/**
 * UCID: oka
 * Date: 2025-07-25
 * Summary: Reads payloads from server on a background thread and forwards to ClientMain.
 */
public class ClientReader extends Thread {
    private final ObjectInputStream in;

    public ClientReader(ObjectInputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object obj = in.readObject();
                if (!(obj instanceof Payload)) {
                    System.out.println("[DEBUG] Unknown object from server: " + obj);
                    continue;
                }
                Payload p = (Payload) obj;
                PayloadHandler.handlePayload(p);


            }
        } catch (Exception e) {
            System.out.println("[SYSTEM] Disconnected: " + e.getMessage());
        }
    }
}

