package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * UCID: oka
 * Date: 2025-07-25
 * Summary: Starts server, accepts sockets, hands them to ServerThread.
 */
public class ServerMain {
    private static final CopyOnWriteArrayList<ServerThread> CLIENTS = new CopyOnWriteArrayList<>();
    private static final RoomManager ROOM_MANAGER = new RoomManager();

    public static void main(String[] args) throws Exception {
        int port = (args.length > 0) ? Integer.parseInt(args[0]) : 5555;
        ServerSocket ss = new ServerSocket(port);
        System.out.println("Server listening on port " + port + " ...");

        while (true) {
            Socket s = ss.accept();
            ServerThread st = new ServerThread(s, ROOM_MANAGER);
            CLIENTS.add(st);
            st.start();
        }
    }

    public static void removeClient(ServerThread st) {
        CLIENTS.remove(st);
    }
}

