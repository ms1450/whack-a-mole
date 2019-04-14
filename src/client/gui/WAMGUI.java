package client.gui;

import client.Observer;
import client.WAMBoard;
import client.WAMClient;

import common.WAMException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;


/**
 * The GUI acts as a controller and view for the users to interact with the
 * game.
 * @author Dade Wood
 * @author Mehul Sen
 */
public class WAMGUI extends Application implements Observer<WAMBoard> {
    // TODO Dade

    // The client that will will connect to the server.
    private WAMClient client;

    // The instance of the game board that the users will see.
    private WAMBoard board;

    // All of the holes that are displayed on the GUI
    private GridPane holes;

    private Label score;

    // The BorderPane that will hold all the GUI parts
    private BorderPane window;

    // The scene for the GUI (Used for quick lookup of the holes)
    private Scene scene;

    /**
     * Initializes the WAMBoard and WAMClient.
     * @throws WAMException If something goes wrong
     */
    @Override
    public void init() throws WAMException {
        List<String> args = getParameters().getRaw();
        String host = args.get(0);
        int port = Integer.parseInt(args.get(1));

        this.board = new WAMBoard();
        this.board.addObserver(this);
        this.client = new WAMClient(host, port, this.board);
    }

    /**
     * Creates the StackPanes that hold the images of the moles and holes and
     * adds each one the the gridPane.
     */
    public void createHoles(){
        this.holes = new GridPane();
        this.holes.setHgap(10);
        this.holes.setVgap(10);
        for (int i = 0; i < board.getColumns(); i++){
            for (int j = 0; j < board.getRows(); j++){
                ImageView hole = new ImageView(getClass().getResource("hole" +
                        ".png").toExternalForm());
                ImageView mole = new ImageView(getClass().getResource("mole" +
                        ".png").toExternalForm());
                StackPane pane = new StackPane();
                pane.setId(Integer.toString(i + j*board.getColumns()));
                pane.getChildren().addAll(hole, mole);
                pane.getChildren().get(1).setVisible(false);
                this.holes.add(pane, i, j);
            }
        }
    }

    /**
     * Creates the scene for javaFX to display for the users.
     * @param stage The window everything is shown on
     * @throws Exception If something goes wrong
     */
    @Override
    public void start(Stage stage) throws Exception {
        createHoles();

        window = new BorderPane();
        window.setCenter(holes);

        scene = new Scene(window);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Whack-A-Mole");
        stage.show();

        client.startListener();
    }

    /**
     * Refreshes the board of moles when something is changed and displays it
     * to the users.
     */
    public void refresh() {
        for (int i = 0; i < board.getRows()*board.getColumns(); i++){
            if (board.getContents(i) == WAMBoard.Hole.OCCUPIED){
                ((StackPane) scene.lookup("#" + i)).getChildren().get(1).setVisible(true);
            }else {
                ((StackPane) scene.lookup("#" + i)).getChildren().get(1).setVisible(false);
            }
        }

    }

    /**
     * Tells the board to refresh when the board changes.
     * @param wamBoard
     */
    @Override
    public void update(WAMBoard wamBoard) {
        if (Platform.isFxApplicationThread()){
            this.refresh();
        } else {
            Platform.runLater(() -> this.refresh());
        }
    }

    /**
     * Ends the client and board connection when the window is closed by the
     * user.
     */
    @Override
    public void stop(){
        client.close();
        board.close();
    }

    /**
     * Starts the GUI.
     * @param args The host and port number that the client needs to start on
     */
    public static void main(String[] args){
        if (args.length != 2){
            System.out.println("Usage: java WAMGUI host port");
            System.exit(-1);
        }else{
            Application.launch(args);
        }
    }
}
