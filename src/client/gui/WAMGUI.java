package client.gui;

import client.Observer;
import client.WAMBoard;
import client.WAMClient;

import common.WAMException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;

public class WAMGUI extends Application implements Observer<WAMBoard>{
    // TODO Dade
    private WAMClient client;
    private WAMBoard board;
    private GridPane holes;
    private Label score;
    private BorderPane window;
    private Scene scene;

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

    public ObservableList<Node> getButton(int col, int row){
        return ((StackPane)holes.getChildren().get(col*board.getRows() + row)).getChildren();
    }

    public void refresh() {
        for (int i = 0; i < board.getRows()*board.getColumns(); i++){
            if (board.getContents(i) == WAMBoard.Hole.OCCUPIED){
                ((StackPane) scene.lookup("#" + i)).getChildren().get(1).setVisible(true);
            }else {
                ((StackPane) scene.lookup("#" + i)).getChildren().get(1).setVisible(false);
            }
        }

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
