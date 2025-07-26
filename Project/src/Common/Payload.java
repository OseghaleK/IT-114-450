package Common;

import java.io.Serializable;

public class Payload implements Serializable {
    private PayloadType type;
    private String message;

    private int senderId = -1;
    private String senderName;

    public Payload(PayloadType type) {
        this.type = type;
    }

    public Payload(PayloadType type, String message) {
        this.type = type;
        this.message = message;
    }

    // --- what other code calls ---
    public PayloadType getType() { return type; }              // switch(p.getType())
    public PayloadType getPayloadType() { return type; }       // in case some files still use this
    public String getMessage() { return message; }
    public void setMessage(String m) { this.message = m; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int id) { this.senderId = id; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String n) { this.senderName = n; }

    @Override
    public String toString() {
        return "Payload{type=" + type + ", message='" + message + "', senderId=" + senderId +
               ", senderName='" + senderName + "'}";
    }
}