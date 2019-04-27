package server;

/**
 * Represents a Mole in The Board
 * @author Mehul Sen
 * @author Dade Wood
 */
public class Mole extends Thread{
    private int holeNo;
    private WAMGame game;
    private long endTime;

    /**
     * Creates the Mole instance
     *
     * @param game The instance of the Game
     * @param endTime The duration till which the game is supposed to run
     */
    Mole(int holeNo, WAMGame game, long endTime){
        this.holeNo = holeNo;
        this.game = game;
        this.endTime = endTime;
    }

    /**
     * The actual working of the Mole, Runs till the the endTime is reached.
     */
    public void run() {
        while (System.currentTimeMillis() < endTime) {

            game.informPlayers(false, holeNo);
            try {
                Thread.sleep(game.getRandomDowntime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            game.informPlayers(true, holeNo);
            try {
                Thread.sleep(game.getRandomUptime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}