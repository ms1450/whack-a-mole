package server;

public class Mole extends Thread{
    private int row;
    private int col;
    private WAMGame game;
    private boolean running;

    Mole(int row, int col, WAMGame game){
        this.row = row;
        this.col = col;
        this.game = game;
        running = true;
    }

    public void stopRunning(){
        running = false;
    }

    public void run(){
        while (true) {
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