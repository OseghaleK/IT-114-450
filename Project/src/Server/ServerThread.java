package Server;

import Common.Payload;
import Common.PayloadType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

// UCID: oka
// Date: 2025-07-24
// Summary: Handles a single client connection; processes payloads & room changes.
public class ServerThread extends Thread {
    private final Socket socket;
    private final ServerMain server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String clientName = "anon";
    private Room currentRoom;

    public ServerThread(Socket socket, ServerMain server) {
        this.socket = socket;
        this.server = server;
    }

    public String getClientName() { return clientName; }

    public void sendMessage(String msg) {
        try {
            out.writeObject(new Payload(PayloadType.MESSAGE, msg));
            out.flush();
        } catch (Exception ignored) {}
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in  = new ObjectInputStream(socket.getInputStream());

            currentRoom = server.getRoomManager().getOrCreate("lobby");
            currentRoom.add(this);
            sendMessage("[SYSTEM] Connected. You are in lobby.");

            while (true) {
                Payload p = (Payload) in.readObject();
                handlePayload(p);
            }
        } catch (Exception e) {
            // connection lost or client quit
        } finally {
            cleanup();
        }
    }

    private void handlePayload(Payload p) {
        switch (p.getType()) {
            case NAME -> {
                clientName = p.getData();
                sendMessage("[SYSTEM] Name set to " + clientName);
            }
            case MESSAGE -> {
                if (currentRoom != null) currentRoom.broadcast(clientName + ": " + p.getData());
            }
            case CREATE_ROOM -> {
                Room r = server.getRoomManager().getOrCreate(p.getData());
                moveToRoom(r);
                sendMessage("[SYSTEM] Created & joined room: " + r.getName());
            }
            case JOIN_ROOM -> {
                Room r2 = server.getRoomManager().get(p.getData());
                if (r2 != null) {
                    moveToRoom(r2);
                    sendMessage("[SYSTEM] Joined room: " + r2.getName());
                } else {
                    sendMessage("[SYSTEM] Room does not exist.");
                }
            }
            case LEAVE_ROOM -> {
                moveToRoom(server.getRoomManager().getOrCreate("lobby"));
                sendMessage("[SYSTEM] Back to lobby");
            }
            case DISCONNECT -> {
                sendMessage("[SYSTEM] Disconnecting...");
                cleanup();
            }
            default -> {}
        }
    }

    private void moveToRoom(Room newRoom) {
        if (currentRoom != null) currentRoom.remove(this);
        currentRoom = newRoom;
        currentRoom.add(this);
    }

    private void cleanup() {
        try { if (currentRoom != null) currentRoom.remove(this); } catch (Exception ignored) {}
        try { socket.close(); } catch (Exception ignored) {}
        server.removeClient(this);
    }
}
