package server;

public class WAMGame implements Runnable {
    private WAMPlayer[] players;
    private WAM wam;

    public WAMGame(WAMPlayer[] players, int gameDuration){
        this.players = players;
        this.wam = new WAM(gameDuration);
    }

    @Override
    public void run() {
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
