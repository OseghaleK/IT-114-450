package Server;

/**
 * UCID: oka
 * Date: 2025-07-25
 * Summary: Simple model for a player in the game (name + points + guessed flag).
 */
public class Player {
    private final ServerThread thread; // who this is
    private int points = 0;
    private boolean guessedThisRound = false;

    public Player(ServerThread st) {
        this.thread = st;
    }

    public String getName() { return thread.getClientName(); }
    public int getPoints()   { return points; }
    public void addPoints(int amt) { points += amt; }

    public boolean hasGuessed() { return guessedThisRound; }
    public void setGuessed(boolean g) { guessedThisRound = g; }

    public ServerThread getThread() { return thread; }
}

