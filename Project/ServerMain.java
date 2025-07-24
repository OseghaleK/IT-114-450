// UCID: oka
// Date: 2025-07-24
// Summary: Entry point for server. Creates ServerSocket, accepts connections, spawns ServerThread.

package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArraySet;

public class ServerMain {
    private final int port;
    private final RoomManager roomManager = new RoomManager();
    private final CopyOnWriteArraySet<ServerThread> clients = new CopyOnWriteArraySet<>();

    public ServerMain(int port) {
        this.port = port;
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port + " ...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ServerThread st = new ServerThread(clientSocket, this, roomManager);
                clients.add(st);
                st.start();
                System.out.println("Client connected from " + clientSocket.getInetAddress());
            }
        } catch (IOException e) {
            System.out.println("Server stopped: " + e.getMessage());
        }
    }

    public void removeClient(ServerThread st) {
        clients.remove(st);
    }

    public static void main(String[] args) {
        int port = 5555;
        if (args.length > 0) {
            try { port = Integer.parseInt(args[0]); } catch (NumberFormatException ignored) {}
        }
        new ServerMain(port).startServer();
    }
}
