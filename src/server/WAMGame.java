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
        int colTotal = wam.getColumn();
        int rowCounter = 0;
        int colCounter = 0;
        int counter = 0;
        while(rowCounter < row){
            counter += colTotal + 1;
            rowCounter ++;
        }
        while(colCounter < col){
            counter += 1;
            colCounter ++;
        }
        return counter;
    }

    public void informPlayers(boolean state, int row, int col){
        if(state){
            for(WAMPlayer p: players){
                p.moleUp(getHoleNumFromRowAndCol(row,col));
            }
        }
        else{
            for(WAMPlayer p: players){
                p.moleDown(getHoleNumFromRowAndCol(row,col));
            }
        }
    }

    public void closeAll(){
        for(WAMPlayer player: players){
            player.gameTied();
            player.close();
        }
    }


    @Override
    public void run() {

        long endTime = System.currentTimeMillis() + gameDuration*1000;

        while (System.currentTimeMillis() < endTime) {
            int[] pos = wam.chooseRandomSlot();
            wam.popOut(pos[0],pos[1]);
            informPlayers(true, pos[0], pos[1]);

            while (System.currentTimeMillis() < wam.getRandomUpTime()*1000) {

                System.out.println("MOLE is UP");
            }

            wam.popIn(pos[0],pos[1]);
            informPlayers(false, pos[0], pos[1]);

            while (System.currentTimeMillis() < wam.getRandomDownTime()) {
                System.out.println("MOLE is Down");
            }
        }
        //For now all the players get a tie.
        System.out.println("Game Over");
        closeAll();
     }
}
