package server;

import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;
import common.WAMException;

public class PlayerWHACKChecker extends Thread {
    private WAMPlayer player;
    private WAM wam;
    private WAMGame game;
    private long duration;

    public PlayerWHACKChecker(WAMPlayer player, WAM wam, long duration, WAMGame game){
        this.player = player;
        this.wam = wam;
        this.duration = duration;
        this.game = game;
    }

    private int[] holeNoToRowAndColumn(int hole){
        int rowNum = 0;
        int colNum = 0;
        int maxRow = wam.ROWS;
        while(hole>maxRow){
            hole -= maxRow;
            rowNum ++;
        }
        colNum = hole;
        System.out.println("HOLE NUMBER "+hole);
        System.out.println(rowNum +" "+ colNum);
        return new int[]{0,0};
    }

    public synchronized void correct() throws WAMException {
        player.scoreUp();
        int holeNo = player.whack();
        game.informPlayers(false, holeNo);
    }

    public void run(){
        long end = System.currentTimeMillis() + duration;
        while (System.currentTimeMillis() < end){
            try {
                if(player.hasWhack()){
                    int holeNo = player.whack();
                    if(wam.checkIfMole(holeNo)){
                        correct();
                    }
                    else {
                        player.scoreDown();
                    }
                }
                else{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    player.scores(game.getPlayers());
                }
            } catch (WAMException e) {
                e.printStackTrace();
            }
        }
    }
}
