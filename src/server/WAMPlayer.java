package server;

import common.WAMProtocol;
import common.WAMException;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class represents a Player in the Game.
 * @author Mehul Sen
 * @author Dade Wood
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

    /**
     * Method that adds the Score by 2
     */
    public void scoreUp(){
        this.score += 2;
    }

    /**
     * Method that subtracts the score by 1
     */
    public void scoreDown(){
        this.score -= 1;
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
    public void scores(ArrayList<WAMPlayer> scores){
        this.score = scores.get(playerNo).getScore();
        String s = "";
        for(WAMPlayer player:scores){
            s = s + " " + player.getScore();
        }
        printer.println(SCORE + " " + s);
    }

    /**
     * Returns the Score of the Player
     * @return score of the Player
     */
    public int getScore(){
        return score;
    }

    public boolean hasWhack() throws WAMException{
        return scanner.hasNext();
    }
    //TODO Hole Numbers are messeed UP
    public int whack() throws WAMException{
        String response = scanner.nextLine();
        String[] tokens = response.trim().split(" ");
        if(Integer.parseInt(tokens[2]) == playerNo){
            return Integer.parseInt(tokens[1]);
        }
        else throw new WAMException(("Invalid Player Response"));
    }

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
     * @param message error message
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
