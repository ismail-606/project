import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Consumer<String> onMessageReceived;

    public ChatClient(String host, int port, Consumer<String> onMessageReceived) {
        this.onMessageReceived = onMessageReceived;
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void startClient() {
        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equals("SUBMIT_NAME")) {
                        // Name will be sent from LoginFrame
                        continue;
                    }
                    onMessageReceived.accept(message);
                }
            } catch (IOException e) {
                System.err.println("Error receiving messages: " + e.getMessage());
            }
        }).start();
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void sendName(String name) {
        out.println(name);
    }

    public void sendExit() {
        out.println("EXIT");
    }
}