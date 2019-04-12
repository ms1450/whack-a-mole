package server;

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

    @Override
    public void run() {
        long endTime = System.currentTimeMillis() + gameDuration*1000;
        while (System.currentTimeMillis() < endTime) {
            //loops till the seconds have passed.
            int[] pos = wam.chooseRandomSlot();
            //pos[0] will be the row and pos[1] the column
            //Now we have to pop that specific mole and wait till either the player knock it
            // or a specific time passes, 1 sec? 500 mili? something in that value which has to be random as well.
            // and based on that get inputs from the user (which can be done later, update the score.)

        }










        // TODO Dade
//        boolean go = true;
//        while(go) {
//            try {
//                if(makeMove(playerOne, playerTwo)) {
//                    go = false;
//                }
//                else if(makeMove(playerTwo, playerOne)) {
//                    go = false;
//                }
//            }
//            catch(ConnectFourException e) {
//                playerOne.error(e.getMessage());
//                playerTwo.error(e.getMessage());
//                go = false;
//            }
//        }
//
//        playerOne.close();
//        playerTwo.close();
    }
}
