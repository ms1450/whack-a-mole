package client;

import common.WAMException;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static common.WAMProtocol.*;

public class WAMClient {

    private static final boolean DEBUG = false;

    private static void dPrint( Object logMsg ) {
        if ( WAMClient.DEBUG ) {
            System.out.println( logMsg );
        }
    }

    private Socket clientSocket;
    private Scanner networkIn;
    private PrintStream networkOut;
    private WAMBoard board;
    private boolean go;

    private synchronized boolean goodToGo() {
        return this.go;
    }

    private synchronized void stop() {
        this.go = false;
    }

    public void error( String arguments ) {
        WAMClient.dPrint( '!' + ERROR + ',' + arguments );
        dPrint( "Fatal error: " + arguments );
        this.board.error( arguments );
        this.stop();
    }

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

    public void startListener() {
        new Thread(() -> this.run()).start();
    }

    public void gameWon() {
        WAMClient.dPrint( '!' + GAME_WON );

        dPrint( "You won! Yay!" );
        this.board.gameWon();
        this.stop();
    }

    public void gameLost() {
        WAMClient.dPrint( '!' + GAME_LOST );
        dPrint( "You lost! Boo!" );
        this.board.gameLost();
        this.stop();
    }

    public void gameTied() {
        WAMClient.dPrint( '!' + GAME_TIED );
        dPrint( "You tied! Meh!" );
        this.board.gameTied();
        this.stop();
    }


    public void moleUp(String args){
        String[] val = args.trim().split(" ");
        int holeNo = Integer.parseInt(val[0]);
        this.board.holeUp(holeNo);
    }

    public void moleDown(String args){
        String[] val = args.trim().split(" ");
        int holeNo = Integer.parseInt(val[0]);
        this.board.holeDown(holeNo);
    }

    public void scoresUpdate(String args){
        String[] val = args.trim().split(" ");
        int[] scores = new int[val.length-1];
        int i = 0;
        for(String s:val) {
            scores[i] = Integer.parseInt(s);
            i++;
        }
        this.board.updateScore(scores);
    }

    public void createBoard(String args){
        String[] fields = args.trim().split( " " );
        int row = Integer.parseInt(fields[0]);
        int column = Integer.parseInt(fields[1]);
        int players = Integer.parseInt(fields[2]);
        int playerNo = Integer.parseInt(fields[3]);
        this.board.initializeBoard(row,column,players,playerNo);
    }

    public WAMBoard getBoard(){
        return board;
    }

    public void close() {
        try {
            this.clientSocket.close();
        }
        catch( IOException ioe ) {
            // squash
        }
        this.board.close();
    }

    private void run() {
        while (this.goodToGo()) {
            try {
                String request = this.networkIn.next();
                String arguments = this.networkIn.nextLine().trim();
                WAMClient.dPrint( "Net message in = \"" + request + '"' );
                System.out.println(request+arguments);
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
