package server;

/**
 * The actual class where the Game runs.
 */
public class WAMGame implements Runnable{
    private WAMPlayer[] players;
    private WAM wam;
    private int gameDuration;

    public WAMGame(int row, int column, WAMPlayer[] players, int gameDuration){
        this.players = players;
        this.wam = new WAM(row, column);
        this.gameDuration = gameDuration;
    }

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

    public void informPlayers(boolean state, int row, int col){
        if(state){
            for(WAMPlayer p: players){
                p.moleUp(getHoleNumFromRowAndCol(row,col));
            }
        }
        else{
            for(WAMPlayer p: players){
                p.moleDown(getHoleNumFromRowAndCol(row,col));
            }
        }
    }

    public void closeAll(){
        for(WAMPlayer player: players){
            player.gameTied();
            player.close();
        }
    }

    private void whackCheck(){
        for(WAMPlayer player:players){
            int[] val = new int[2];
            if(0 == player.whack()){
                val = holeNoToRowAndColumn(player.whack());
            }
            if (wam.checkIfMole(val[0],val[1])){
                player.scoreUp();
            }
            else {
                player.scoreDown();
            }
        }
    }

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


    @Override
    public void run() {
        Thread[][] threads = new Thread[wam.getRow()][wam.getColumn()];
        long endTime = System.currentTimeMillis() + gameDuration*1000;
        for(int i = 0; i < wam.getRow();i++){
            for(int j = 0; j < wam.getColumn(); j++){
                threads[i][j] = new Thread(new Mole(this.wam, i,j));
            }
        }
        while (System.currentTimeMillis() < endTime) {
            for (int i = 0; i < wam.getRow(); i++) {
                for (int j = 0; j < wam.getColumn(); j++) {
                    threads[i][j].start();
                }
            }
            whackCheck();

            //TODO Figure out how to pop up multiple Moles at the Same time

        }
        System.out.println("Game Over");
        closeAll();
     }
}
