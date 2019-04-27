package server;

/**
 * Represents a Mole in The Board
 * @author Mehul Sen
 * @author Dade Wood
 */
public class Mole extends Thread{
    private int row;
    private int col;
    private WAMGame game;
    private long endTime;

    /**
     * Creates the Mole instance
     * @param row Row Number of the Mole
     * @param col Col Number of the Mole
     * @param game The instance of the Game
     * @param endTime The duration till which the game is supposed to run
     */
    Mole(int row, int col, WAMGame game, long endTime){
        this.row = row;
        this.col = col;
        this.game = game;
        this.endTime = endTime;
    }

    /**
     * The actual working of the Mole, Runs till the the endTime is reached.
     */
    public void run() {
        while (System.currentTimeMillis() < endTime) {

            game.informPlayers(true, row, col);
            try {
                Thread.sleep(game.getRandomUptime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            game.informPlayers(false, row, col);
            try {
                Thread.sleep(game.getRandomDowntime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}