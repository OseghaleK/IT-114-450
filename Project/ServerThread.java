// UCID: oka
// Date: 2025-07-24
// Summary: Handles one client socket: reads Payloads, updates rooms, broadcasts messages.

package Server;

import Common.Payload;
import Common.PayloadType;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerThread extends Thread {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private final Socket socket;
    private final ServerMain server;
    private final RoomManager roomManager;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private String clientName = "anon";
    private int clientId = NEXT_ID.getAndIncrement();
    private Room currentRoom;

    public ServerThread(Socket socket, ServerMain server, RoomManager rm) {
        this.socket = socket;
        this.server = server;
        this.roomManager = rm;
    }

    public String getClientName() { return clientName; }
    public int getClientId() { return clientId; }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in  = new ObjectInputStream(socket.getInputStream());

            // put client in lobby by default
            moveToRoom(roomManager.getLobby());

            // send welcome
            send(Payload.system("Connected. You are in lobby."));

            while (!socket.isClosed()) {
                Object obj = in.readObject();
                if (!(obj instanceof Payload payload)) continue;
                handle(payload);
            }
        } catch (EOFException | StreamCorruptedException e) {
            // client closed or stream broken
        } catch (Exception e) {
            System.err.println("ServerThread error: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    private void handle(Payload p) {
        switch (p.getType()) {
            case NAME -> {
                this.clientName = p.getMessage(); // client sent their name in message
                send(Payload.system("Name set to " + clientName));
            }
            case MESSAGE -> {
                if (currentRoom != null) {
                    currentRoom.broadcastMessage(this, p.getMessage());
                }
            }
            case CREATE_ROOM -> {
                String rName = p.getRoom();
                Room r = roomManager.getOrCreate(rName);
                moveToRoom(r);
                send(Payload.system("Created & joined room: " + rName));
            }
            case JOIN_ROOM -> {
                String rName = p.getRoom();
                Room r = roomManager.get(rName);
                if (r == null) {
                    send(Payload.system("Room does not exist."));
                } else {
                    moveToRoom(r);
                    send(Payload.system("Joined room: " + rName));
                }
            }
            case LEAVE_ROOM -> {
                // back to lobby
                moveToRoom(roomManager.getLobby());
                send(Payload.system("Returned to lobby."));
            }
            case DISCONNECT -> {
                send(Payload.system("Goodbye."));
                closeQuietly();
            }
            default -> {
                // ignore or future types
            }
        }
    }

    private void moveToRoom(Room newRoom) {
        if (currentRoom != null) {
            currentRoom.remove(this);
        }
        currentRoom = newRoom;
        currentRoom.add(this);
    }

    public void send(Payload p) {
        try {
            out.writeObject(p);
            out.flush();
        } catch (IOException e) {
            // client might be gone
        }
    }

    private void cleanup() {
        if (currentRoom != null) {
            currentRoom.remove(this);
        }
        server.removeClient(this);
        closeQuietly();
    }

    private void closeQuietly() {
        try { socket.close(); } catch (IOException ignored) {}
    }
}
