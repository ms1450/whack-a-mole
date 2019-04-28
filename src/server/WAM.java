package server;

import java.util.Random;

/**
 * This will act as the instance of the board.
 * It will be run by the WAMGame class.
 * @author Mehul Sen
 * @author Dade Wood
 */
public class WAM {
    //The board with its rows and columns
    private Hole[] board;
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
        board = new Hole[ROWS * COLUMNS];
        for(int i = 0; i < board.length; i++) {
            board[i] = Hole.EMPTY;
        }
    }

    /**
     * Pops out a mole on the Board     *
     */
    public void moleUp(int holeNo){
        board[holeNo] = Hole.OCCUPIED;
    }

    /**
     * Downs a mole on the Board     *
     */
    public void moleDown(int holeNo){
        board[holeNo] = Hole.EMPTY;
    }

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
     *
     * @return true if it is occupied and false if not.
     */
    public boolean checkIfMole(int holeNo){
        return board[holeNo].equals(Hole.OCCUPIED);
    }

    /**
     * Returns a random value between 5000 and 3000
     * @return integer
     */
    public int getRandomUpTime(){
        Random r = new Random();
        return (r.nextInt(3) + 3)*1000 ;
    }

    /**
     * Returns a random value between 10000 and 2000
     * @return integer
     */
    public int getRandomDownTime(){
        Random r = new Random();
        System.out.println((r.nextInt(9) + 2)*1000);
        return (r.nextInt(9) + 2)*1000;
    }

}
