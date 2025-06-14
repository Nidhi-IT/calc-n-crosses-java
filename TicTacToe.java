import java.util.Scanner;

public class TicTacToe {
    static char[][] board = new char[3][3];
    static char currentPlayer = 'X';

    static int xWins = 0;
    static int oWins = 0;
    static int draws = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;

        System.out.println("Welcome to Tic-Tac-Toe!");

        while (playAgain) {
            resetBoard();
            boolean gameEnded = false;
            currentPlayer = 'X';
            printBoard();

            while (!gameEnded) {
                int row = -1, col = -1;
                boolean validInput = false;

                while (!validInput) {
                    System.out.print("Player " + currentPlayer + ", enter your move (row and column: 1 1 for top-left): ");
                    if (scanner.hasNextInt()) {
                        row = scanner.nextInt() - 1;
                    } else {
                        System.out.println("Invalid input. Please enter numbers.");
                        scanner.next(); // discard invalid input
                        continue;
                    }

                    if (scanner.hasNextInt()) {
                        col = scanner.nextInt() - 1;
                    } else {
                        System.out.println("Invalid input. Please enter numbers.");
                        scanner.next(); // discard invalid input
                        continue;
                    }

                    if (isValidMove(row, col)) {
                        board[row][col] = currentPlayer;
                        validInput = true;
                    } else {
                        System.out.println("Invalid move. Try again.");
                    }
                }

                printBoard();

                if (checkWin()) {
                    System.out.println("Player " + currentPlayer + " wins!");
                    if (currentPlayer == 'X') xWins++;
                    else oWins++;
                    gameEnded = true;
                } else if (isBoardFull()) {
                    System.out.println("It's a draw!");
                    draws++;
                    gameEnded = true;
                } else {
                    switchPlayer();
                }
            }

            // Show score
            System.out.println("\nScoreboard:");
            System.out.println("Player X Wins: " + xWins);
            System.out.println("Player O Wins: " + oWins);
            System.out.println("Draws: " + draws);

            System.out.print("\nDo you want to play again? (yes/no): ");
            String response = scanner.next().toLowerCase();
            if (!response.equals("yes")) {
                playAgain = false;
            }
        }

        System.out.println("Thanks for playing!");
        scanner.close();
    }

    // Reset the board for a new game
    static void resetBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = ' ';
    }

    // Print the current state of the board
    static void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println("\n-------------");
        }
    }

    // Check if a move is valid
    static boolean isValidMove(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == ' ';
    }

    // Switch turns between players
    static void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    // Check for a win
    static boolean checkWin() {
        // Rows and columns
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == currentPlayer &&
                 board[i][1] == currentPlayer &&
                 board[i][2] == currentPlayer) ||
                (board[0][i] == currentPlayer &&
                 board[1][i] == currentPlayer &&
                 board[2][i] == currentPlayer)) {
                return true;
            }
        }

        // Diagonals
        return (board[0][0] == currentPlayer &&
                board[1][1] == currentPlayer &&
                board[2][2] == currentPlayer) ||
               (board[0][2] == currentPlayer &&
                board[1][1] == currentPlayer &&
                board[2][0] == currentPlayer);
    }

    // Check if the board is full
    static boolean isBoardFull() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == ' ') return false;
        return true;
    }
}
