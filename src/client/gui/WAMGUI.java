package client.gui;

import client.Observer;
import client.WAMBoard;
import client.WAMClient;

import common.WAMException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;


/**
 * The GUI acts as a controller and view for the users to interact with the
 * game.
 * @author Dade Wood
 * @author Mehul Sen
 */
public class WAMGUI extends Application implements Observer<WAMBoard> {
    // The client that will will connect to the server.
    private WAMClient client;
    // The instance of the game board that the users will see.
    private WAMBoard board;
    // All of the holes that are displayed on the GUI
    private GridPane holes;
    // The board for the GUI that will be displayed to the user
    private StackPane guiBoard;
    //VBOX that holds the Scores for the Player
    private VBox scores;
    //The status of the Game
    private Label gameStatus;
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
    private void createHoles(){
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
                this.holes.setAlignment(Pos.CENTER);

                pane.setOnMouseClicked(event -> this.sendHit(pane));
            }
        }
    }

    /**
     * Sends the WHACK message to the Server.
     * @param pane Pane at which the WHACK Message is sent.
     */
    private void sendHit(StackPane pane){
        if (board.getStatus() == WAMBoard.Status.RUNNING) {
            int holeNo = Integer.parseInt(pane.getId());
            this.client.sendWHACK(holeNo);
        }
    }

    /**
     * Gets the Scores for the Players and updates it for the GUI
     */
    private void createScores(){
        scores = new VBox();
        for (int i = 0; i < board.getScores().length; i++){
            HBox scoreLine = new HBox();
            Text displayScore = new Text();
            Label displayPlayer = new Label("Player #" + (i+1) + ": ");
            displayPlayer.setFont(Font.font(30));
            displayScore.setText(Integer.toString(board.getScores()[i]));
            displayScore.setFont(Font.font(30));
            scoreLine.getChildren().addAll(displayPlayer, displayScore);
            scores.getChildren().add(scoreLine);
        }
    }

    /**
     * Creates the scene for javaFX to display for the users.
     * @param stage The window everything is shown on
     * @throws Exception If something goes wrong
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Creates the holes and scores for the GUI
        createHoles();
        createScores();

        // Creates the Label for the game status
        gameStatus = new Label();
        gameStatus.setFont(Font.font(10*board.getColumns() + 10*board.getRows()));
        gameStatus.setTextFill(Color.WHITE);

        // Sets the background to the game
        ImageView grass = new ImageView(getClass().getResource("grass" +
                ".jpg").toExternalForm());
        grass.setFitWidth(140*board.getColumns());
        grass.setFitHeight(140*board.getRows());

        // For when the game is over, darkens the game behind the status
        // being displayed
        Rectangle darkenGame = new Rectangle(140*board.getColumns(),
                140*board.getRows());
        darkenGame.setFill(Color.BLACK);
        darkenGame.setOpacity(.7);

        // Adds everything to the guiBoard
        guiBoard = new StackPane();
        guiBoard.getChildren().addAll(grass, holes, darkenGame, gameStatus);
        guiBoard.getChildren().get(2).setVisible(false);

        // Adds everything to the gridPane that will be viewed
        window = new BorderPane();
        window.setCenter(guiBoard);
        window.setRight(scores);

        // Adds margins around the holes and scores
        Insets scoreInset = new Insets(10, 75, 10, 10);
        Insets holeInset = new Insets(10);
        BorderPane.setMargin(scores, scoreInset);
        StackPane.setMargin(holes, holeInset);

        scene = new Scene(window);

        // Starts the window with a title of which player it is and non
        // resizable
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Whack-A-Mole Player #" + (board.getPlayerNo()+1));
        stage.show();

        client.startListener();
    }

    /**
     * Checks each hole for changes and then updates if it is occupied by a
     * mole or not in the view.
     */
    private void updateHoles(){
        for (int i = 0; i < board.getRows()*board.getColumns(); i++){
            if (board.getContents(i) == WAMBoard.Hole.OCCUPIED){
                ((StackPane) scene.lookup("#" + i)).getChildren().get(1).setVisible(true);
            }else {
                ((StackPane) scene.lookup("#" + i)).getChildren().get(1).setVisible(false);
            }
        }
    }

    /**
     * Changes the scores for the player to view.
     */
    private void updateScores(){
        for (int i = 0; i < board.getScores().length; i++){
            ((Text) ((HBox) scores.getChildren().get(i)).getChildren().get(1)).setText(Integer.toString(board.getScores()[i]));
        }
    }

    /**
     * Changes the status and displays it for the player to view.
     */
    private void updateStatus(){
        if (board.getStatus() == WAMBoard.Status.I_WON){
            gameStatus.setText("You Won.");
            guiBoard.getChildren().get(2).setVisible(true);
        } else if(board.getStatus() == WAMBoard.Status.I_LOST){
            gameStatus.setText("You Lost.");
            guiBoard.getChildren().get(2).setVisible(true);
        } else if (board.getStatus() == WAMBoard.Status.TIE){
            gameStatus.setText("You Tied.");
            guiBoard.getChildren().get(2).setVisible(true);
        } else if (board.getStatus() == WAMBoard.Status.ERROR) {
            gameStatus.setText("ERROR");
            guiBoard.getChildren().get(2).setVisible(true);
        }
    }

    /**
     * Refreshes the board of moles when something is changed and displays it
     * to the users.
     */
    private void refresh() {
        updateHoles();

        updateScores();

        updateStatus();
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
