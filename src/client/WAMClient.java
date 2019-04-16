package client;

import common.WAMException;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static common.WAMProtocol.*;

/**
 * The client takes messages from the server, modifies its version of the
 * WAMBoard and tells the view (GUI) to update.
 * @author Mehul Sen
 * @author Dade Wood
 */
public class WAMClient {

    private static final boolean DEBUG = false;

    private static void dPrint( Object logMsg ) {
        if ( WAMClient.DEBUG ) {
            System.out.println( logMsg );
        }
    }

    // The socket for this client
    private Socket clientSocket;

    // Takes in output from the server
    private Scanner networkIn;

    // Sends messages back to the server
    private PrintStream networkOut;

    // The board that this client is interacting with
    private WAMBoard board;

    // If the server is running
    private boolean go;

    /**
     * Checks if the client is good to run.
     * @return
     */
    private synchronized boolean goodToGo() {
        return this.go;
    }

    /**
     * Tells the client that it should stop running.
     */
    private synchronized void stop() {
        this.go = false;
    }

    /**
     * Sends an error with a message.
     * @param arguments the message
     */
    public void error( String arguments ) {
        WAMClient.dPrint( '!' + ERROR + ',' + arguments );
        dPrint( "Fatal error: " + arguments );
        this.board.error( arguments );
        this.stop();
    }

    /**
     * Creates the client.
     * @param host The host name for the socket
     * @param port The port number for the socket
     * @param board The board that this client will interact with
     * @throws WAMException When an error occurs
     */
    public WAMClient(String host, int port, WAMBoard board)
            throws WAMException {
        try {
            this.clientSocket = new Socket(host, port);
            this.networkIn = new Scanner(clientSocket.getInputStream());
            this.networkOut = new PrintStream(clientSocket.getOutputStream());
            this.board = board;
            this.go = true;

            // Block waiting for the CONNECT message from the server.
            String request = this.networkIn.next();
            String arguments = this.networkIn.nextLine();
            if (!request.equals(WELCOME)) {
                throw new WAMException("Expected WELCOME from server");
            }
            createBoard(arguments);
            WAMClient.dPrint("Connected to server " + this.clientSocket);
        }
        catch(IOException e) {
            throw new WAMException(e);
        }
    }

    /**
     * Tells the observer to start listening for updates.
     */
    public void startListener() {
        new Thread(() -> this.run()).start();
    }

    /**
     * Tells the board that the game has been won.
     */
    public void gameWon() {
        WAMClient.dPrint( '!' + GAME_WON );

        dPrint( "You won! Yay!" );
        this.board.gameWon();
        this.stop();
    }

    /**
     * Tells the board that the game has been lost.
     */
    public void gameLost() {
        WAMClient.dPrint( '!' + GAME_LOST );
        dPrint( "You lost! Boo!" );
        this.board.gameLost();
        this.stop();
    }

    /**
     * Tells the board that the game has been tied.
     */
    public void gameTied() {
        WAMClient.dPrint( '!' + GAME_TIED );
        dPrint( "You tied! Meh!" );
        this.board.gameTied();
        this.stop();
    }


    /**
     * Tells the board that a mole has gone up.
     * @param args which hole the mole occupies
     */
    public void moleUp(String args){
        String[] val = args.trim().split(" ");
        int holeNo = Integer.parseInt(val[0]);
        this.board.holeUp(holeNo);
    }

    /**
     * Tells the board that a mole has gone down.
     * @param args which hole the mole leaves
     */
    public void moleDown(String args){
        String[] val = args.trim().split(" ");
        int holeNo = Integer.parseInt(val[0]);
        this.board.holeDown(holeNo);
    }

    /**
     * Tells the board that the scores have been updated.
     * @param args the new scores
     */
    public void scoresUpdate(String args){
        String[] val = args.trim().split(" ");
        int[] scores = new int[val.length];
        int i = 0;
        for(String s:val) {
            scores[i] = Integer.parseInt(s);
            i++;
        }
        this.board.updateScore(scores);
    }

    /**
     * Creates the board when it receives the welcome message.
     * @param args the row, column, players, and playerNo of the board being
     *             created
     */
    public void createBoard(String args){
        String[] fields = args.trim().split( " " );
        int row = Integer.parseInt(fields[0]);
        int column = Integer.parseInt(fields[1]);
        int players = Integer.parseInt(fields[2]);
        int playerNo = Integer.parseInt(fields[3]);
        this.board.initializeBoard(row,column,players,playerNo);
    }

    /**
     * Send the WHACK to the Server
     * @param holeNo hole number on which WHACK is made
     */
    public void sendWHACK(int holeNo){
        this.networkOut.println(WHACK + " " + holeNo + " " + this.board.getPlayerNo());
    }

    /**
     * Closes the client and the board.
     */
    public void close() {
        try {
            this.clientSocket.close();
        }
        catch( IOException ioe ) {
            // squash
        }
        this.board.close();
    }

    /**
     * Depending on the message received from the server, the client will do
     * various actions.
     */
    private void run() {
        while (this.goodToGo()) {
            try {
                String request = this.networkIn.next();
                String arguments = this.networkIn.nextLine().trim();
                WAMClient.dPrint( "Net message in = \"" + request + '"' );
                //System.out.println(request+arguments);
                switch ( request ) {
                    case SCORE:
                        scoresUpdate(arguments);
                        break;
                    case MOLE_UP:
                        moleUp(arguments);
                        break;
                    case MOLE_DOWN:
                        moleDown( arguments );
                        break;
                    case GAME_WON:
                        gameWon();
                        break;
                    case GAME_LOST:
                        gameLost();
                        break;
                    case GAME_TIED:
                        gameTied();
                        break;
                    case ERROR:
                        error( arguments );
                        break;
                    default:
                        System.err.println("Unrecognized request: " + request);
                        this.stop();
                        break;
                }
            }
            catch( NoSuchElementException nse ) {
                // Looks like the connection shut down.
                this.error( "Lost connection to server." );
                this.stop();
            }
            catch( Exception e ) {
                this.error( e.getMessage() + '?' );
                this.stop();
            }
        }
        this.close();
    }
}
