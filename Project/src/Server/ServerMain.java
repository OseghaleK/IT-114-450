package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// UCID: oka
// Date: 2025-07-24
// Summary: Boots the server, accepts sockets, spawns ServerThread instances.
public class ServerMain {
    private final int port;
    private final RoomManager roomManager = new RoomManager();
    private final Set<ServerThread> clients = ConcurrentHashMap.newKeySet();

    public ServerMain(int port) { this.port = port; }

    public RoomManager getRoomManager() { return roomManager; }
    public void removeClient(ServerThread st) { clients.remove(st); }

    public void startServer() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port + " ...");
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThread st = new ServerThread(socket, this);
                clients.add(st);
                st.start();
                System.out.println("Client connected from " + socket.getInetAddress());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        int port = (args.length > 0) ? Integer.parseInt(args[0]) : 5555;
        new ServerMain(port).startServer();
    }
}
