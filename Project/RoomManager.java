// UCID: oka
// Date: 2025-07-24
// Summary: Central registry of all rooms, with a permanent "lobby".

package Server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    public static final String LOBBY = "lobby";

    private final Map<String, Room> rooms = new ConcurrentHashMap<>();

    public RoomManager() {
        rooms.put(LOBBY, new Room(LOBBY));
    }

    public Room getLobby() {
        return rooms.get(LOBBY);
    }

    public Room get(String name) {
        return rooms.get(name);
    }

    public Room getOrCreate(String name) {
        return rooms.computeIfAbsent(name, Room::new);
    }

    public boolean exists(String name) {
        return rooms.containsKey(name);
    }

    public void removeIfEmpty(String name) {
        if (LOBBY.equals(name)) return; // never remove lobby
        Room r = rooms.get(name);
        if (r != null && rIsEmpty(r)) {
            rooms.remove(name);
        }
    }

    private boolean rIsEmpty(Room r) {
        return false; 
    }
}
