package client.gui;
import common.WAMException;
import common.WAMProtocol;

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
//        while (this.goodToGo()) {
//            try {
//                String request = this.networkIn.next();
//                String arguments = this.networkIn.nextLine().trim();
//                ConnectFourNetworkClient.dPrint( "Net message in = \"" + request + '"' );
//
//                switch ( request ) {
//                    case MAKE_MOVE:
//                        makeMove();
//                        break;
//                    case MOVE_MADE:
//                        moveMade( arguments );
//                        break;
//                    case GAME_WON:
//                        gameWon();
//                        break;
//                    case GAME_LOST:
//                        gameLost();
//                        break;
//                    case GAME_TIED:
//                        gameTied();
//                        break;
//                    case ERROR:
//                        error( arguments );
//                        break;
//                    default:
//                        System.err.println("Unrecognized request: " + request);
//                        this.stop();
//                        break;
//                }
//            }
//            catch( NoSuchElementException nse ) {
//                // Looks like the connection shut down.
//                this.error( "Lost connection to server." );
//                this.stop();
//            }
//            catch( Exception e ) {
//                this.error( e.getMessage() + '?' );
//                this.stop();
//            }
//        }
//        this.close();
//    }
//    }


}}
