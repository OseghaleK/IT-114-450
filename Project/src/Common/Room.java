// UCID: oka
// Date: 2025-07-29
// Summary: Milestone-3 updates – adds away/spectate payloads and board export.
package Common;

import java.util.ArrayList;
import java.util.List;

import Server.ServerThread;
import Server.RoomManager;

public class Room {
    private final String name;
    private final List<ServerThread> members = new ArrayList<>();
    private RoomManager manager;

    public Room(String name) { this.name = name; }

    public Room(String name, RoomManager manager) {
        this.name = name;
        this.manager = manager;
    }

    public String getName() { return name; }

    public RoomManager getManager() { return manager; }
    public void setManager(RoomManager manager) { this.manager = manager; }

    public void addMember(ServerThread st) {
        if (st == null) return;
        synchronized (members) {
            if (!members.contains(st)) members.add(st);
        }
    }

    public void removeMember(ServerThread st) {
        if (st == null) return;
        synchronized (members) { members.remove(st); }
    }

    // Compatibility methods expected by ServerThread
    public void addClient(ServerThread st) { addMember(st); }
    public void removeClient(ServerThread st) { removeMember(st); }

    /** Broadcast a ready-made payload to everyone. */
    public void broadcast(Payload p) {
        if (p == null) return;
        p.setRoom(name);
        synchronized (members) {
            for (ServerThread m : members) {
                m.send(p);
            }
        }
    }

    /** Broadcast a normal chat message from a sender to everyone in this room. */
    public void sendToAll(String msg, ServerThread sender) {
        Payload p = new Payload(PayloadType.MESSAGE, msg);
        if (sender != null) {
            p.setFrom(sender.getClientName());
            p.setFromId(sender.getClientId());
        }
        broadcast(p);
    }

    /** Broadcast a system/server message. */
    public void sendSystemToAll(String msg) {
        Payload p = Payload.systemMessage(msg);
        broadcast(p);
    }

    public List<ServerThread> getMembers() {
        synchronized (members) { return new ArrayList<>(members); }
    }
}
