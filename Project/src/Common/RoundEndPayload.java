package Common;

import java.io.Serializable;

/**
 * UCID: oka
 * Date: 2025-07-25
 * Summary: Announces round end and the correct word.
 */
public class RoundEndPayload extends Payload implements Serializable {
    private final String word;

    public RoundEndPayload(String word) {
        super(PayloadType.ROUND_END, "");
        this.word = word;
    }

    public String getWord() { return word; }

    @Override
    public String toString() {
        return "RoundEndPayload{word='" + word + "'}";
    }
}
