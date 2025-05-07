import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ChatClientGUI extends JFrame {
    private JTextField messageField;
    private JPanel chatPanel;
    private String name;
    private ChatClient client;
    private static final Color MY_BUBBLE_COLOR = new Color(100, 149, 237); // Blue for your messages
    private static final Color OTHER_BUBBLE_COLOR = new Color(220, 220, 220); // Gray for others
    private static final Color SENDER_COLOR = new Color(200, 50, 50); // Red for sender name
    private static final Color SYSTEM_COLOR = new Color(50, 50, 50); // Dark gray for system messages
    private static final Font MESSAGE_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 14);
    private static final Font SENDER_FONT = new Font("Segoe UI Emoji", Font.BOLD, 14);
    private static final Font SYSTEM_FONT = new Font("Segoe UI Emoji", Font.ITALIC, 14);

    public ChatClientGUI(String name) {
        super("606 ChatApp - " + name);
        this.name = name;
        System.out.println("Initializing chat interface for user: " + name);

        setSize(500, 400);
        setMinimumSize(new Dimension(400, 300));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(61, 11, 136));

        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(new Color(238, 238, 238));

        JScrollPane scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        messageField = new JTextField();
        messageField.setFont(MESSAGE_FONT);
        messageField.addActionListener(e -> sendMessage());

        JButton sendButton = new JButton("Send");
        sendButton.setBackground(new Color(167, 29, 251));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.addActionListener(e -> sendMessage());

        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(167, 29, 251));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.addActionListener(e -> {
            System.out.println("User " + name + " is leaving");
            client.sendExit();
            Timer exitTimer = new Timer(1000, event -> System.exit(0));
            exitTimer.setRepeats(false);
            exitTimer.start();
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout(10, 10));
        inputPanel.setBackground(new Color(61, 11, 136));
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(61, 11, 136));
        bottomPanel.add(exitButton);

        setLayout(new BorderLayout(10, 10));
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.NORTH);

        // Initialize client
        this.client = new ChatClient("127.0.0.1", 5000, this::onMessageReceived);
        client.startClient();
        client.sendName(name); // Send name to server
        System.out.println("Connected to server");
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            client.sendMessage(name + ": " + message);
            messageField.setText("");
        }
    }

    private void onMessageReceived(String message) {
        System.out.println("Received message: " + message);
        SwingUtilities.invokeLater(() -> {
            if (message.contains("has joined the chat") || message.contains("has left the chat")) {
                addSystemMessage(message);
            } else {
                addMessageBubble(message);
            }
        });
    }

    private void addMessageBubble(String message) {
        JPanel bubblePanel = new JPanel();
        boolean isMyMessage = message.startsWith(name + ": ");
        bubblePanel.setLayout(new FlowLayout(isMyMessage ? FlowLayout.RIGHT : FlowLayout.LEFT));
        bubblePanel.setBackground(new Color(238, 238, 238));
        bubblePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel bubble = new JPanel();
        bubble.setLayout(new BorderLayout());
        bubble.setBackground(isMyMessage ? MY_BUBBLE_COLOR : OTHER_BUBBLE_COLOR);
        bubble.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        bubble.setOpaque(true);

        String sender = message.substring(0, message.indexOf(": "));
        String content = message.substring(message.indexOf(": ") + 2);

        JLabel senderLabel = new JLabel(sender);
        senderLabel.setFont(SENDER_FONT);
        senderLabel.setForeground(SENDER_COLOR);

        JLabel messageLabel = new JLabel("<html>" + content.replace("\n", "<br>") + "</html>");
        messageLabel.setFont(MESSAGE_FONT);
        messageLabel.setForeground(Color.BLACK);

        bubble.add(senderLabel, BorderLayout.NORTH);
        bubble.add(messageLabel, BorderLayout.CENTER);

        bubblePanel.add(bubble);
        chatPanel.add(bubblePanel);
        chatPanel.add(Box.createVerticalStrut(5));
        chatPanel.revalidate();
        chatPanel.repaint();

        JScrollBar vertical = ((JScrollPane) chatPanel.getParent().getParent()).getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private void addSystemMessage(String message) {
        JPanel systemPanel = new JPanel();
        systemPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        systemPanel.setBackground(new Color(238, 238, 238));
        systemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel systemLabel = new JLabel(message);
        systemLabel.setFont(SYSTEM_FONT);
        systemLabel.setForeground(SYSTEM_COLOR);

        systemPanel.add(systemLabel);
        chatPanel.add(systemPanel);
        chatPanel.add(Box.createVerticalStrut(5));
        chatPanel.revalidate();
        chatPanel.repaint();

        JScrollBar vertical = ((JScrollPane) chatPanel.getParent().getParent()).getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }
}