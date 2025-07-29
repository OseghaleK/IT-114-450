package Common;

import java.io.Serializable;

public enum PayloadType implements Serializable {
    // MS1
    NAME,
    MESSAGE,
    CREATE_ROOM,
    JOIN_ROOM,
    LEAVE_ROOM,
    DISCONNECT,

    // MS2
    DIMENSION,
    DRAW,
    GUESS,
    DRAW_SYNC,
    POINTS,
    ROUND_START,
    ROUND_END,
    SESSION_END,
    ASSIGN_ID,
    SERVER_NOTIFICATION
}

