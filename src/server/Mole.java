package server;

public class Mole implements Runnable {
    private WAM wam;
    private int rowNo;
    private int colNo;
    public Mole(WAM wam, int rowNo, int colNo){
        this.wam = wam;
        this.rowNo = rowNo;
        this.colNo = colNo;
    }


    @Override
    public void run(){
        try{
            while (true){

                Thread.sleep(wam.getRandomDownTime());
                wam.popOut(rowNo,colNo);
                Thread.sleep(wam.getRandomUpTime());
                wam.popIn(rowNo,colNo);
            }
        }
        catch (InterruptedException e){e.printStackTrace();}
    }
}
