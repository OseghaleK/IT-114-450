package Server;

import Common.*;
import java.util.*;

/**
 * UCID: oka
 * Date: 2025-07-25
 * Summary: Minimal Pictionary/Drawing GameRoom.
 *  - Keeps a boolean board
 *  - Chooses/rotates a drawer
 *  - Sends DIMENSION, ROUND_START, DRAW_SYNC, ROUND_END payloads
 */
public class GameRoom extends Room {
    // --- board ---
    private final int width  = 10;
    private final int height = 10;
    private final boolean[][] board = new boolean[height][width];

    // --- word list / round state ---
    private final List<String> words = new ArrayList<>(Arrays.asList(
            "cat","dog","tree","house","car","phone","book","apple","chair","table"
    ));
    private String currentWord = null;
    private ServerThread drawer = null;
    private final List<ServerThread> correctGuessers = new ArrayList<>();

    public GameRoom(String name) {
        super(name);
    }

    /* ===================== Lifecycle ===================== */

    /** Called when a session starts; size sync + first round. */
    public synchronized void onSessionStart() {
        // Sync board size to all clients
        broadcast(new DimensionPayload(width, height));
        startNextRound();
    }

    /** Start/prepare the next round. Picks drawer & word; sends blanks to all, word to drawer. */
    private synchronized void startNextRound() {
        if (clients.isEmpty()) {
            // Nothing to do
            currentWord = null;
            drawer = null;
            correctGuessers.clear();
            return;
        }
        // pick/rotate drawer
        drawer = pickNextDrawer(drawer);

        // pick word
        if (words.isEmpty()) {
            // End session if no words left
            broadcast(new Payload(PayloadType.SESSION_END, "Session over! No more words."));
            currentWord = null;
            return;
        }
        currentWord = words.remove(0);
        correctGuessers.clear();

        String blanks = currentWord.replaceAll(".", "_");

        // Tell drawer the real word (simple server notice)
        if (drawer != null) {
            drawer.send(new Payload(PayloadType.SERVER_NOTIFICATION,
                    "You are the drawer. Word: " + currentWord));
        }

        // Send blanks + who is drawer to everyone
        broadcast(new RoundStartPayload(drawer != null ? drawer.getClientId() : -1, blanks));
    }

    /** End current round, clear board, broadcast answer, move on. */
    private synchronized void endRound() {
        if (currentWord != null) {
            broadcast(new RoundEndPayload(currentWord));
        }
        // Clear the board
        for (int y = 0; y < height; y++) {
            Arrays.fill(board[y], false);
        }
        currentWord = null;

        // Start the next round
        startNextRound();
    }

    /** Helper: choose next drawer in simple round-robin order. */
    private ServerThread pickNextDrawer(ServerThread previous) {
        if (clients.isEmpty()) return null;
        List<ServerThread> list = new ArrayList<>(clients);
        if (previous == null) {
            return list.get(0);
        }
        int idx = list.indexOf(previous);
        if (idx < 0 || idx == list.size() - 1) {
            return list.get(0);
        }
        return list.get(idx + 1);
    }

    /* ===================== Room hooks ===================== */

    @Override
    protected synchronized void onClientAdded(ServerThread st) {
        // Parent behavior (adds to set, join message, etc.)
        super.onClientAdded(st);

        // Size sync to the joining client
        st.send(new DimensionPayload(width, height));

        // Send already drawn pixels so late joiners see the board
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (board[y][x]) {
                    st.send(new CoordPayload(x, y, "black"));
                }
            }
        }

        // If a round is in progress, re-send the current ROUND_START info to the joiner
        if (currentWord != null && drawer != null) {
            String blanks = currentWord.replaceAll(".", "_");
            st.send(new RoundStartPayload(drawer.getClientId(), blanks));
        }
    }

    @Override
    protected synchronized void onClientRemoved(ServerThread st) {
        super.onClientRemoved(st);
        correctGuessers.remove(st);

        // If drawer left, end the round early (and rotate)
        if (st == drawer) {
            endRound();
        }

        // If everyone left, reset
        if (clients.isEmpty()) {
            currentWord = null;
            drawer = null;
            correctGuessers.clear();
        }
    }

    /* ===================== Game actions ===================== */

    /** Only the drawer may draw; server validates; broadcasts DRAW_SYNC on success. */
    public synchronized void handleDraw(ServerThread st, CoordPayload cp) {
        if (st != drawer) {
            st.send(new Payload(PayloadType.SERVER_NOTIFICATION, "You are not the drawer."));
            return;
        }
        int x = cp.getX();
        int y = cp.getY();
        if (x < 0 || x >= width || y < 0 || y >= height) {
            st.send(new Payload(PayloadType.SERVER_NOTIFICATION, "Out of bounds."));
            return;
        }
        if (board[y][x]) {
            st.send(new Payload(PayloadType.SERVER_NOTIFICATION, "Already drawn there."));
            return;
        }
        board[y][x] = true;
        broadcast(new CoordPayload(x, y, cp.getColor() == null ? "black" : cp.getColor()));
    }

    /** Guesser submits a word; server checks and notifies; ends round if all guessers are done. */
    public synchronized void handleGuess(ServerThread st, String guess) {
        if (currentWord == null) {
            st.send(new Payload(PayloadType.SERVER_NOTIFICATION, "No active round."));
            return;
        }
        if (st == drawer) {
            st.send(new Payload(PayloadType.SERVER_NOTIFICATION, "Drawer cannot guess."));
            return;
        }
        String g = (guess == null) ? "" : guess.trim();
        if (g.isEmpty()) return;

        if (currentWord.equalsIgnoreCase(g)) {
            if (!correctGuessers.contains(st)) {
                correctGuessers.add(st);
                broadcast(new Payload(PayloadType.MESSAGE,
                        st.getClientName() + " guessed correctly!"));
            }
            // If everyone except drawer guessed, end round early
            int totalGuessers = Math.max(0, clients.size() - 1);
            if (correctGuessers.size() >= totalGuessers) {
                endRound();
            }
        } else {
            broadcast(new Payload(PayloadType.MESSAGE,
                    st.getClientName() + " guessed '" + g + "' and it wasn?t correct."));
        }
    }
}
