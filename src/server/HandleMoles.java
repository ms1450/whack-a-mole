package server;

import common.WAMProtocol;

public class HandleMoles implements Runnable {
    private WAM wam;
    private int holeNo;
    private WAMPlayer[] players;

    public HandleMoles(WAM wam, int holeNo, WAMPlayer[] players){
        this.wam = wam;
        this.holeNo = holeNo;
    }

    public void informPlayers(boolean state){
        for(WAMPlayer player: this.players){
            if(state){
                player.moleUp(holeNo);
            }
            else{
                player.moleDown(holeNo);
            }
        }
    }

    private int[] holeNoToRowAndColumn(int hole){
        int rowNum = 0;
        int colNum = 0;
        int maxRow = wam.row;
        while(hole>maxRow){
            hole -= maxRow+1;
            rowNum ++;
        }
        colNum = hole;
        return new int[]{rowNum,colNum};
    }

    @Override
    public void run() {
        try {
            Thread.sleep(wam.getRandomDownTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int[] pos = holeNoToRowAndColumn(holeNo);
        wam.popOut(pos[0],pos[1]);
        informPlayers(true);
        try {
            Thread.sleep(wam.getRandomUpTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wam.popIn(pos[0],pos[1]);
        informPlayers(false);
    }
}
