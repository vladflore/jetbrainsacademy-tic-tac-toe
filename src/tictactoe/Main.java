package tictactoe;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char[][] field = new char[3][3];
        initField(field);
        printField(field);

        boolean turn = true; //means X

        FieldState fieldState;
        String coordinates;
        int[] coords;

        do {
            do {
                System.out.print("Enter the coordinates: ");
                coordinates = scanner.nextLine();
                coords = validateAndReturnRealCoordinates(coordinates, field);
            } while (coords.length == 0);
            if (turn) {
                field[coords[0]][coords[1]] = 'X';
                turn = false;
            } else {
                field[coords[0]][coords[1]] = 'O';
                turn = true;
            }
            printField(field);
            fieldState = analyzeField(field);
        } while (fieldState != FieldState.DRAW && fieldState != FieldState.X_WINS && fieldState != FieldState.O_WINS);

        scanner.close();
    }

    private static void initField(char[][] field) {
        for (char[] chars : field) {
            Arrays.fill(chars, '_');
        }
    }

    private static int[] validateAndReturnRealCoordinates(String coordinates, char[][] field) {
        int[] realCoordinates = new int[2];
        if (!coordinates.matches("\\d\\s\\d")) {
            System.out.println("You should enter numbers!");
            return new int[0];
        }

        int c1 = Integer.parseInt(coordinates.split(" ")[0]);
        int c2 = Integer.parseInt(coordinates.split(" ")[1]);
        if (c1 < 1 || c1 > 3 || c2 < 1 || c2 > 3) {
            System.out.println("Coordinates should be from 1 to 3!");
            return new int[0];
        }

        int realC1 = c2 - 1;
        if (c2 == 1) {
            realC1 += 2;
        }
        if (c2 == 3) {
            realC1 -= 2;
        }

        int realC2 = c1 - 1;

        if (field[realC1][realC2] != '_') {
            System.out.println("This cell is occupied! Choose another one!");
            return new int[0];
        }

        realCoordinates[0] = realC1;
        realCoordinates[1] = realC2;

        return realCoordinates;
    }

    private static FieldState analyzeField(char[][] field) {
        boolean xWins = false;
        boolean oWins = false;
        boolean hasEmptyCell = false;
        int countX = 0;
        int countO = 0;
        char[] horizontal = new char[field.length];
        char[] vertical = new char[field.length];
        char[] diagonal1 = new char[field.length];
        char[] diagonal2 = new char[field.length];
        FieldState fieldState = null;

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {

                if (field[i][j] == 'X') {
                    countX++;
                }

                if (field[i][j] == 'O') {
                    countO++;
                }

                if (field[i][j] == '_') {
                    hasEmptyCell = true;
                }

                horizontal[j] = field[i][j];
                vertical[j] = field[j][i];
                if (i == j) {
                    diagonal1[j] = field[i][j];
                }
                if (i + j == field.length - 1) {
                    diagonal2[i] = field[i][j];
                }

                if (new String(horizontal).equals("XXX") ||
                        new String(vertical).equals("XXX") ||
                        new String(diagonal1).equals("XXX") ||
                        new String(diagonal2).equals("XXX")) {
                    xWins = true;
                }

                if (new String(horizontal).equals("OOO") ||
                        new String(vertical).equals("OOO") ||
                        new String(diagonal1).equals("OOO") ||
                        new String(diagonal2).equals("OOO")) {
                    oWins = true;
                }
            }
            horizontal = new char[field.length];
            vertical = new char[field.length];
        }
        if ((xWins && oWins) || Math.abs(countO - countX) > 1) {
            System.out.println("Impossible");
            fieldState = FieldState.IMPOSSIBLE;
        } else {
            if (xWins) {
                System.out.println("X wins");
                fieldState = FieldState.X_WINS;
            }
            if (oWins) {
                System.out.println("O wins");
                fieldState = FieldState.O_WINS;
            }
            if (!xWins && !oWins && !hasEmptyCell) {
                System.out.println("Draw");
                fieldState = FieldState.DRAW;
            }
            if (!xWins && !oWins && hasEmptyCell) {
                // System.out.println("Game not finished");
                fieldState = FieldState.GAME_NOT_FINISHED;
            }
        }
        return fieldState;
    }

    private static void printField(char[][] field) {
        System.out.println("---------");
        for (char[] chars : field) {
            System.out.print("|");
            for (char aChar : chars) {
                System.out.print(" " + aChar);
            }
            System.out.println(" |");
        }
        System.out.println("---------");
    }
}

enum FieldState {
    IMPOSSIBLE,
    X_WINS,
    O_WINS,
    DRAW,
    GAME_NOT_FINISHED
}
