package server;

import common.WAMProtocol;
import common.WAMException;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * This class represents a Player in the Game.
 */
public class WAMPlayer implements WAMProtocol, Closeable {
    //Socket belonging to the Client
    private  Socket sock;
    //Scanner used to read WHACK
    private  Scanner scanner;
    //printer to print to the client
    private  PrintStream printer;
    //Score for the current player
    private int score;
    //Player Number
    private int playerNo;

    /**
     * Constructor for this class, it creates the Scanner and the Printer.
     * @param sock Socket for Connection
     * @throws WAMException Exception thrown by the Game
     */
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
    public void scoreUp(){
        this.score += 2;
    }

    public void scoreDown(){
        this.score -= 1;
    }

    public int whack(){
        String response = scanner.nextLine();
        String[] args = response.trim().split(" ");
        if(args[0].equals(WHACK)){
            return Integer.parseInt(args[1]);
        }
        else{
            return Integer.parseInt(null);
        }
    }

    /**
     * Method that sends out the welcome message to the Client
     * @param row Number of rows
     * @param column Number of Columns
     * @param players Number of Players in the Game
     * @param playerNo Player Number
     */
    public void welcome(int row, int column, int players, int playerNo) {
        this.playerNo = playerNo;
        printer.println(WELCOME+" "+row+" "+column+" "+players+" "+playerNo);
    }

    /**
     * Sends the MOLE UP message with the hole of mole that is up.
     * @param hole Hole where mole should be up
     */
    public void moleUp(int hole){
        printer.println(MOLE_UP+" "+hole);
    }

    /**
     * Sends the MOLE DOWN message with the hole of mole that goes down
     * @param hole Hole where the mole should be down
     */
    public void moleDown(int hole){
        printer.println(MOLE_DOWN+" "+hole);
    }

    /**
     * Sends the Scores to the Client
     * @param scores array of scores
     */
    public void scores(int[] scores){
        this.score = scores[playerNo];
        String s = "";
        for(int i:scores){
            s = s + " " + i;
        }
        printer.println(SCORE + " " + s);
    }

//    public int whack() throws WAMException {
//        String response = scanner.nextLine();
//
//        if(response.startsWith(WHACK)) {
//            String[] tokens = response.split(" ");
//            if(tokens.length == 2) {
//                return Integer.parseInt(tokens[1]);
//            }
//            else {
//                throw new WAMException("Invalid player response: " +
//                        response);
//            }
//        }
//        else {
//            throw new WAMException("Invalid player response: " +
//                    response);
//        }
//    }

    /**
     * Sends the Game Won message to the Client.
     */
    public void gameWon() {
        printer.println(GAME_WON);
    }

    /**
     * Sends the Game lost message to the Client.
     */
    public void gameLost() {
        printer.println(GAME_LOST);
    }

    /**
     * Sends the Game Tied Message to the Client.
     */
    public void gameTied(){
        printer.println(GAME_TIED);
    }

    /**
     * Send the error message to the Client.
     * @param message
     */
    public void error(String message) {
        printer.println(ERROR + " " + message);
    }

    /**
     * Closes the Sockets and the Connections.
     */
    @Override
    public void close() {
        try {
            printer.close();
            scanner.close();
            sock.close();
        }
        catch(IOException ioe) {
            // squash
        }
    }
}
