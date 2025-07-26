package Server;

import java.util.HashMap;
import java.util.Map;

/**
 * UCID: oka
 * Date: 2025-07-25
 * Summary: Manages rooms. Lobby is default; can create GameRooms.
 */
public class RoomManager {
    private final Map<String, Room> rooms = new HashMap<>();

    public RoomManager() {
        rooms.put("lobby", new Room("lobby", this));
    }

    public synchronized Room getRoom(String name) {
        return rooms.get(name.toLowerCase());
    }

    public synchronized Room getOrCreateRoom(String name, boolean gameRoom) {
        name = name.toLowerCase();
        Room r = rooms.get(name);
        if (r == null) {
            if (gameRoom) {
                r = new GameRoom(name, this);
            } else {
                r = new Room(name, this);
            }
            rooms.put(name, r);
        }
        return r;
    }

    public synchronized Room getLobby() {
        return rooms.get("lobby");
    }
}

