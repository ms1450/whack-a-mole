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

    public synchronized void correct(int holeNo) throws WAMException {
        player.scoreUp();

        game.informPlayers(false, holeNo);
    }

    public void run(){
        long end = System.currentTimeMillis() + duration;
        while (System.currentTimeMillis() < end){
            try {
                if(player.hasWhack()){
                    int holeNo = player.whack();
                    if(wam.checkIfMole(holeNo)){
                        correct(holeNo);
                    }
                    else {
                        player.scoreDown();
                    }
                    for (WAMPlayer player: game.getPlayers()) {
                        player.scores(game.getPlayers());
                    }
                }
                else{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (WAMException e) {
                e.printStackTrace();
            }
        }
    }
}
