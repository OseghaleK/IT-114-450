// UCID: oka
// Date: 2025-07-29
// Summary: Milestone-3 updates – adds away/spectate payloads and board export.
package Server;

import java.util.*;
import Common.*;

public class GameRoom extends Room {
    private final Map<Integer,Integer> points = new HashMap<>();
    private final Random rnd = new Random();

    private static final int WIDTH = 20, HEIGHT = 12;

    private int currentDrawerId = -1;
    private String currentWord = "apple";

    public GameRoom(String name) { super(name); }
    public GameRoom(String name, RoomManager manager) { super(name, manager); }

    public synchronized void onSessionStart() {
        sendSystemToAll("Game session started.");
        broadcast(new DimensionPayload(WIDTH, HEIGHT));
        startRound();
    }

    private void startRound() {
        List<ServerThread> ms = getMembers();
        if (ms.isEmpty()) return;

        ServerThread drawer = ms.get(rnd.nextInt(ms.size()));
        currentDrawerId = drawer.getClientId();

        String blanks = currentWord.replaceAll("[A-Za-z]", "_");
        broadcast(new RoundStartPayload(currentDrawerId, blanks));
    }

    public void handleDraw(ServerThread from, CoordPayload cp) {
        if (from.getClientId() != currentDrawerId) return;
        cp.setPayloadType(PayloadType.DRAW_SYNC);
        broadcast(cp);
    }

    public void handleGuess(ServerThread from, String guess) {
        if (guess == null) return;
        if (guess.trim().equalsIgnoreCase(currentWord)) {
            int id = from.getClientId();
            points.put(id, points.getOrDefault(id, 0) + 1);

            broadcast(new RoundEndPayload(currentWord));
            broadcast(new PointsPayload((java.util.Map)points));

            currentWord = "banana";
            startRound();
        } else {
            sendToAll(from.getClientName() + ": " + guess, from);
        }
    }
}



