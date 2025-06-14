import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;

public class FixedBasicCalculator extends JFrame implements ActionListener {

    JTextField inputField, resultField;
    JTextArea historyArea;
    JScrollPane historyScroll;
    JButton toggleHistoryButton, themeToggleButton;
    boolean isHistoryVisible = true, isDarkTheme = false;
    StringBuilder historyLog = new StringBuilder();
    java.util.List<JButton> allButtons = new java.util.ArrayList<>();

    double firstOperand = 0;
    String operator = "";
    boolean isNewInput = true;

    public FixedBasicCalculator() {
        setTitle("Basic Calculator");
        setSize(420, 690);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        inputField = new JTextField();
        inputField.setBounds(20, 20, 360, 40);
        inputField.setEditable(false);
        inputField.setFont(new Font("Arial", Font.BOLD, 18));
        add(inputField);

        resultField = new JTextField();
        resultField.setBounds(20, 65, 360, 30);
        resultField.setEditable(false);
        resultField.setFont(new Font("Arial", Font.ITALIC, 14));
        resultField.setForeground(Color.GRAY);
        resultField.setBorder(null);
        add(resultField);

        toggleHistoryButton = new JButton("Hide History");
        toggleHistoryButton.setBounds(20, 100, 170, 30);
        toggleHistoryButton.addActionListener(e -> toggleHistory());
        add(toggleHistoryButton);

        themeToggleButton = new JButton("Dark Theme");
        themeToggleButton.setBounds(210, 100, 170, 30);
        themeToggleButton.addActionListener(e -> toggleTheme());
        add(themeToggleButton);

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        historyScroll = new JScrollPane(historyArea);
        historyScroll.setBounds(20, 140, 360, 100);
        historyScroll.setBorder(BorderFactory.createTitledBorder("History"));
        add(historyScroll);

        String[] buttons = {
            "C", "Back", "", "", "/",
            "7", "8", "9", "*", "-",
            "4", "5", "6", "+", "Copy",
            "1", "2", "3", "CH", "=",
            "0", ".", "", "", ""
        };

        int x = 20, y = 260;
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].equals("")) continue;

            JButton btn = new JButton(buttons[i]);
            btn.setBounds(x, y, 60, 40);
            btn.setFont(new Font("Arial", Font.PLAIN, 16));
            btn.addActionListener(this);
            add(btn);
            allButtons.add(btn);

            x += 70;
            if ((i + 1) % 5 == 0) {
                x = 20;
                y += 50;
            }
        }

        applyTheme();
        setVisible(true);
    }

    private void toggleHistory() {
        isHistoryVisible = !isHistoryVisible;
        historyScroll.setVisible(isHistoryVisible);
        toggleHistoryButton.setText(isHistoryVisible ? "Hide History" : "Show History");
    }

    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        themeToggleButton.setText(isDarkTheme ? "Light Theme" : "Dark Theme");
        applyTheme();
    }

    private void applyTheme() {
        Color bg = isDarkTheme ? Color.DARK_GRAY : Color.WHITE;
        Color fg = isDarkTheme ? Color.WHITE : Color.BLACK;
        Color btnBg = isDarkTheme ? new Color(60, 63, 65) : Color.LIGHT_GRAY;
        Color fieldBg = isDarkTheme ? new Color(40, 40, 40) : Color.WHITE;

        getContentPane().setBackground(bg);
        inputField.setBackground(fieldBg);
        inputField.setForeground(fg);
        resultField.setBackground(bg);
        resultField.setForeground(isDarkTheme ? Color.LIGHT_GRAY : Color.GRAY);
        historyArea.setBackground(fieldBg);
        historyArea.setForeground(fg);
        historyScroll.setBackground(bg);

        toggleHistoryButton.setBackground(btnBg);
        toggleHistoryButton.setForeground(fg);
        themeToggleButton.setBackground(btnBg);
        themeToggleButton.setForeground(fg);

        for (JButton btn : allButtons) {
            btn.setBackground(btnBg);
            btn.setForeground(fg);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.matches("[0-9.]")) {
            if (isNewInput) {
                inputField.setText(command);
                isNewInput = false;
            } else {
                inputField.setText(inputField.getText() + command);
            }
        } else if (command.matches("[+\\-*/]")) {
            if (!inputField.getText().isEmpty()) {
                firstOperand = Double.parseDouble(inputField.getText());
                operator = command;
                isNewInput = true;
            }
        } else if (command.equals("=")) {
            if (!inputField.getText().isEmpty() && !operator.isEmpty()) {
                double secondOperand = Double.parseDouble(inputField.getText());
                double result = 0;

                switch (operator) {
                    case "+": result = firstOperand + secondOperand; break;
                    case "-": result = firstOperand - secondOperand; break;
                    case "*": result = firstOperand * secondOperand; break;
                    case "/": result = (secondOperand != 0) ? firstOperand / secondOperand : 0; break;
                }

                String expression = firstOperand + " " + operator + " " + secondOperand + " = " + result;
                inputField.setText(String.valueOf(result));
                resultField.setText("");
                historyLog.append(expression).append("\n");
                historyArea.setText(historyLog.toString());

                operator = "";
                isNewInput = true;
            }
        } else if (command.equals("C")) {
            inputField.setText("");
            resultField.setText("");
            operator = "";
            firstOperand = 0;
        } else if (command.equals("CH")) {
            historyLog.setLength(0);
            historyArea.setText("");
        } else if (command.equals("Back")) {
            String current = inputField.getText();
            if (!current.isEmpty()) {
                inputField.setText(current.substring(0, current.length() - 1));
            }
        } else if (command.equals("Copy")) {
            StringSelection selection = new StringSelection(inputField.getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        }
    }

    public static void main(String[] args) {
        new FixedBasicCalculator();
    }
}
