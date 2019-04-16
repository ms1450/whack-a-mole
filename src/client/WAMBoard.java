package client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The WAMBoard is observed by the GUI and tells it when to make changes to
 * the view.
 * @author Dade Wood
 * @author Mehul Sen
 */
public class WAMBoard {
    // TODO Dade

    // The list of each GUI observing the board's changes
    private List<Observer<WAMBoard>> observers;

    // The array of holes indexed by ID
    private Hole[] holes;

    // Number of rows of the board
    private int rows;

    // Number of columns of the board
    private int columns;

    // Number of players playing
    private int players;

    // Which player this currently is
    private int playerNo;

    //The Score of the Player
    private int[] scores;

    /**
     * All the different statuses that the game could be in.
     */
    public enum Status {
        RUNNING, I_WON, I_LOST, TIE, ERROR;

        private String msg = null;

        public void setMsg(String msg){this.msg = msg;}

        @Override
        public String toString(){
            return super.toString() +
                    this.msg == null ? "" : (this.msg);
        }

    }

    /**
     * Each hole is either empty or occupied by a mole.
     */
    public enum Hole {
        EMPTY, OCCUPIED
    }

    // What the status currently is
    private Status status;

    /**
     * Adds a GUI to the observer list.
     * @param observer the observer being added
     */
    public void addObserver(Observer<WAMBoard> observer){
        this.observers.add(observer);
    }

    /**
     * Tells each observer that a change has occured and it needs to update
     * the view.
     */
    private void alertObservers(){
        for (Observer<WAMBoard> observer: this.observers){
            observer.update(this);
        }
    }

    /**
     * An error has occurred and a message is sent.
     * @param arguments The message being sent
     */
    public void error(String arguments) {
        this.status = Status.ERROR;
        this.status.setMsg(arguments);
        alertObservers();
    }

    /**
     * Tells the observers that the game has been won.
     */
    public void gameWon() {
        this.status = Status.I_WON;
        alertObservers();
    }

    /**
     * Tells the observers that the game has been lost.
     */
    public void gameLost() {
        this.status = Status.I_LOST;
        alertObservers();
    }

    /**
     * Tells the observers that the game ended in a tie.
     */
    public void gameTied() {
        this.status = Status.TIE;
        alertObservers();
    }

    /**
     * Tells the observers to end.
     */
    public void close() {
        alertObservers();
    }

    /**
     * Tells the observers that a specific hole is now occupied.
     * @param holeNo The number of the hole that is now occupied
     */
    public void holeUp(int holeNo) {
        holes[holeNo] = Hole.OCCUPIED;
        alertObservers();
    }

    /**
     * Tells the observers that the scores have been changed for the users.
     * @param newScore the new score that changed
     */
    public void updateScore(int[] newScore) {
        this.scores = newScore;
        alertObservers();
    }

    /**
     * Tells the observers that a specific hole is now empty.
     * @param holeNo The number of the hole that is now empty
     */
    public void holeDown(int holeNo) {
        holes[holeNo] = Hole.EMPTY;
        alertObservers();
    }

    /**
     * Initializes the game board once the server sends the WELCOME message.
     * @param rows number of rows for the board
     * @param columns number of columns for the board
     * @param players number of players currently playing the game
     * @param playerNo number of the player for this instance of the board
     */
    public void initializeBoard(int rows, int columns, int players,
                                int playerNo) {
        this.scores = new int[players];
        for (int score: scores) {
            score = 0;
        }
        this.rows = rows;
        this.columns = columns;
        this.playerNo = playerNo;
        this.players = players;
        this.holes = new Hole[columns*rows];
        for (int i = 0; i < holes.length; i++){
            holes[i] = Hole.EMPTY;
        }
    }

    /**
     * Returns if a hole is OCCUPIED or EMPTY.
     * @param index the holeNo
     * @return EMPTY of the hole is empty or OCCUPIED if the hole is occupied
     */
    public Hole getContents(int index){
        //System.out.println(index + " " + holes[index]);
        return this.holes[index];
    }

    /**
     * Gets the number of rows the board has.
     * @return integer of rows
     */
    public int getRows(){return this.rows;}

    /**
     * Gets the number of columns the board has.
     * @return integer of the columns
     */
    public int getColumns(){return this.columns;}

    public int[] getScores(){return this.scores;}

    public Status getStatus(){return this.status;}

    public int getPlayerNo(){return this.playerNo;}


    /**
     * Constructor for the WAMBoard. Sets the status to RUNNING and
     * initializes the observers list.
     */
    public WAMBoard(){
        this.observers = new LinkedList<>();

        this.status = Status.RUNNING;
    }
}
