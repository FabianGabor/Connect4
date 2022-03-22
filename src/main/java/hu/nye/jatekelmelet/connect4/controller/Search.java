package hu.nye.jatekelmelet.connect4.controller;

import hu.nye.jatekelmelet.connect4.model.Board;
import hu.nye.jatekelmelet.connect4.model.Player;

import java.util.Arrays;
import java.util.Scanner;

public class Search {

    private static final int MAX_DEPTH = 2;

    public static int humanMove(Board board, Player p) {

        Scanner scan = new Scanner(System.in);
        int move;

        System.out.println(board);

        do {
            System.out.println(p.getName() + " enter a column number (0-" + (board.getColumnSize() - 1) + "): ");
            move = scan.nextInt();
        } while (move < 0 || board.getColumnSize() - 1 < move);

        return move;
    }

    public static boolean isWinningMove(Board board, Player player, Player opponent, int col) {

        board.addSymbol(col, player.getSymbol());
        String win = board.getConnect4(player, opponent);
        boolean isWin = win.equals(player.getSymbol());

        board.removeSymbol(col);

        return isWin;
    }

    public static int getHeuristic(Board board, Player player, Player opponent) {

        int w4 = 10;
        int w3 = 5;
        int w2 = 3;
        int w1 = 1;

        int player4 = board.getNumStraights(player.getSymbol(), 4);
        int player3 = board.getNumStraights(player.getSymbol(), 3);
        int player2 = board.getNumStraights(player.getSymbol(), 2);
        int player1 = board.getNumStraights(player.getSymbol(), 1);
        int opponent4 = board.getNumStraights(opponent.getSymbol(), 4);
        int opponent3 = board.getNumStraights(opponent.getSymbol(), 3);
        int opponent2 = board.getNumStraights(opponent.getSymbol(), 2);
        int opponent1 = board.getNumStraights(opponent.getSymbol(), 1);

        int playerHeuristic = (player4 * w4) + (player3 * w3) + (player2 * w2) + (player1 * w1);
        int opponentHeuristic = (opponent4 * w4) + (opponent3 * w3) + (opponent2 * w2) + (opponent1 * w1);

        return playerHeuristic - opponentHeuristic;
    }

    public static int getSimpleHeuristic(Board board, Player player, Player opponent) {

        int w3 = 5;
        int w2 = 3;

        int player3 = board.getNumStraights(player.getSymbol(), 3);
        int player2 = board.getNumStraights(player.getSymbol(), 2);
        int opponent3 = board.getNumStraights(opponent.getSymbol(), 3);
        int opponent2 = board.getNumStraights(opponent.getSymbol(), 2);

        int playerHeuristic = (player3 * w3) + (player2 * w2);
        int opponentHeuristic = (opponent3 * w3) + (opponent2 * w2);

        return playerHeuristic - opponentHeuristic;
    }


    public static int nextBestMove(Board board, Player player, Player opponent) {

        int[] moves = board.getAvailableColumns();

        int minHeuristic = -1000;
        int highestCol = moves[0];
        for (int col : moves) {
            board.addSymbol(col, player.getSymbol());
            int currHeuristic = getHeuristic(board, player, opponent);
            board.removeSymbol(col);

            if (currHeuristic > minHeuristic) {
                highestCol = col;
                minHeuristic = currHeuristic;
            }
        }

        return highestCol;
    }


    // TODO depth
    public static int minimax(Board board, Player player, Player opponent) {

        int[] firstMoves = board.getAvailableColumns();
        int[] heuristics = new int[firstMoves.length];


        for (int col : firstMoves) {
            if (isWinningMove(board, player, opponent, col)) {
                return col;
            }
        }

        final boolean RECURSIVE = true;

        if (RECURSIVE) {
            for (int i = 0; i < firstMoves.length; i++) {
                int firstCol = firstMoves[i];
                board.addSymbol(firstCol, player.getSymbol());
                int[] secondMoves = board.getAvailableColumns();
                heuristics[i] = calculateHeuristics(board, player, opponent, secondMoves, heuristics[i], 1, i);
                board.removeSymbol(firstCol);
            }
        } else {
            for (int i = 0; i < firstMoves.length; i++) {
                int firstCol = firstMoves[i];
                board.addSymbol(firstCol, player.getSymbol());
                int[] secondMoves = board.getAvailableColumns();

                for (int j = 0; j < secondMoves.length; j++) {
                    int secondCol = secondMoves[j];
                    board.addSymbol(secondCol, opponent.getSymbol());
                    int[] thirdMoves = board.getAvailableColumns();

                    for (int k = 0; k < thirdMoves.length; k++) {
                        int thirdCol = thirdMoves[k];
                        board.addSymbol(thirdCol, player.getSymbol());
                        heuristics[i] += getSimpleHeuristic(board, player, opponent);
                        board.removeSymbol(thirdCol);
                    }
                    board.removeSymbol(secondCol);
                }
                board.removeSymbol(firstCol);
            }
        }

        System.out.println("Heuristics: " + Arrays.toString(heuristics));

        int maxIndex = 0;
        int maxHeuristic = heuristics[0];
        for (int i = 0; i < heuristics.length; i++) {
            if (heuristics[i] > maxHeuristic) {
                maxIndex = i;
                maxHeuristic = heuristics[i];
            }
        }

        return firstMoves[maxIndex];
    }


    public static int calculateHeuristics(Board board, Player player, Player opponent, int[] moves, int heuristicValue,
                                           int depth, int index) {

        for (int i = 0; i < moves.length; i++) {
            int col = moves[index];

            if (depth % 2 == 0) {
                board.addSymbol(col, player.getSymbol());
            } else {
                board.addSymbol(col, opponent.getSymbol());
            }

            if (depth < MAX_DEPTH) {
                int[] nextMoves = board.getAvailableColumns();
                calculateHeuristics(board, player, opponent, nextMoves, heuristicValue, depth + 1, index);
            } else {
                heuristicValue += getSimpleHeuristic(board, player, opponent);
            }

            board.removeSymbol(col);
        }
        return heuristicValue;
    }

}
