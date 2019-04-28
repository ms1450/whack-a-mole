package server;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The actual implementation of the Game.
 * This controls the Mole Up and Down movement as well as calculates any scores.
 * @author Mehul Sen
 * @author Dade Wood
 */
public class WAMGame implements Runnable{
    //Array of Players
    private WAMPlayer[] players;
    //Wam Board
    private WAM wam;
    //Duration the Game will last for
    private int gameDuration;

    /**
     * Constructor to Create the Game
     * @param row Rows of the Board
     * @param column Columns of the Board
     * @param players Number of Players Playing the Game
     * @param gameDuration Duration for which the Game will last
     */
    public WAMGame(int row, int column, WAMPlayer[] players, int gameDuration){
        this.players = players;
        this.wam = new WAM(row, column);
        this.gameDuration = gameDuration;
    }

    /**
     * Returns an ArrayList of the Players in the Game
     * @return ArrayList of Players
     */
    public ArrayList<WAMPlayer> getPlayers(){
        return new ArrayList<>(Arrays.asList(players));
    }

    /**
     * Used to inform players if a Mole is Up or Down as well as update the Board
     * @param state true if mole is up and false if mole is down
     *
     */
    public void informPlayers(boolean state, int holeNo){
        if(state){
            wam.moleUp(holeNo);
            for(WAMPlayer p: players){
                p.moleUp(holeNo);
            }
        }
        else{
            wam.moleDown(holeNo);
            for(WAMPlayer p: players){
                p.moleDown(holeNo);
            }
        }
    }

    /**
     * the Up time for a Mole
     * @return Random Up Time
     */
    public long getRandomUptime(){
        return wam.getRandomUpTime();
    }

    /**
     * the Down time for a Mole
     * @return Random Mole Time
     */
    public long getRandomDowntime(){
        return wam.getRandomDownTime();
    }

    /**
     * Calculates the Scores and picks the winners and losers
     * @return Array of winners and losers
     */
    public ArrayList<WAMPlayer>[] scoreWinner(){
        ArrayList<WAMPlayer> winners = new ArrayList<>();
        ArrayList<WAMPlayer> losers = new ArrayList<>();
        int highestScore = 0;
        for(WAMPlayer player: players){
            if(highestScore < player.getScore()){
                highestScore = player.getScore();
            }
        }
        for (WAMPlayer player: players){
            if(highestScore == player.getScore()){
                winners.add(player);
            }
            else{
                losers.add(player);
            }
        }
        return new ArrayList[]{winners,losers};
    }

    /**
     * Closes the Game, sends win, lose and tie messages.
     */
    public void closeAll(){
        ArrayList<WAMPlayer> winners = scoreWinner()[0];
        if(winners.size() > 1){
            for(WAMPlayer player: winners){
                player.gameTied();
                player.close();
            }
        }
        else{
            winners.get(0).gameWon();
            winners.get(0).close();
        }
        ArrayList<WAMPlayer> losers = scoreWinner()[1];
        for(WAMPlayer player: losers) {
            player.gameLost();
            player.close();
        }
    }

    /**
     * Thread run Method that runs the Moles and Players.
     */
    @Override
    public void run() {
        Thread[] threads = new Thread[wam.getRow()*wam.getColumn()];
        //Creates the Threads for the Moles.
        for (int i = 0; i < threads.length; i++) {
                long endTime = System.currentTimeMillis() + gameDuration;
                threads[i] = new Thread(new Mole(i, this, endTime));
        }
        //Creates the Threads for the Players
        for(WAMPlayer player:players){
            new Thread(new PlayerWHACKChecker(player, wam, gameDuration,this)).start();
        }
        //Starts the Mole Threads
        for (Thread mole: threads) {
            mole.start();
        }
        //Sleeps the method for the duration of the Game
        try {
            Thread.sleep(gameDuration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Game Over");
        closeAll();
    }

}

