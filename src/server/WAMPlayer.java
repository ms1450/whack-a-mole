package server;

import common.WAMProtocol;
import common.WAMException;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class WAMPlayer implements WAMProtocol, Closeable {
    private  Socket sock;
    private  Scanner scanner;
    private  PrintStream printer;

    public WAMPlayer(Socket sock) throws WAMException {
        this.sock = sock;
        try {
            scanner = new Scanner(sock.getInputStream());
            printer = new PrintStream(sock.getOutputStream());
        }
        catch (IOException e) {
            throw new WAMException(e);
        }
    }

    public void welcome(int row, int column, int players, int playerNo) {
        printer.println(WELCOME+" "+row+" "+column+" "+players+" "+playerNo);
    }

    public void moleUp(int hole){
        printer.println(MOLE_UP+" "+hole);
    }

    public void moleDown(int hole){
        printer.println(MOLE_DOWN+" "+hole);
    }

    public void scores(int[] scores){
        String s = SCORE;
        for(int i = 0; i < scores.length;i++){
            s = s + " " + scores[i];
        }
        printer.println(s);
    }

    public int whack() throws WAMException {
        String response = scanner.nextLine();

        if(response.startsWith(WHACK)) {
            String[] tokens = response.split(" ");
            if(tokens.length == 2) {
                return Integer.parseInt(tokens[1]);
            }
            else {
                throw new WAMException("Invalid player response: " +
                        response);
            }
        }
        else {
            throw new WAMException("Invalid player response: " +
                    response);
        }
    }

    public void gameWon() {
        printer.println(GAME_WON);
    }

    public void gameLost() {
        printer.println(GAME_LOST);
    }

    public void gameTied(){
        printer.println(GAME_TIED);
    }

    public void error(String message) {
        printer.println(ERROR + " " + message);
    }

    @Override
    public void close() {
        try {
            sock.close();
        }
        catch(IOException ioe) {
            // squash
        }
    }
}
