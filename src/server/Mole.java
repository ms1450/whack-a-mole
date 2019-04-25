package server;

public class Mole extends Thread {
    private long uptime;
    private long downtime;
    private WAM wam;
    private WAMGame game;
    private int rowNo;
    private int colNo;

    Mole(WAM wam, int rowNo, int colNo, WAMGame game){
        this.wam = wam;
        this.uptime = wam.getRandomUpTime();
        this.downtime = wam.getRandomDownTime();
        this.rowNo = rowNo;
        this.colNo = colNo;
        this.game = game;
    }

    private void moleUp(){
        wam.popOut(rowNo,colNo);
        game.informPlayers(true,rowNo,colNo);
    }

    private void moleDown(){
        wam.popIn(rowNo,colNo);
        game.informPlayers(false,rowNo,colNo);
    }

    @Override
    public void run()
    {
        long timeUp = System.currentTimeMillis() + uptime;
        while(System.currentTimeMillis() < timeUp){
            moleUp();
        }
        long timeDown = System.currentTimeMillis() + downtime;
        while(System.currentTimeMillis() < timeDown){
            moleDown();
        }
    }
}
