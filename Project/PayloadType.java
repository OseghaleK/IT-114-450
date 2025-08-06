// UCID: oka
// Date: 2025-07-24
// Summary: Enum of all message/command types exchanged between client and server.

package Common;

public enum PayloadType {
    NAME,            // client sets or updates its display name
    MESSAGE,         // normal room chat message
    CREATE_ROOM,     // request to create a room
    JOIN_ROOM,       // request to join an existing room
    LEAVE_ROOM,      // leave current room (back to lobby)
    DISCONNECT,      // client is quitting
    SYSTEM           // server-to-client informational/system messages
}
