package Common;

import java.io.Serializable;

// UCID: oka
// Date: 2025-07-24
// Summary: Serializable wrapper object used to send data between client and server.
public class Payload implements Serializable {
    private static final long serialVersionUID = 1L;

    private final PayloadType type;
    private final String data;

    public Payload(PayloadType type, String data) {
        this.type = type;
        this.data = data;
    }

    public PayloadType getType() { return type; }
    public String getData() { return data; }

    @Override
    public String toString() {
        return "Payload{type=" + type + ", data='" + data + "'}";
    }
}
