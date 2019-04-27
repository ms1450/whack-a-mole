package server;
import common.WAMException;

/**
 * This Class represents a thread that checks if a players sends out a whack message and the responds appropriately.
 * @author Dade Wood
 * @author Mehul Sen
 */
public class PlayerWHACKChecker extends Thread {
    //The Player that is checking the game.
    private WAMPlayer player;
    //The instance of the WAM class
    private WAM wam;
    //Instance of the Game
    private WAMGame game;
    //Duration till which this game should run.
    private long duration;

    /**
     * This constructor initializes the Thread Class.
     * @param player Player to which this thread belongs to.
     * @param wam WAM instance
     * @param duration Duration till which this game will last
     * @param game Instance of the game
     */
    public PlayerWHACKChecker(WAMPlayer player, WAM wam, long duration, WAMGame game){
        this.player = player;
        this.wam = wam;
        this.duration = duration;
        this.game = game;
    }

    /**
     * This method runs if the player hits the mole.
     * @param holeNo Hole Number at which the player hits
     */
    public synchronized void correct(int holeNo) {
        player.scoreUp();
        game.informPlayers(false, holeNo);
    }

    /**
     * This is the method that checks if a player hits a mole and updates the scores accordingly.
     */
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
