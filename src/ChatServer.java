import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 5000;
    private static HashSet<ClientHandler> clients = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Request client name
                out.println("SUBMIT_NAME");
                clientName = in.readLine();
                if (clientName == null || clientName.trim().isEmpty()) {
                    clientName = "Anonymous";
                }
                System.out.println("New client joined: " + clientName);

                // Broadcast join notification
                broadcast(clientName + " has joined the chat.", true);

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equals("EXIT")) {
                        break;
                    }
                    System.out.println("Received: " + message);
                    broadcast(message, message.startsWith(clientName + ": "));
                }
            } catch (IOException e) {
                System.err.println("Error with client " + clientName + ": " + e.getMessage());
            } finally {
                try {
                    // Broadcast leave notification
                    broadcast(clientName + " has left the chat.", true);
                    clients.remove(this);
                    socket.close();
                    System.out.println("Client " + clientName + " left");
                } catch (IOException e) {
                    System.err.println("Error closing client connection: " + e.getMessage());
                }
            }
        }

        private void broadcast(String message, boolean isSystemMessage) {
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    if (isSystemMessage || client != this) {
                        client.out.println(message);
                    }
                }
            }
        }
    }
}