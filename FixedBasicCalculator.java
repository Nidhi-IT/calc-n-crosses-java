import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class FixedBasicCalculator extends JFrame implements ActionListener {

    private JTextField inputField, resultField;
    private JTextArea historyArea;
    private JScrollPane historyScroll;
    private JButton toggleHistoryButton, themeToggleButton;
    private JPanel topPanel, buttonPanel;
    
    private boolean isHistoryVisible = true;
    private boolean isDarkTheme = false;
    private StringBuilder historyLog = new StringBuilder();
    private List<JButton> allButtons = new ArrayList<>();

    // State to track if we need to clear the screen on the next number press
    private boolean resetInputOnNextNumber = false;

    public FixedBasicCalculator() {
        setTitle("BODMAS Calculator");
        setSize(420, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); 
        
        // --- TOP PANEL (Displays and Controls) ---
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        inputField = new JTextField();
        inputField.setEditable(false);
        inputField.setFont(new Font("Arial", Font.BOLD, 26));
        inputField.setHorizontalAlignment(JTextField.RIGHT);
        
        resultField = new JTextField();
        resultField.setEditable(false);
        resultField.setFont(new Font("Arial", Font.ITALIC, 14));
        resultField.setForeground(Color.GRAY);
        resultField.setBorder(null);
        resultField.setHorizontalAlignment(JTextField.RIGHT);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        controlPanel.setOpaque(false); 
        
        toggleHistoryButton = new JButton("Hide History");
        toggleHistoryButton.addActionListener(e -> toggleHistory());
        themeToggleButton = new JButton("Dark Theme");
        themeToggleButton.addActionListener(e -> toggleTheme());
        
        controlPanel.add(toggleHistoryButton);
        controlPanel.add(Box.createHorizontalStrut(10));
        controlPanel.add(themeToggleButton);

        topPanel.add(inputField);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(resultField);
        topPanel.add(controlPanel);
        
        add(topPanel, BorderLayout.NORTH);

        // --- CENTER PANEL (History) ---
        historyArea = new JTextArea(5, 20);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        historyScroll = new JScrollPane(historyArea);
        historyScroll.setBorder(BorderFactory.createTitledBorder("History"));
        
        JPanel historyContainer = new JPanel(new BorderLayout());
        historyContainer.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        historyContainer.setOpaque(false);
        historyContainer.add(historyScroll, BorderLayout.CENTER);
        add(historyContainer, BorderLayout.CENTER);

        // --- BOTTOM PANEL (Buttons) ---
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 5, 5, 5)); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        // Updated layout to include Brackets '(' ')' and Exponents '^'
        String[] buttons = {
            "C", "(", ")", "/", "Back",
            "7", "8", "9", "*", "CH",
            "4", "5", "6", "-", "^",
            "1", "2", "3", "+", "Copy",
            "0", ".", "", "", "="
        };

        for (String text : buttons) {
            if (text.isEmpty()) {
                buttonPanel.add(new JLabel("")); 
                continue;
            }
            JButton btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.PLAIN, 16));
            btn.addActionListener(this);
            allButtons.add(btn);
            buttonPanel.add(btn);
        }
        
        add(buttonPanel, BorderLayout.SOUTH);

        applyTheme();
        setLocationRelativeTo(null); 
        setVisible(true);
    }

    private void toggleHistory() {
        isHistoryVisible = !isHistoryVisible;
        historyScroll.setVisible(isHistoryVisible);
        toggleHistoryButton.setText(isHistoryVisible ? "Hide History" : "Show History");
        revalidate(); 
        repaint();
    }

    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        themeToggleButton.setText(isDarkTheme ? "Light Theme" : "Dark Theme");
        applyTheme();
    }

    private void applyTheme() {
        Color bg = isDarkTheme ? new Color(43, 43, 43) : new Color(240, 240, 240);
        Color fg = isDarkTheme ? Color.WHITE : Color.BLACK;
        Color btnBg = isDarkTheme ? new Color(60, 63, 65) : Color.LIGHT_GRAY;
        Color fieldBg = isDarkTheme ? new Color(60, 63, 65) : Color.WHITE;

        getContentPane().setBackground(bg);
        topPanel.setBackground(bg);
        buttonPanel.setBackground(bg);
        
        inputField.setBackground(fieldBg);
        inputField.setForeground(fg);
        resultField.setBackground(bg);
        resultField.setForeground(isDarkTheme ? Color.LIGHT_GRAY : Color.GRAY);
        
        historyArea.setBackground(fieldBg);
        historyArea.setForeground(fg);
        historyScroll.setBackground(bg);
        historyScroll.getVerticalScrollBar().setBackground(bg);

        toggleHistoryButton.setBackground(btnBg);
        toggleHistoryButton.setForeground(fg);
        themeToggleButton.setBackground(btnBg);
        themeToggleButton.setForeground(fg);

        for (JButton btn : allButtons) {
            btn.setBackground(btnBg);
            btn.setForeground(fg);
            if (btn.getText().matches("[+\\-*/=()^]")) {
                btn.setBackground(isDarkTheme ? new Color(75, 110, 175) : new Color(173, 216, 230));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        String currentText = inputField.getText();

        if (command.equals("C")) {
            inputField.setText("");
            resultField.setText("");
            resetInputOnNextNumber = false;
        } else if (command.equals("CH")) {
            historyLog.setLength(0);
            historyArea.setText("");
        } else if (command.equals("Back")) {
            if (!currentText.isEmpty() && !currentText.equals("Error") && !currentText.equals("Math Error")) {
                inputField.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else if (command.equals("Copy")) {
            if (!currentText.isEmpty() && !currentText.contains("Error")) {
                StringSelection selection = new StringSelection(currentText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            }
        } else if (command.equals("=")) {
            if (!currentText.isEmpty() && !currentText.contains("Error")) {
                try {
                    double result = eval(currentText);
                    
                    if (Double.isInfinite(result) || Double.isNaN(result)) {
                        inputField.setText("Math Error");
                        resetInputOnNextNumber = true;
                        return;
                    }

                    // Format to remove trailing ".0" if whole number
                    String resultStr = (result == (long) result) ? String.format("%d", (long) result) : String.valueOf(result);
                    
                    resultField.setText(currentText + " =");
                    inputField.setText(resultStr);
                    
                    historyLog.append(currentText).append(" = ").append(resultStr).append("\n");
                    historyArea.setText(historyLog.toString());
                    
                    resetInputOnNextNumber = true; // Next number press starts a new equation
                } catch (Exception ex) {
                    inputField.setText("Syntax Error");
                    resetInputOnNextNumber = true;
                }
            }
        } else {
            // It's a number, decimal, or operator
            if (resetInputOnNextNumber) {
                if (command.matches("[0-9.]")) {
                    inputField.setText(command); // Start fresh if typing a number
                } else {
                    inputField.setText(currentText + command); // Continue equation if pressing an operator
                }
                resetInputOnNextNumber = false;
                resultField.setText("");
            } else {
                if (currentText.equals("Error") || currentText.equals("Math Error") || currentText.equals("Syntax Error")) {
                    inputField.setText(command);
                } else {
                    inputField.setText(currentText + command);
                }
            }
        }
    }

    /**
     * Recursive Descent Parser that evaluates a mathematical string following BODMAS.
     */
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // Addition
                    else if (eat('-')) x -= parseTerm(); // Subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // Multiplication
                    else if (eat('/')) x /= parseFactor(); // Division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // Unary plus
                if (eat('-')) return -parseFactor(); // Unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // Brackets
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // Numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // Orders (Exponentiation)

                return x;
            }
        }.parse();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FixedBasicCalculator());
    }
}
