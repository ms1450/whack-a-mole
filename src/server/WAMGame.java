package server;

import java.io.IOException;

/**
 * The actual class where the Game runs.
 */
public class WAMGame implements Runnable {
    private WAMPlayer[] players;
    private WAM wam;
    private int gameDuration;

    public WAMGame(int row, int column, WAMPlayer[] players, int gameDuration){
        this.players = players;
        this.wam = new WAM(row, column);
        this.gameDuration = gameDuration;
    }

    public int getHoleNumFromRowAndCol(int row, int col){
        int rowTotal = wam.getRow();
        int colTotal = wam.getColumn()
        int rowCounter = 0;
        int colCounter = 0;
        int counter = 0;
        while(rowCounter < row){
            counter += colTotal + 1;
            row ++;
        }
        while(colCounter < col){
            counter += 1;
            col ++;
        }
        return counter;
    }

    public void informPlayers(boolean state, int row, int col){
        if(state){
            for(WAMPlayer p: players){
                //p.moleUp(row, col);
            }
        }

    }

    @Override
    public void run() {
        long endTime = System.currentTimeMillis() + gameDuration*1000;
        while (System.currentTimeMillis() < endTime) {
            int[] pos = wam.chooseRandomSlot();
            wam.popOut(pos[0],pos[1]);
            //informPlayers(true);
            while (System.currentTimeMillis() < wam.getRandomUpTime()*1000) {

                System.out.println("MOLE is UP");
            }
            wam.popIn(pos[0],pos[1]);
            while (System.currentTimeMillis() < wam.getRandomDownTime()) {
                System.out.println("MOLE is Down");
            }
        }
     }
}
