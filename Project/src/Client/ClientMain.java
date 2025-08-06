package Client;

import Common.Payload;
import Common.PayloadType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

// UCID: oka
// Date: 2025-07-24
// Summary: CLI client supporting /name, /connect, /createroom, /joinroom, /leave, /msg, /quit.
public class ClientMain {
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static Socket socket;
    private static String name = "anon";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("[SYSTEM] Commands: /name, /connect, /createroom, /joinroom, /leave, /msg, /quit");

        while (true) {
            String line = sc.nextLine().trim();

            if (line.startsWith("/name ")) {
                name = line.substring(6).trim();
                send(new Payload(PayloadType.NAME, name));
                System.out.println("[SYSTEM] Name set locally to " + name);
            } else if (line.startsWith("/connect ")) {
                String[] parts = line.split("\\s+");
                if (parts.length < 3) {
                    System.out.println("Usage: /connect host port");
                    continue;
                }
                connect(parts[1], Integer.parseInt(parts[2]));
            } else if (line.startsWith("/createroom ")) {
                send(new Payload(PayloadType.CREATE_ROOM, line.substring(12).trim()));
            } else if (line.startsWith("/joinroom ")) {
                send(new Payload(PayloadType.JOIN_ROOM, line.substring(10).trim()));
            } else if (line.equals("/leave")) {
                send(new Payload(PayloadType.LEAVE_ROOM, ""));
            } else if (line.startsWith("/msg ")) {
                send(new Payload(PayloadType.MESSAGE, line.substring(5)));
            } else if (line.equals("/quit")) {
                send(new Payload(PayloadType.DISCONNECT, ""));
                close();
                break;
            } else {
                System.out.println("Unknown command.");
            }
        }
        sc.close();
    }

    private static void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in  = new ObjectInputStream(socket.getInputStream());
            new ClientReader(in).start();
            send(new Payload(PayloadType.NAME, name));
            System.out.println("[SYSTEM] Connected to " + host + ":" + port);
        } catch (Exception e) {
            System.out.println("[SYSTEM] Failed to connect: " + e.getMessage());
        }
    }

    private static void send(Payload p) {
        try {
            if (out != null) {
                out.writeObject(p);
                out.flush();
            } else {
                System.out.println("[SYSTEM] Not connected. Use /connect first.");
            }
        } catch (Exception e) {
            System.out.println("[SYSTEM] Send failed: " + e.getMessage());
        }
    }

    private static void close() {
        try { socket.close(); } catch (Exception ignored) {}
    }
}
