package server;

import java.util.Random;

/**
 * This will act as the instance of the board.
 * It will be run by the WAMGame class.
 */
public class WAM {
    //The board with its rows and columns
    private Hole[][] board;
    //rows in the board
    public final int ROWS;
    //columns in the board
    public final int COLUMNS;

    /**
     * Constructor for the WAM
     * @param ROWS rows of the board
     * @param COLUMNS columns of the board
     */
    public WAM(int ROWS, int COLUMNS){
        this.ROWS = ROWS;
        this.COLUMNS = COLUMNS;
        board = new Hole[ROWS][COLUMNS];
        for(int col=0; col<COLUMNS; col++) {
            for(int r=0; r < ROWS; r++) {
                board[col][r] = Hole.EMPTY;
            }
        }
    }

    /**
     * Pops out a mole on the Board
     * @param row row number
     * @param col column number
     */
    public void popOut(int row, int col){
        board[row][col] = Hole.OCCUPIED;
    }

    /**
     * Downs a mole on the Board
     * @param row row number
     * @param col column number
     */
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

    /**
     * Get the Rows
     * @return rows
     */
    public int getRow(){
        return ROWS;
    }

    /**
     * Get the Columns
     * @return columns
     */
    public int getColumn(){
        return COLUMNS;
    }


    /**
     * Represents a single Hole on the Board.
     */
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

    /**
     * Checks if a Board has a Mole up
     * @param row row number
     * @param column column number
     * @return true if it is occupied and false if not.
     */
    public boolean checkIfMole(int row, int column){
        return board[row][column].equals(Hole.OCCUPIED);
    }

    /**
     * Returns a random value between 5000 and 3000
     * @return integer
     */
    public long getRandomUpTime(){
        Random r = new Random();
        return r.nextInt(((5 - 3) + 1) + 3)*1000;
    }

    /**
     * Returns a random value between 8000 and 2000
     * @return integer
     */
    public long getRandomDownTime(){
        Random r = new Random();
        return r.nextInt(((8 - 2)+ 1)+2)*1000;
    }

}
