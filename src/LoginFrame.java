import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JButton joinButton;

    public LoginFrame() {
        super("Login - 606 ChatApp");
        System.out.println("Initializing login interface");

        // Set window properties
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create gradient panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(61, 11, 136), 0, getHeight(), new Color(167, 29, 251));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Username field
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        usernameField.setText("Enter username");
        usernameField.setForeground(Color.GRAY);
        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals("Enter username")) {
                    usernameField.setText("");
                    usernameField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText("Enter username");
                    usernameField.setForeground(Color.GRAY);
                }
            }
        });
        usernameField.addActionListener(e -> joinChat());

        // Join button
        joinButton = new JButton("Join Chat") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        joinButton.setBackground(new Color(167, 29, 251));
        joinButton.setForeground(Color.WHITE);
        joinButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        joinButton.setFocusPainted(false);
        joinButton.setBorderPainted(false);
        joinButton.setContentAreaFilled(false);
        joinButton.setOpaque(false);
        joinButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        joinButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                joinButton.setBackground(new Color(200, 60, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                joinButton.setBackground(new Color(167, 29, 251));
            }
        });
        joinButton.addActionListener(e -> joinChat());

        // Layout components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(usernameLabel, gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(joinButton, gbc);

        add(panel);
    }

    private void joinChat() {
        String username = usernameField.getText().trim();
        if (username.equals("Enter username")) {
            username = "";
        }
        boolean isValid = !username.isEmpty() && username.matches("[a-zA-Z0-9]+");
        System.out.println("Username: " + username + ", Valid: " + isValid);

        if (isValid) {
            System.out.println("User " + username + " is attempting to join the chat");
            try {
                ChatClientGUI chatGUI = new ChatClientGUI(username);
                dispose();
                chatGUI.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to connect to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid username (letters and numbers only).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}