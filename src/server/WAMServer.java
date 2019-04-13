package server;

import common.WAMException;
import common.WAMProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WAMServer implements WAMProtocol ,Runnable {

    private ServerSocket server;
    private int row;
    private int column;
    private int players;
    private int gameDuration;

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

    @Override
    public void run() {
        try {
            WAMPlayer[] playerArray = new WAMPlayer[players];
            for(int player = 0; player < players; player++){
                System.out.println("Waiting for player "+player+1);
                Socket socket = server.accept();
                    WAMPlayer play = new WAMPlayer(socket);
                    playerArray[player] = play;
                    play.welcome(row,column,players,player);
                    System.out.println("Player "+player+1+"is Connected!");
            }
            System.out.println("Starting game!");
            WAMGame game = new WAMGame(row, column, playerArray, gameDuration);
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

