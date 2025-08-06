// UCID: oka
// Date: 2025-07-24
// Summary: Background thread that reads Payloads from the server and prints them.

package Client;

import Common.Payload;

import java.io.ObjectInputStream;

public class ClientReader extends Thread {
    private final ObjectInputStream in;

    public ClientReader(ObjectInputStream in) {
        this.in = in;
        setDaemon(true); // let JVM exit if only this thread is running
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object obj = in.readObject();
                if (obj instanceof Payload p) {
                    switch (p.getType()) {
                        case SYSTEM -> System.out.println("[SYSTEM] " + p.getMessage());
                        case MESSAGE -> {
                            String from = p.getFrom() == null ? "anon" : p.getFrom();
                            System.out.println(from + ": " + p.getMessage());
                        }
                        default -> System.out.println(p.toString());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[SYSTEM] Disconnected from server.");
        }
    }
}
