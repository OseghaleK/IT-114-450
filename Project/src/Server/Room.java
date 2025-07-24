package Server;

import java.util.HashSet;
import java.util.Set;

// UCID: oka
// Date: 2025-07-24
// Summary: Represents a chat room; manages membership and broadcasts messages.
public class Room {
    private final String name;
    private final Set<ServerThread> members = new HashSet<>();

    public Room(String name) { this.name = name; }
    public String getName() { return name; }

    public synchronized void add(ServerThread st) {
        members.add(st);
        broadcast("[SYSTEM] " + st.getClientName() + " joined the room.");
    }

    public synchronized void remove(ServerThread st) {
        if (members.remove(st)) {
            broadcast("[SYSTEM] " + st.getClientName() + " left the room.");
        }
    }

    public synchronized void broadcast(String msg) {
        for (ServerThread m : members) {
            m.sendMessage(msg);
        }
    }
}
