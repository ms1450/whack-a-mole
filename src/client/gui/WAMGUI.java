package client.gui;

import client.Observer;
import client.WAMBoard;
import client.WAMClient;

import common.WAMException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.List;

public class WAMGUI extends Application implements Observer<WAMBoard>{
    // TODO Dade
    private WAMClient client;
    private WAMBoard board;
    private GridPane holes;
    private Label score;
    private BorderPane window;

    @Override
    public void init() throws WAMException {
        List<String> args = getParameters().getRaw();
        String host = args.get(0);
        int port = Integer.parseInt(args.get(1));

        this.board = new WAMBoard();
        this.board.addObserver(this);
        this.client = new WAMClient(host, port, this.board);
    }

    public void createHoles(){
        this.holes = new GridPane();
        this.holes.setHgap(10);
        this.holes.setVgap(10);
        System.out.println(board.getColumns());
        for (int i = 0; i < board.getColumns(); i++){
            for (int j = 0; j < board.getRows(); j++){
                ImageView hole = new ImageView(getClass().getResource("hole" +
                        ".png").toExternalForm());
                ImageView mole = new ImageView(getClass().getResource("mole" +
                        ".png").toExternalForm());
                StackPane pane = new StackPane();
                pane.getChildren().addAll(hole, mole);
                this.holes.setAlignment(Pos.BOTTOM_LEFT);
                this.holes.add(pane, i, j);
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        createHoles();

        window = new BorderPane();
        window.setCenter(holes);

        stage.setScene(new Scene(window));
        stage.setResizable(false);
        stage.setTitle("Whack-A-Mole");
        stage.show();

        client.startListener();
    }

    public void refresh() {

    }

    @Override
    public void update(WAMBoard wamBoard) {
        if (Platform.isFxApplicationThread()){
            this.refresh();
        } else {
            Platform.runLater(() -> this.refresh());
        }
    }

    @Override
    public void stop(){
        client.close();
        board.close();
    }

    public static void main(String[] args){
        if (args.length != 2){
            System.out.println("Usage: java WAMGUI host port");
            System.exit(-1);
        }else{
            Application.launch(args);
        }
    }

}
