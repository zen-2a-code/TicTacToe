import javax.swing.*;
import java.util.*;

public class TicTacToe {
    private Player player1;
    private Player player2;

    private final Scanner scanner = new Scanner(System.in);
    private boolean gameIsRunning = true;
    private String[][] gameBoardArr = new String[3][3];
    private int turnsCounter = 0;

    private void printSymbolsLine(String[] arr) {
        String firstSymbol = (arr[0] == null) ? " " : arr[0];
        String secondSymbol = (arr[1] == null) ? " " : arr[1];
        String thirdSymbol = (arr[2] == null) ? " " : arr[2];

        System.out.printf(" %s | %s | %s \n", firstSymbol, secondSymbol, thirdSymbol);
    }

    private void printSeparatorLine() {
        System.out.println(" - - - - - ");
    }

    private void setPlayersNames() {

        System.out.print("Player 1 please enter your name: ");
        String player1Name = this.scanner.nextLine();

        System.out.print("Please select your character (should be only 1 symbol e.g - X; 0: ");
        String player1Symbol = this.scanner.nextLine();

        if (player1Symbol.length() > 1) {
            throw new IllegalArgumentException("Invalid input: The character should be only 1 character");
        }

        this.player1 = new Player(player1Name, player1Symbol.trim(), true);

        System.out.print("Player 2 please enter your name: ");
        String player2Name = this.scanner.nextLine();

        System.out.print("Please select your character (should be only 1 symbol e.g - X; 0: ");
        String player2Symbol = this.scanner.nextLine();
        checkStringLength(player2Symbol, 1);
        this.player2 = new Player(player2Name, player2Symbol.trim(), true);

        if (Player.arePlayerCharactersMatching(player1, player2)) {
            throw new InputMismatchException("Both players have the same character. The game is closing.");
        }

        if (Player.arePlayerNamesMatching(player1, player2)) {
            throw new InputMismatchException("Both players have the same name. Make sure to include at least one" +
                    " different character to distinguish between them. The game is closing.");
        }
        System.out.println();
    }

    private void printBoard() {
        int gameBoardArrayLength = this.gameBoardArr.length;

        for (int i = 0; i < gameBoardArrayLength; i++) {
            printSymbolsLine(this.gameBoardArr[i]);

            if (i != gameBoardArrayLength - 1) {
                printSeparatorLine();
            }
        }
    }

    private void printInstructions(Player currentPlayer) {
        System.out.println("It is " + currentPlayer.getPlayersName() + " turn");
        System.out.println("Please enter the row THEN the column, each for 0, 1 or 2, separated by single space");
    }

    public TicTacToe() {
        setPlayersNames();
    }

    private void makeAMove(Player currentPlayer) {
        int[] playerInput = promptPlayerForValidMove();
        int row = playerInput[0];
        int column = playerInput[1];

        while (!isMoveAvaliable(row, column)) {
            System.out.println("That cell is occupied!");
            System.out.println("Please enter the row THEN the column, each for 0, 1 or 2, separated by single space");
            playerInput = promptPlayerForValidMove();

            row = playerInput[0];
            column = playerInput[1];
        }

        this.turnsCounter++;
        // using the symbol from the player array to add it to game board
        this.gameBoardArr[row][column] = currentPlayer.getPlayersSymbol();
    }

    private boolean isMoveAvaliable(int row, int column) {
        return this.gameBoardArr[row][column] == null;
    }

    /**
     * This method check if the player had field all cells in one of the rows with their character.
     *
     * @param playerSymbol String e.g 'X'; 'O'
     * @return boolean - true if a whole row is populated with the player's symbol
     */
    private boolean checkForWinnerByRow(String playerSymbol) {
        for (String[] row : this.gameBoardArr) {
            int matchCounter = 0;
            for (String cell : row) {
                if (cell == null || !cell.equals(playerSymbol)) {
                    break; // no need to change the counter as it is declared on each outer for
                } else {
                    matchCounter++;
                }
            } // end inner for
            if (matchCounter == 3) {
                return true;
            }
        } // end for
        return false;
    }

    private boolean checkForWinnerByColumn(String playerSymbol) {
        for (int i = 0; i < 3; i++) {
            int matchWinCounter = 0;
            for (int j = 0; j < 3; j++) {
                String cellValue = this.gameBoardArr[j][i];
                if (cellValue == null || !cellValue.equals(playerSymbol)) {
                    // no need for have temp variable for a winner  because if the whole 'For' cycle
                    // passes without return true the method returns false as no winning column was found
                    break; // no need to change the counter as it is declared on each outer for
                } else {
                    matchWinCounter++;
                }
            } // end inner for
            if (matchWinCounter == 3) {
                return true;
            }
        } // end outer for
        return false;
    }

