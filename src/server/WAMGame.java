package server;

import java.lang.reflect.Array;
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

    public ArrayList<WAMPlayer> getPlayers(){
        return new ArrayList<>(Arrays.asList(players));
    }

    /**
     * Method that retrieves a hole number from row and columns.
     * @param row Row Number
     * @param col Column Number
     * @return Hole Number
     */
    public int getHoleNumFromRowAndCol(int row, int col){
        int colTotal = wam.getColumn();
        int rowCounter = 0;
        int colCounter = 0;
        int counter = 0;
        while(rowCounter < row){
            counter += colTotal ;
            rowCounter ++;
        }
        while(colCounter < col){
            counter += 1;
            colCounter ++;
        }
        return counter;
    }

    /**
     * Used to inform players if a Mole is Up or Down as well as update the Board
     * @param state true if mole is up and false if mole is down
     * @param row row number
     * @param col column number
     */
    public void informPlayers(boolean state, int row, int col){
        if(state){
            wam.moleUp(row,col);
            for(WAMPlayer p: players){
                p.moleUp(getHoleNumFromRowAndCol(row,col));
            }
        }
        else{
            wam.moleDown(row,col);
            for(WAMPlayer p: players){
                p.moleDown(getHoleNumFromRowAndCol(row,col));
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
     * closes the Game, sends win, lose and tie messages.
     */
    public void closeAll(){
        ArrayList<WAMPlayer> winners = scoreWinner()[0];
        if(winners.size() > 1){
            for(WAMPlayer player: players){
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
     * Checks if a Whack has been made
     */
//    private void whackCheck(){
//        for(WAMPlayer player:players){
//            int[] val = new int[2];
//            if(0 == player.whack()){
//                val = holeNoToRowAndColumn(player.whack());
//            }
//            if (wam.checkIfMole(val[0],val[1])){
//                player.scoreUp();
//            }
//            else {
//                player.scoreDown();
//            }
//        }
//    }

    /**
     * Method that retrieves the row and column number from the Hole Number
     * @param hole hole number
     * @return array with row and column
     */
    private int[] holeNoToRowAndColumn(int hole){
        int rowNum = 0;
        int colNum = 0;
        int maxRow = 3;
        while(hole>maxRow){
            hole -= maxRow+1;
            rowNum ++;
        }
        colNum = hole;
        return new int[]{rowNum,colNum};
    }


    /**
     * Thread run Method that runs the Moles.
     */
    @Override
    public void run() {
        Thread[][] threads = new Thread[wam.getRow()][wam.getColumn()];

        //Creates the Threads for all the Moles
        for (int i = 0; i < wam.getRow(); i++) {
            for (int j = 0; j < wam.getColumn(); j++) {
                long endTime = System.currentTimeMillis() + gameDuration;
                threads[i][j] = new Thread(new Mole(i, j, this, endTime));
            }
        }
        for(WAMPlayer player:players){
            new Thread(new PlayerWHACKChecker(player,wam,gameDuration,this)).start();
        }
        //Starts those moles in a gap of 1 second to avoid overcrowding.
        //TODO Fix the Timings (Shouldnt be a Major issue , Works as is.)
        for (int i = 0; i < wam.getRow(); i++) {
            for (int j = 0; j < wam.getColumn(); j++) {

                threads[i][j].start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                   e.printStackTrace();
                }
            }
        }
        System.out.println("Game Over");
        //TODO Figure out a Way to Read Whack messages and update the score for each player.
        closeAll();
    }

}

