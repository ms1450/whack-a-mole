package client.gui;
import common.WAMException;
import common.WAMProtocol;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
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



    private void run() {
    }


}
