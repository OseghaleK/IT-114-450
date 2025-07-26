package Common;

import java.io.Serializable;

/**
 * UCID: oka
 * Date: 2025-07-25
 * Summary: Sends board width/height to clients.
 */
public class DimensionPayload extends Payload implements Serializable {
    private static final long serialVersionUID = 1L;

    private int width;
    private int height;

    public DimensionPayload(int width, int height) {
        super(PayloadType.DIMENSION, width + "x" + height);
        this.width = width;
        this.height = height;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    @Override
    public String toString() {
        return "DimensionPayload{w=" + width + ", h=" + height + "}";
    }
}

