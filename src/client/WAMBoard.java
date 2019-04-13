package client;

import server.WAM.*;

import java.util.LinkedList;
import java.util.List;

public class WAMBoard {
    // TODO Dade

    private List<Observer<WAMBoard>> observers;

    private int score;

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

    public void close() {
        alertObservers();
    }

    public void holeUp(int holeNo) {
        alertObservers();
    }

    public void updateScore(int[] score) {
        alertObservers();
    }

    public void holeDown(int holeNo) {
        alertObservers();
    }

    public void initializeBoard(int rows, int columns, int players,
                                int playerNo) {
        this.score = 0;
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
    }
}
