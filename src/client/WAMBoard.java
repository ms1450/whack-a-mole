package client;

import server.WAM.*;

import java.util.LinkedList;
import java.util.List;

public class WAMBoard {
    // TODO Dade

    private List<Observer<WAMBoard>> observers;

    private int[] scores;

    private Hole[] holes;

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

    public enum Hole {
        EMPTY, OCCUPIED;
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
        holes[holeNo] = Hole.OCCUPIED;
        alertObservers();
    }

    public void updateScore(int[] newScore) {
        scores = newScore;
        alertObservers();
    }

    public void holeDown(int holeNo) {
        holes[holeNo] = Hole.EMPTY;
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
        this.holes = new Hole[columns*rows];
        for (int i = 0; i < holes.length; i++){
            holes[i] = Hole.EMPTY;
        }
    }

    public Hole getContents(int index){
        //System.out.println(index + " " + holes[index]);
        return this.holes[index];
    }

    public int getRows(){return this.rows;}

    public int getColumns(){return this.columns;}

    public WAMBoard(){
        this.observers = new LinkedList<>();

        this.status = Status.RUNNING;
    }
}
