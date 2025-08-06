package Server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// UCID: oka
// Date: 2025-07-24
// Summary: Creates and returns rooms. Always provides a default "lobby".
public class RoomManager {
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();

    public RoomManager() {
        rooms.put("lobby", new Room("lobby"));
    }

    public Room getOrCreate(String name) {
        return rooms.computeIfAbsent(name, Room::new);
    }

    public Room get(String name) {
        return rooms.get(name);
    }
}
