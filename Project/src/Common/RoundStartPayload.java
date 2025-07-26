package Common;

import java.io.Serializable;

/**
 * UCID: oka
 * Date: 2025-07-25
 * Summary: Announces a new round. Tells clients who draws and shows blanks to guessers.
 */
public class RoundStartPayload extends Payload implements Serializable {
    private final int drawerId;
    private final String blanks;

    public RoundStartPayload(int drawerId, String blanks) {
        super(PayloadType.ROUND_START, "");
        this.drawerId = drawerId;
        this.blanks = blanks;
    }

    public int getDrawerId() { return drawerId; }
    public String getBlanks() { return blanks; }

    @Override
    public String toString() {
        return "RoundStartPayload{drawerId=" + drawerId + ", blanks='" + blanks + "'}";
    }
}
