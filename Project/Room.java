// UCID: oka
// Date: 2025-07-24
// Summary: Holds clients in a room and provides broadcast helpers.

package Server;

import Common.Payload;
import Common.PayloadType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Room {
    private final String name;
    private final Set<ServerThread> members = Collections.synchronizedSet(new HashSet<>());

    public Room(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void add(ServerThread st) {
        members.add(st);
        broadcastSystem(st.getClientName() + " joined the room.");
    }

    public void remove(ServerThread st) {
        if (members.remove(st)) {
            broadcastSystem(st.getClientName() + " left the room.");
        }
    }

    public void broadcastMessage(ServerThread sender, String msg) {
        Payload p = new Payload(PayloadType.MESSAGE);
        p.setMessage(msg);
        p.setFrom(sender.getClientName());
        p.setFromId(sender.getClientId());
        p.setRoom(name);
        synchronized (members) {
            for (ServerThread st : members) {
                st.send(p);
            }
        }
    }

    public void broadcastSystem(String msg) {
        Payload p = Payload.system(msg);
        p.setRoom(name);
        synchronized (members) {
            for (ServerThread st : members) {
                st.send(p);
            }
        }
    }
}
