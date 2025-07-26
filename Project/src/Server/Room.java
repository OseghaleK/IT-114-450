package Server;

import Common.Payload;

import java.util.HashSet;
import java.util.Set;

/**
 * UCID: oka
 * Date: 2025-07-25
 * Summary: Base room for chat. GameRoom extends this and overrides hooks.
 */
public class Room {
    protected final String name;
    protected final RoomManager manager;
    protected final Set<ServerThread> clients = new HashSet<>();

    public Room(String name, RoomManager manager) {
        this.name = name;
        this.manager = manager;
    }

    public String getName() { return name; }

    public synchronized void addClient(ServerThread st) {
        clients.add(st);
        onClientAdded(st);
        broadcast(new Payload(Common.PayloadType.SERVER_NOTIFICATION,
                st.getClientName() + " joined " + name));
    }

    public synchronized void removeClient(ServerThread st) {
        clients.remove(st);
        onClientRemoved(st);
        broadcast(new Payload(Common.PayloadType.SERVER_NOTIFICATION,
                st.getClientName() + " left " + name));
    }

    public synchronized void broadcast(Payload p) {
        for (ServerThread st : clients) {
            st.send(p);
        }
    }

    // Hooks for subclasses
    public void onClientAdded(ServerThread st) {}
    public void onClientRemoved(ServerThread st) {}
}