    private boolean checkForWinnerByHorizontal_UpperLeftBottomRight(String playerSymbol) {
        int matchWinCounter = 0;
        for (int i = 0; i < 3; i++) {

            // the condition is done that way because if we try to compare a null with String we will raise an exception
            // NullPointerException Logical OR shortCuts the chain and never makes the second check
            if (this.gameBoardArr[i][i] == null || !this.gameBoardArr[i][i].equals(playerSymbol)) {
                break;
            } else {
                matchWinCounter++;
            }
        } // end for
        return matchWinCounter == 3;
    }


    private boolean checkForWinnerByHorizontal_UpperRightBottomLeft(String playerSymbol) {
        int matchWinCounter = 0;
        for (int i = 2; i >= 0; i--) {
            if (this.gameBoardArr[2 - i][i] == null || !this.gameBoardArr[2 - i][i].equals(playerSymbol)) {
                break;
            } else {
                matchWinCounter++;
            }
            if (matchWinCounter == 3) {
                return true;
            }
        }
        return false;
    }

    private void endGameCheck(boolean thereIsWinner, String playersName) {
        if (!thereIsWinner && this.turnsCounter == 9) {
            System.out.println("Good game, but no winner.");
            printBoard();
        } else if (thereIsWinner) {
            System.out.println("Good game! " + playersName + " wins");
            printBoard();
        }
        System.out.print("Do you want to try again? (Y/N): ");
        String userInput = this.scanner.nextLine().toUpperCase();

        if (!userInput.equals("Y") && !userInput.equals("N")) {
            throw new InputMismatchException("Wrong input, program is stopping.Thank you for playing");
        }

        switch (userInput) {
            case "Y":
                newGame();
                break;
            case "N":
                this.gameIsRunning = false;
                break;
            default:
                System.out.println("Invalid Input. The game is stopping");
                this.gameIsRunning = false;
                break;
        }
    }


    private void checkForWinnerOrDraw(Player currentPlayer) {

        String playerSymbol = currentPlayer.getPlayersSymbol();

        // Check for winner
        boolean winner = checkForWinnerByRow(playerSymbol);

        if (!winner) winner = checkForWinnerByColumn(playerSymbol);
        if (!winner) winner = checkForWinnerByHorizontal_UpperLeftBottomRight(playerSymbol);
        if (!winner) winner = checkForWinnerByHorizontal_UpperRightBottomLeft(playerSymbol);

        if (winner || this.turnsCounter == 9) {
            endGameCheck(winner, currentPlayer.getPlayersName());
        }
    }

    /**
     * This method is used to validated user Input and raises exception if the length of the string is longer than
     * expected
     *
     * @param str:           String
     * @param expectedLength int
     */
    private void checkStringLength(String str, int expectedLength) {
        if (str.length() > expectedLength) {
            throw new IllegalArgumentException("Invalid input: The character should be only " + expectedLength +
                    " character/s.");
        }
    }

    /**
     * This is recursive method that insures that the player is entering a valid input of an integers and that the
     * number is valid move on the game board
     * number - (0, 1 ,2)
     *
     * @return int[] of the checked input  - At index 0 row, At index 1 column
     */
    private int[] promptPlayerForValidMove() {
        String playerInput = scanner.nextLine().trim();

        // ensuring the player entered valid Integers
        try {
            int row = Integer.parseInt(Character.toString(playerInput.charAt(0)));
            int column = Integer.parseInt(Character.toString(playerInput.charAt(2)));

            if ((row > 2 || row < 0) || (column > 2 || column < 0)) {
                System.out.println("Make sure to input valid numbers e.g '0 1'.");
                return promptPlayerForValidMove();
            }
        } catch (NumberFormatException e) {
            System.out.println("Make sure to input valid numbers e.g '0 1'.");
            return promptPlayerForValidMove();
        }

        // Ensuring the player have entered a valid string no longer than 3 characters.
        // Excluding blank trailing and leading whitespaces.
        if (playerInput.length() != 3) {
            System.out.println("Please enter valid input e.g '0 1'.");
            return promptPlayerForValidMove();
        } else {
            String[] splitInput = playerInput.split(" ");
            int row = Integer.parseInt(splitInput[0]);
            int column = Integer.parseInt(splitInput[1]);
            return new int[]{row, column};
        }
    }

    private void newGame() {
        this.gameBoardArr = new String[3][3];
        this.turnsCounter = 0;
        Player.switchPlayerTurn(player1, player2);
    }

    // playTicTacToe
    public void play() {
        printBoard();

        while (this.gameIsRunning) {
            if (this.player1.getTurnStatus()) {
                printInstructions(this.player1);
                makeAMove(this.player1);
                checkForWinnerOrDraw(this.player1);
                Player.switchPlayerTurn(this.player1, this.player2);
            } else {
                printInstructions(this.player2);
                makeAMove(this.player2);
                checkForWinnerOrDraw(this.player2);
                Player.switchPlayerTurn(this.player1, this.player2);
            }
            if (this.gameIsRunning) {
                printBoard();
            }
        }
    }
}
