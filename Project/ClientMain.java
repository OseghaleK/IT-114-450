// UCID: oka
// Date: 2025-07-24
// Summary: CLI entry point. Parses slash commands, connects socket, sends Payloads.

package Client;

import Common.Payload;
import Common.PayloadType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    private static final String CMDS =
            "[SYSTEM] Commands: /name, /connect, /createroom, /joinroom, /leave, /msg, /quit";

    private ObjectOutputStream out;
    private Socket socket;
    private boolean connected = false;
    private String localName = "anon";

    public static void main(String[] args) {
        new ClientMain().start();
    }

    private void start() {
        System.out.println(CMDS);
        Scanner sc = new Scanner(System.in);
        while (true) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("/")) {
                handleCommand(line);
            } else {
                System.out.println("Unknown command.");
            }
        }
    }

    private void handleCommand(String line) {
        try {
            if (line.startsWith("/quit")) {
                send(Payload.system("Client quitting"));
                sendType(PayloadType.DISCONNECT);
                closeQuietly();
                System.out.println("[SYSTEM] Disconnected from server.");
                System.exit(0);
                return;
            }

            if (line.startsWith("/connect")) {
                String[] parts = line.split("\\s+");
                if (parts.length < 3) {
                    System.out.println("[SYSTEM] Usage: /connect <host> <port>");
                    return;
                }
                connect(parts[1], Integer.parseInt(parts[2]));
                return;
            }

            if (!connected) {
                if (line.startsWith("/name")) {
                    String name = line.replaceFirst("/name\\s*", "").trim();
                    if (name.isEmpty()) {
                        System.out.println("[SYSTEM] Usage: /name <displayName>");
                        return;
                    }
                    localName = name;
                    System.out.println("[SYSTEM] Name set locally to " + localName);
                } else {
                    System.out.println("[SYSTEM] Not connected. Use /connect first.");
                }
                return;
            }

            // below commands require connection
            if (line.startsWith("/name")) {
                // update name on server
                String name = line.replaceFirst("/name\\s*", "").trim();
                if (name.isEmpty()) {
                    System.out.println("[SYSTEM] Usage: /name <displayName>");
                    return;
                }
                localName = name;
                Payload p = new Payload(PayloadType.NAME);
                p.setMessage(localName);
                send(p);
            } else if (line.startsWith("/msg")) {
                String msg = line.replaceFirst("/msg\\s*", "").trim();
                if (msg.isEmpty()) {
                    System.out.println("[SYSTEM] Usage: /msg <text>");
                    return;
                }
                Payload p = new Payload(PayloadType.MESSAGE);
                p.setMessage(msg);
                send(p);
            } else if (line.startsWith("/createroom")) {
                String r = line.replaceFirst("/createroom\\s*", "").trim();
                if (r.isEmpty()) {
                    System.out.println("[SYSTEM] Usage: /createroom <roomName>");
                    return;
                }
                Payload p = new Payload(PayloadType.CREATE_ROOM);
                p.setRoom(r);
                send(p);
            } else if (line.startsWith("/joinroom")) {
                String r = line.replaceFirst("/joinroom\\s*", "").trim();
                if (r.isEmpty()) {
                    System.out.println("[SYSTEM] Usage: /joinroom <roomName>");
                    return;
                }
                Payload p = new Payload(PayloadType.JOIN_ROOM);
                p.setRoom(r);
                send(p);
            } else if (line.startsWith("/leave")) {
                sendType(PayloadType.LEAVE_ROOM);
            } else {
                System.out.println("Unknown command.");
            }
        } catch (Exception e) {
            System.out.println("[SYSTEM] Command error: " + e.getMessage());
        }
    }

    private void connect(String host, int port) throws Exception {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        new ClientReader(in).start();
        connected = true;

        // send the name if user already set it locally
        Payload p = new Payload(PayloadType.NAME);
        p.setMessage(localName);
        send(p);
    }

    private void sendType(PayloadType type) throws Exception {
        Payload p = new Payload(type);
        send(p);
    }

    private void send(Payload p) throws Exception {
        if (out == null) {
            System.out.println("[SYSTEM] Not connected yet.");
            return;
        }
        out.writeObject(p);
        out.flush();
    }

    private void closeQuietly() {
        try { if (socket != null) socket.close(); } catch (Exception ignored) {}
    }
}
