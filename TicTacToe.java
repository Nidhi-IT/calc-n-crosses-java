import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToeGUI extends JFrame implements ActionListener {

    private JButton[][] buttons = new JButton[3][3];
    private char currentPlayer = 'X';

    private int xWins = 0, oWins = 0, draws = 0;

    private JLabel statusLabel;
    private JLabel scoreLabel;

    public TicTacToeGUI() {
        setTitle("Tic Tac Toe - GUI Version");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

     
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.GRAY);

        
        statusLabel = new JLabel("Player X's turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 22));
        statusLabel.setForeground(Color.WHITE);
        mainPanel.add(statusLabel, BorderLayout.NORTH);


        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 3));
        gridPanel.setBackground(Color.GRAY);

      
        Font btnFont = new Font("Arial", Font.BOLD, 48);

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

        scoreLabel = new JLabel(getScoreText(), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setForeground(Color.WHITE);
        mainPanel.add(scoreLabel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private String getScoreText() {
        return "Score → X Wins: " + xWins + " | O Wins: " + oWins + " | Draws: " + draws;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();

        if (!btn.getText().equals("")) {
            return; 
        }

        btn.setText(String.valueOf(currentPlayer));

        if (checkWin()) {
            JOptionPane.showMessageDialog(this, "Player " + currentPlayer + " wins!");
            if (currentPlayer == 'X') xWins++;
            else oWins++;
            resetBoard();
            scoreLabel.setText(getScoreText());
            return;
        }

        if (isBoardFull()) {
            JOptionPane.showMessageDialog(this, "It's a draw!");
            draws++;
            resetBoard();
            scoreLabel.setText(getScoreText());
            return;
        }

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

      
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(p) &&
                buttons[i][1].getText().equals(p) &&
                buttons[i][2].getText().equals(p))
                return true;

            if (buttons[0][i].getText().equals(p) &&
                buttons[1][i].getText().equals(p) &&
                buttons[2][i].getText().equals(p))
                return true;
        }

        
        return (buttons[0][0].getText().equals(p) &&
                buttons[1][1].getText().equals(p) &&
                buttons[2][2].getText().equals(p)) ||

               (buttons[0][2].getText().equals(p) &&
                buttons[1][1].getText().equals(p) &&
                buttons[2][0].getText().equals(p));
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                buttons[i][j].setText("");

        currentPlayer = 'X';
        statusLabel.setText("Player X's turn");
    }

    public static void main(String[] args) {
        new TicTacToeGUI();
    }
}
