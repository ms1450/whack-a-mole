package server;

import common.WAMException;
import common.WAMProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This is the Server of WAM
 * @author Dade Wood
 * @author Mehul Sen
 */
public class WAMServer implements WAMProtocol ,Runnable {

    private ServerSocket server;
    private int row;
    private int column;
    private int players;
    private int gameDuration;

    /**
     * Constructor for this Class
     * @param port Port number to connect with Client on
     * @param row Rows of the Board
     * @param column Columns of the Board
     * @param players Number of Players playing the Game
     * @param gameDuration The duration for which the game should run.
     * @throws WAMException Any Exception Caused by the Game
     */
    public WAMServer(int port, int row, int column, int players, int gameDuration) throws WAMException {
        try {
            server = new ServerSocket(port);
            this.column = column;
            this.players = players;
            this.row = row;
            this.gameDuration = gameDuration;
        } catch (IOException e) {
            throw new WAMException(e);
        }
    }

    /**
     * Main method to run the Server.
     * @param args Arguments passed.
     * @throws WAMException Exception the Game might cause.
     */
    public static void main(String[] args) throws WAMException {

        if (args.length != 5) {
            System.out.println("Usage: java WAMServer <port> <row> <column> <players> <Game Duration Seconds>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        int row = Integer.parseInt(args[1]);
        int column = Integer.parseInt(args[2]);
        int players = Integer.parseInt(args[3]);
        int gameDuration = Integer.parseInt(args[4]);
        WAMServer server = new WAMServer(port,row,column,players,gameDuration);
        server.run();
    }

    /**
     * Runs the WAMGame Class and starts the Game by sending out the WELCOME Messages.
     */
    @Override
    public void run() {
        try {
            WAMPlayer[] playerArray = new WAMPlayer[players];
            for(int playerNo = 0; playerNo < players; playerNo++){
                System.out.println("Waiting for player "+playerNo+1+ "... ");
                Socket socket = server.accept();
                    WAMPlayer play = new WAMPlayer(socket);
                    playerArray[playerNo] = play;
                    play.welcome(row,column,players,playerNo);
                    System.out.println("Player "+playerNo+1+" is Connected!");
            }
            System.out.println("Starting game!");
            WAMGame game = new WAMGame(row, column, playerArray, gameDuration*1000);
            // server is not multithreaded
            new Thread(game).run();
        } catch (IOException e) {
            System.err.println("Something has gone horribly wrong!");
            e.printStackTrace();
        } catch (WAMException e) {
            System.err.println("Failed to create players!");
            e.printStackTrace();
        }
    }
}

