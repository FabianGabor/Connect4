package hu.nye.jatekelmelet.connect4.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Board {
    private final int rowSize;
    private final int columnSize;

    private String[][] board;
    private int[] heights;

    public Board(){
        this(6, 7);
    }

    public Board(int rowSize, int columnSize){
//        if(rowSize < 6) throw new IllegalArgumentException("Invalid rowSize parameter");
//        if(columnSize < 7) throw new IllegalArgumentException("Invalid columnSize parameter");

        this.rowSize = rowSize;
        this.columnSize = columnSize;

        board = new String[this.rowSize][this.columnSize];
        heights = new int[this.columnSize];

        initBoard();
    }

    private void initBoard(){
        for(int i = 0; i < rowSize; i++){
            for(int j = 0; j < columnSize; j++){
                board[i][j] = Color.EMPTY.getValue();
            }
        }

        for(int i = 0; i < columnSize; i++){
            heights[i] = 0;
        }
    }

    public String getPosition(int row, int column){
        if(row < 0 || row > rowSize - 1) throw new IllegalArgumentException("Invalid row position");
        if(column < 0 || column > columnSize - 1) throw new IllegalArgumentException("Invalid column position");
        return board[row][column];
    }

    public int getNumMoves(){
        int moves = 0;
        for (int height : heights) {
            moves += height;
        }
        return moves;
    }

    public int[] getAvailableColumns(){
        int[] available;
        int size = 0;
        for(int i = 0; i < columnSize; i++){
            if(!isFull(i)){
                size++;
            }
        }
        available = new int[size];

        int j = 0;
        for(int i = 0; i < columnSize; i++){
            if(!isFull(i)){
                available[j] = i;
                j++;
            }
        }
        return available;
    }

    public boolean isFull(int column){
        if(column < 0 || column > columnSize - 1) throw new IllegalArgumentException("Invalid column position");
        return heights[column] == rowSize;
    }

    public boolean isBoardFull(){
        boolean full = true;
        for(int i = 0; i < columnSize; i++){
            if(!isFull(i)){
                full = false;
                break;
            }
        }
        return full;
    }

    public void addSymbol(int column, String symbol){
        if(isFull(column)) throw new IllegalArgumentException("Cannot add a symbol to a full column");
        if(column < 0 || column > columnSize - 1) throw new IllegalArgumentException("Invalid column position");

        for(int i = rowSize-1; i >= 0; i--){
            if(board[i][column].equals(Color.EMPTY.getValue())){
                board[i][column] = symbol;
                heights[column]++;
                return;
            }
        }
    }

    public void removeSymbol(int column){
        if(column < 0 || column > columnSize - 1) throw new IllegalArgumentException("Invalid column position");
        if(heights[column] == 0) throw new IllegalArgumentException("Cannot Remove a Symbol from an empty column");

        for(int i = 0; i < rowSize; i++){
            if(!board[i][column].equals(Color.EMPTY.getValue())){
                board[i][column] = Color.EMPTY.getValue();
                heights[column]--;
                return;
            }
        }
    }

    public String getConnect4(Player p1, Player p2){

        StringBuilder s = new StringBuilder();

        StringBuilder p1Sequence = new StringBuilder();
        StringBuilder p2Sequence = new StringBuilder();
        for(int i = 0; i < 4; i++){
            p1Sequence.append(p1.getSymbol());
            p2Sequence.append(p2.getSymbol());
        }

        // Check a Connect 4 in all rows
        for(int i = 0; i < rowSize; i++){
            s = new StringBuilder(getRow(i));

            if(s.toString().contains(p1Sequence.toString())){
                return p1.getSymbol();
            }

            if(s.toString().contains(p2Sequence.toString())){
                return p2.getSymbol();
            }

            s = new StringBuilder();
        }

        // Check a Connect 4 in all columns
        for(int i = 0; i < columnSize; i++){
            s = new StringBuilder(getColumn(i));

            if(s.toString().contains(p1Sequence.toString())){
                return p1.getSymbol();
            }

            if(s.toString().contains(p2Sequence.toString())){
                return p2.getSymbol();
            }

            s = new StringBuilder();
        }

        // Check a Connect 4 in all top-left to down-right straights
        for(int i = 0; i < rowSize-3; i++){
            for(int j = 0; j < columnSize-3; j++){
                for(int k = 0; k < 4; k++){
                    s.append(getPosition(i + k, j + k));
                }

                if(s.toString().contains(p1Sequence.toString())){
                    return p1.getSymbol();
                }

                if(s.toString().contains(p2Sequence.toString())){
                    return p2.getSymbol();
                }

                s = new StringBuilder();
            }
        }

        // Check a Connect 4 in all bottom-left to top-right straights
        for(int i = rowSize-1; i >= rowSize-3; i--){
            for(int j = 0; j < columnSize-3; j++){
                for(int k = 0; k < 4; k++){
                    s.append(getPosition(i - k, j + k));
                }

                if(s.toString().contains(p1Sequence.toString())){
                    return p1.getSymbol();
                }

                if(s.toString().contains(p2Sequence.toString())){
                    return p2.getSymbol();
                }

                s = new StringBuilder();
            }
        }

        return s.toString();
    }

    public int getNumStraights(String symbol, int length){

        StringBuilder s = new StringBuilder();
        int count = 0;

        List<String> combos = new ArrayList<>();
        switch(length){
            case 1:
                combos.add(Color.EMPTY.getValue() + symbol + Color.EMPTY.getValue());
                break;
            case 2:
                combos.add(Color.EMPTY.getValue() + symbol + symbol + Color.EMPTY.getValue());
                combos.add(Color.EMPTY.getValue() + symbol + symbol);
                combos.add(symbol + Color.EMPTY.getValue() + symbol);
                combos.add(symbol + symbol + Color.EMPTY.getValue());
                break;
            case 3:
                combos.add(Color.EMPTY.getValue() + symbol + symbol + symbol + Color.EMPTY.getValue());
                combos.add(Color.EMPTY.getValue() + symbol + symbol + symbol);
                combos.add(symbol + Color.EMPTY.getValue() + symbol + symbol);
                combos.add(symbol + symbol + Color.EMPTY.getValue() + symbol);
                combos.add(symbol + symbol + symbol + Color.EMPTY.getValue());
                break;
            case 4:
                combos.add(symbol + symbol + symbol + symbol);
                break;
            default:
                throw new IllegalArgumentException("Invalid length");
        }

        // Check a Connect 4 in all rows
        for(int i = 0; i < rowSize; i++){
            s = new StringBuilder(getRow(i));

            for (String combo : combos) {
                if (s.toString().contains(combo)) {
                    count++;
                }
            }

            s = new StringBuilder();
        }

        // Check a Connect 4 in all columns
        for(int i = 0; i < columnSize; i++){
            s = new StringBuilder(getColumn(i));

            for (String combo : combos) {
                if (s.toString().contains(combo)) {
                    count++;
                }
            }

            s = new StringBuilder();
        }

        // Check a Connect 4 in all top-left to down-right straights
        for(int i = 0; i < rowSize-3; i++){
            for(int j = 0; j < columnSize-3; j++){
                for(int k = 0; k < 4; k++){
                    s.append(getPosition(i + k, j + k));
                }

                for (String combo : combos) {
                    if (s.toString().contains(combo)) {
                        count++;
                    }
                }

                s = new StringBuilder();
            }
        }

        // Check a Connect 4 in all bottom-left to top-right straights
        for(int i = rowSize-1; i >= rowSize-3; i--){
            for(int j = 0; j < columnSize-3; j++){
                for(int k = 0; k < 4; k++){
                    s.append(getPosition(i - k, j + k));
                }

                for (String combo : combos) {
                    if (s.toString().contains(combo)) {
                        count++;
                    }
                }

                s = new StringBuilder();
            }
        }

        return count;
    }

    public String getRow(int row){

        if(row < 0 || row >= rowSize) throw new IllegalArgumentException("Invalid getRow index");

        StringBuilder s = new StringBuilder();
        for(int i = 0; i < columnSize; i++){
            s.append(getPosition(row, i));
        }

        return s.toString();
    }

    public String getColumn(int col){

        if(col < 0 || col >= columnSize) throw new IllegalArgumentException("Invalid getColumn index");

        StringBuilder s = new StringBuilder();
        for(int i = 0; i < rowSize; i++){
            s.append(getPosition(i, col));
        }

        return s.toString();
    }

    public String toString(){
        StringBuilder s = new StringBuilder();

        // Add board symbols
        for(int i = 0; i < rowSize; i++){
            for(int j = 0; j < columnSize; j++){
                s.append(board[i][j]).append(" ");
            }
            s.append('\n');
        }

        s.append("Heights: [");
        for(int i = 0; i < columnSize; i++){
            s.append(heights[i]).append(" ");
        }
        s.append("]\n");

        return s.toString();
    }
}
