import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToeGUI extends JFrame implements ActionListener {

    private JButton[][] buttons = new JButton[3][3];
    private char currentPlayer = 'X';

    private int xWins = 0, oWins = 0, draws = 0;

    private JLabel statusLabel;
    private JLabel scoreLabel;
    private JButton resetButton;

    public TicTacToeGUI() {
        setTitle("Tic Tac Toe - GUI Version");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.DARK_GRAY);

        // --- TOP PANEL ---
        statusLabel = new JLabel("Player X's turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 22));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(statusLabel, BorderLayout.NORTH);

        // --- CENTER PANEL (Grid) ---
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 3, 5, 5)); // Added 5px gaps between buttons
        gridPanel.setBackground(Color.DARK_GRAY);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font btnFont = new Font("Arial", Font.BOLD, 60); // Increased font size

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(btnFont);
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setBackground(Color.LIGHT_GRAY);
                buttons[i][j].addActionListener(this);
                gridPanel.add(buttons[i][j]);
            }
        }

        mainPanel.add(gridPanel, BorderLayout.CENTER);

        // --- BOTTOM PANEL (Score & Controls) ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBackground(Color.DARK_GRAY);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scoreLabel = new JLabel(getScoreText(), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(Color.WHITE);
        bottomPanel.add(scoreLabel, BorderLayout.CENTER);

        resetButton = new JButton("Restart Match");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setFocusPainted(false);
        resetButton.setBackground(new Color(220, 53, 69)); // A nice red color
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(e -> resetBoard(true)); // True means a full manual reset
        bottomPanel.add(resetButton, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private String getScoreText() {
        return "Score → X: " + xWins + " | O: " + oWins + " | Draws: " + draws;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();

        // Prevent clicking on an already filled button
        if (!btn.getText().equals("")) {
            return; 
        }

        // Set text and assign distinct colors based on the player
        btn.setText(String.valueOf(currentPlayer));
        btn.setForeground(currentPlayer == 'X' ? new Color(0, 122, 255) : new Color(255, 59, 48));

        if (checkWin()) {
            // Update score
            if (currentPlayer == 'X') xWins++;
            else oWins++;
            scoreLabel.setText(getScoreText());
            
            // Show message and reset
            JOptionPane.showMessageDialog(this, "Player " + currentPlayer + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            resetBoard(false);
            return;
        }

        if (isBoardFull()) {
            draws++;
            scoreLabel.setText(getScoreText());
            JOptionPane.showMessageDialog(this, "It's a draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            resetBoard(false);
            return;
        }

        // Switch turns
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        statusLabel.setText("Player " + currentPlayer + "'s turn");
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (buttons[i][j].getText().equals("")) return false;
        return true;
    }

    private boolean checkWin() {
        String p = String.valueOf(currentPlayer);

        // Check Rows and Columns
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(p) && buttons[i][1].getText().equals(p) && buttons[i][2].getText().equals(p)) {
                highlightWinningLine(buttons[i][0], buttons[i][1], buttons[i][2]);
                return true;
            }
            if (buttons[0][i].getText().equals(p) && buttons[1][i].getText().equals(p) && buttons[2][i].getText().equals(p)) {
                highlightWinningLine(buttons[0][i], buttons[1][i], buttons[2][i]);
                return true;
            }
        }

        // Check Diagonals
        if (buttons[0][0].getText().equals(p) && buttons[1][1].getText().equals(p) && buttons[2][2].getText().equals(p)) {
            highlightWinningLine(buttons[0][0], buttons[1][1], buttons[2][2]);
            return true;
        }
        if (buttons[0][2].getText().equals(p) && buttons[1][1].getText().equals(p) && buttons[2][0].getText().equals(p)) {
            highlightWinningLine(buttons[0][2], buttons[1][1], buttons[2][0]);
            return true;
        }

        return false;
    }

    private void highlightWinningLine(JButton b1, JButton b2, JButton b3) {
        Color winColor = new Color(50, 205, 50); // Lime Green
        b1.setBackground(winColor);
        b2.setBackground(winColor);
        b3.setBackground(winColor);
    }

    private void resetBoard(boolean manualReset) {
        // Clear the text and reset the background color
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(Color.LIGHT_GRAY);
            }
        }

        // If manually restarted, keep X as the starting player
        currentPlayer = 'X';
        statusLabel.setText("Player X's turn");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToeGUI());
    }
}
