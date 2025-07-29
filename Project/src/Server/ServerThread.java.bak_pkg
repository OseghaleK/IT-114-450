package Server;

import Common.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * UCID: oka
 * Date: 2025-07-25
 * Summary: Handles one client connection. Reads Payloads and routes them to Room/Game logic.
 */
public class ServerThread extends Thread {

    private static int NEXT_ID = 1;

    private final Socket socket;
    private final RoomManager roomManager;
    private ObjectOutputStream out;
    private ObjectInputStream  in;

    private Room  currentRoom;
    private final int id;
    private String clientName = "anon";

    public ServerThread(Socket socket, RoomManager rm) {
        this.socket = socket;
        this.roomManager = rm;
        this.id = NEXT_ID++;
    }

    /** Avoid overriding Thread.getId(); use our own accessor. */
    public int getClientId() { return id; }
    public String getClientName() { return clientName; }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in  = new ObjectInputStream(socket.getInputStream());
            send(new Payload(PayloadType.ASSIGN_ID, Integer.toString(id)));
// join lobby by default
            switchRoom(roomManager.getLobby());

            while (true) {
                Object obj = in.readObject();
                if (!(obj instanceof Payload)) {
                    System.out.println("[SERVER] Unknown object: " + obj);
                    continue;
                }
                Payload p = (Payload) obj;

                // If your Payload doesn't have these setters, remove them
                p.setSenderId(id);
                p.setSenderName(clientName);

                handlePayload(p);
            }
        } catch (Exception e) {
            System.out.println("[SERVER] Client error: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    private void handlePayload(Payload p) {
        switch (p.getType()) {

            // -------- MS1 ----------
            case NAME:
                clientName = p.getMessage();
                send(new Payload(PayloadType.SERVER_NOTIFICATION,
                        "Name set to " + clientName));
                break;

            case MESSAGE:
                if (currentRoom != null) {
                    currentRoom.broadcast(new Payload(
                            PayloadType.MESSAGE,
                            clientName + ": " + p.getMessage()));
                }
                break;

            case CREATE_ROOM: {
                String roomName = p.getMessage();
                // treat new rooms as GameRooms for MS2
                Room r = roomManager.getOrCreateRoom(roomName, true);
                switchRoom(r);
                if (r instanceof GameRoom gr) {
                    gr.onSessionStart(); // quick start
                }
                break;
            }

            case JOIN_ROOM: {
                String roomName = p.getMessage();
                Room r = roomManager.getRoom(roomName);
                if (r == null) {
                    send(new Payload(PayloadType.SERVER_NOTIFICATION,
                            "Room not found"));
                } else {
                    switchRoom(r);
                }
                break;
            }

            case LEAVE_ROOM:
                switchRoom(roomManager.getLobby());
                break;

            case DISCONNECT:
                cleanup();
                return; // stop processing after disconnect

            // -------- MS2 ----------
            case DRAW: // Client -> Server: CoordPayload
                if (currentRoom instanceof GameRoom gr) {
                    gr.handleDraw(this, (CoordPayload) p);
                }
                break;

            case GUESS: // Client -> Server: guess text in message
                if (currentRoom instanceof GameRoom gr2) {
                    gr2.handleGuess(this, p.getMessage());
                }
                break;

            default:
                System.out.println("[SERVER] Unhandled payload: " + p);
        }
    }

    private void switchRoom(Room newRoom) {
        if (currentRoom != null) {
            currentRoom.removeClient(this);
        }
        currentRoom = newRoom;
        if (currentRoom != null) {
            currentRoom.addClient(this);
        }
    }

    public void send(Payload p) {
        try {
            out.writeObject(p);
            out.flush();
        } catch (Exception e) {
            System.out.println("[SERVER] Send fail: " + e.getMessage());
        }
    }

    private void cleanup() {
        try { if (currentRoom != null) currentRoom.removeClient(this); } catch (Exception ignore) {}
        try { socket.close(); } catch (Exception ignore) {}
        ServerMain.removeClient(this); // make sure this exists, or remove
    }
}

