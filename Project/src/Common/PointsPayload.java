package Common;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * UCID: oka
 * Date: 2025-07-25
 * Summary: Scoreboard. Uses a LinkedHashMap to preserve sort order (highest first).
 */
public class PointsPayload extends Payload implements Serializable {
    private static final long serialVersionUID = 1L;

    // name -> points
    private LinkedHashMap<String, Integer> scores = new LinkedHashMap<>();

    public PointsPayload(Map<String, Integer> scores) {
        super(PayloadType.POINTS, "scoreboard");
        this.scores.putAll(scores);
    }

    public Map<String, Integer> getScores() { return scores; }

    @Override
    public String toString() {
        return "PointsPayload" + scores;
    }
}

