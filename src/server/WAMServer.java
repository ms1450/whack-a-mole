package server;

import common.WAMException;
import common.WAMProtocol;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This is the Server of WAM
 * @author Mehul Sen
 * @author Dade Wood
 */
public class WAMServer implements WAMProtocol ,Runnable {
    //The Server Socket for the Game
    private ServerSocket server;
    //Number of ROWS
    private final int ROWS;
    //Number of COLUMNS
    private final int COLUMNS;
    //Number of Players
    private final int PLAYERS;
    //Duration of the Game
    private final int GAME_DURATION;

    /**
     * Constructor for this Class
     * @param port Port number to connect with Client on
     * @param rows Rows of the Board
     * @param columns Columns of the Board
     * @param players Number of Players playing the Game
     * @param gameDuration The duration for which the game should run.
     * @throws WAMException Any Exception Caused by the Game
     */
    public WAMServer(int port, int rows, int columns, int players,
                     int gameDuration) throws WAMException {
        try {
            server = new ServerSocket(port);
            this.COLUMNS = columns;
            this.PLAYERS = players;
            this.ROWS = rows;
            this.GAME_DURATION = gameDuration;
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
        WAMServer server = new WAMServer(port,row,column,players,
                gameDuration*1000);
        server.run();
    }

    /**
     * Runs the WAMGame Class and starts the Game by sending out the WELCOME Messages.
     */
    @Override
    public void run() {
        try {
            WAMPlayer[] playerArray = new WAMPlayer[PLAYERS];
            for(int playerNo = 0; playerNo < PLAYERS; playerNo++){
                System.out.println("Waiting for player " + (playerNo+1) + ".." +
                        ". ");
                Socket socket = server.accept();
                WAMPlayer play = new WAMPlayer(socket);
                playerArray[playerNo] = play;
                play.welcome(ROWS,COLUMNS,PLAYERS,playerNo);
                System.out.println("Player " + (playerNo+1) + " is " +
                        "Connected!");
            }
            System.out.println("Starting game!");
            WAMGame game = new WAMGame(ROWS, COLUMNS, playerArray,GAME_DURATION);
            new Thread(game).start();
        } catch (IOException e) {
            System.err.println("Something has gone horribly wrong!");
            e.printStackTrace();
        } catch (WAMException e) {
            System.err.println("Failed to create players!");
            e.printStackTrace();
        }
    }
}

