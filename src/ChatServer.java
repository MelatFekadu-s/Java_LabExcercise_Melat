import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    private static final int PORT = 12345; // server port
    private static Set<ClientHandler> clients = ConcurrentHashMap.newKeySet(); 
    private static DatabaseHelper db = new DatabaseHelper(); // save messages

    public static void main(String[] args) {
        System.out.println("Chat Server started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept(); // wait client
                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);
                new Thread(handler).start(); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // send message to everyone except sender
    public static void broadcast(String message, ClientHandler sender) {
        db.saveMessage("User", message, "text"); 
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    ChatServer.broadcast(message, this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try { socket.close(); } catch (IOException e) {}
                clients.remove(this);
            }
        }

        void sendMessage(String message) {
            out.println(message);
        }
    }
}
