package server;

/**
 * The actual class where the Game runs.
 */
public class WAMGame implements Runnable{
    private WAMPlayer[] players;
    private WAM wam;
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
     * Method that retrieves a hole number from row and columns.
     * @param row Row Number
     * @param col Column Number
     * @return Hole Number
     */
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

    /**
     * Used to inform players if a Mole is Up or Down
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

    public long getRandomUptime(){
        return wam.getRandomUpTime();
    }

    public long getRandomDowntime(){
        return wam.getRandomDownTime();
    }

    /**
     * closes the Game
     */
    public void closeAll(){
        for(WAMPlayer player: players){
            //TODO calculate the Scores and send players messages.
            player.gameTied();
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
     * Thread run Method
     */
    @Override
    public void run() {
        Thread[][] threads = new Thread[wam.getRow()][wam.getColumn()];
        long endTime = System.currentTimeMillis() + gameDuration;
        for (int i = 0; i < wam.getRow(); i++) {
                for (int j = 0; j < wam.getColumn(); j++) {
                    threads[i][j] = new Thread(new Mole(i,j,this));
                }
            }
        for(Thread[] thr:threads) for(Thread thread: thr){
            thread.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //whackCheck();
        //TODO Figure out how to pop up multiple Moles at the Same time
        System.out.println("Game Over");
        //closeAll();
     }
}
