package client;

import server.WAM.*;

import java.util.LinkedList;
import java.util.List;

public class WAMBoard {
    // TODO Dade

    private List<Observer<WAMBoard>> observers;

    private int[] scores;

    private Hole[][] holes;

    private int rows;
    private int columns;
    private int players;
    private int playerNo;

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

    private Status status;

    public void addObserver(Observer<WAMBoard> observer){
        this.observers.add(observer);
    }

    private void alertObservers(){
        for (Observer<WAMBoard> observer: this.observers){
            observer.update(this);
        }
    }

    public void error(String arguments) {
        this.status = Status.ERROR;
        this.status.setMsg(arguments);
        alertObservers();
    }

    public void gameWon() {
        this.status = Status.I_WON;
        alertObservers();
    }

    public void gameLost() {
        this.status = Status.I_LOST;
        alertObservers();
    }

    public void gameTied() {
        this.status = Status.TIE;
        alertObservers();
    }

    private int[] holeNoToRowAndColumn(int hole){
        int rowNum = 0;
        int colNum = 0;
        int maxRow = 3;
        while(hole>maxRow){
            hole -= maxRow+1;
            rowNum ++;
        }
        colNum = hole;
        return new int[]{rowNum,colNum};
    }

    public void close() {
        alertObservers();
    }

    public void holeUp(int holeNo) {
        int[] pos = holeNoToRowAndColumn(holeNo);
        holes[pos[0]][pos[1]] = Hole.OCCUPIED;
        alertObservers();
    }

    public void updateScore(int[] newScore) {
        scores = newScore;
        alertObservers();
    }

    public void holeDown(int holeNo) {
        int[] pos = holeNoToRowAndColumn(holeNo);
        holes[pos[0]][pos[1]] = Hole.EMPTY;
        alertObservers();
    }

    public void initializeBoard(int rows, int columns, int players,
                                int playerNo) {
        this.scores = new int[players];
        for (int score: scores){
            score = 0;
        }
        this.rows = rows;
        this.columns = columns;
        this.playerNo = playerNo;
        this.players = players;
        alertObservers();
    }

    public int getRows(){return this.rows;}

    public int getColumns(){return this.columns;}

    public WAMBoard(){
        this.observers = new LinkedList<>();

        this.holes = new Hole[columns][rows];
        for (int col = 0; col < columns; col++){
            for (int row = 0; row < rows; row++){
                holes[col][row] = Hole.EMPTY;
            }
        }
        this.status = Status.RUNNING;
    }
}
