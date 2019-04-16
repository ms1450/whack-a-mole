package server;

import java.util.Random;

/**
 * This will act as the instance of the board.
 * It will be run by the WAMGame class.
 */
public class WAM {
    //The board with its rows and columns
    private Hole[][] board;
    public int row;
    public int column;

    //Init for the Board
    public WAM(int row, int column){
        this.row = row;
        this.column = column;
        board = new Hole[row][column];
        for(int col=0; col<column; col++) {
            for(int r=0; r < row; row++) {
                board[col][r] = Hole.EMPTY;
            }
        }
    }

    //WAMGame will send this to the instance to pop out a Mole.
    public void popOut(int row, int col){
        board[row][col] = Hole.OCCUPIED;
    }

    //WAMGame will send this to the instance to pop in a Mole.
    public void popIn(int row, int col){
        board[row][col] = Hole.EMPTY;
    }

    //WAMGame will send this to the instance when the mole gets whacked and once whacked the mole will go back.
//    public int whack(int row, int col){
//        if(board[row][col].equals(Hole.OCCUPIED)){
//            board[row][col] = Hole.EMPTY;
//            return 2;
//        }
//        else{
//            return -1;
//        }
//    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }


    //ENUM to show two states of the hole, occupied and empty.
    public enum Hole{
        EMPTY('O'),
        OCCUPIED('X');
        private char symbol;
        Hole(char symbol) {
            this.symbol = symbol;
        }
        public char getSymbol() {
            return symbol;
        }
    }

    public boolean checkIfMole(int row, int column){
        return board[row][column].equals(Hole.OCCUPIED);
    }

    public int getRandomUpTime(){
        Random r = new Random();
        return r.nextInt(((5 - 3) + 1) + 3)*1000;
    }

    public int getRandomDownTime(){
        Random r = new Random();
        return r.nextInt(((8 - 2)+ 1)+2)*1000;
    }

}
