// UCID: oka
// Date: 2025-07-24
// Summary: Serializable object passed over the socket containing a type and associated data.

package Common;

import java.io.Serializable;

public class Payload implements Serializable {
    private static final long serialVersionUID = 1L;

    private PayloadType type;
    private String message;    // generic string payload (chat text, errors, etc.)
    private String room;       // room name for create/join/leave
    private String from;       // sender display name (server fills this in when broadcasting)
    private int fromId;        // optional unique id (server assigns)

    public Payload(PayloadType type) {
        this.type = type;
    }

    // builder helpers
    public static Payload message(String msg) {
        Payload p = new Payload(PayloadType.MESSAGE);
        p.setMessage(msg);
        return p;
    }

    public static Payload system(String msg) {
        Payload p = new Payload(PayloadType.SYSTEM);
        p.setMessage(msg);
        return p;
    }

    // getters/setters
    public PayloadType getType() { return type; }
    public void setType(PayloadType type) { this.type = type; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public int getFromId() { return fromId; }
    public void setFromId(int fromId) { this.fromId = fromId; }

    @Override
    public String toString() {
        return "Payload{" +
                "type=" + type +
                ", message='" + message + '\'' +
                ", room='" + room + '\'' +
                ", from='" + from + '\'' +
                ", fromId=" + fromId +
                '}';
    }
}
