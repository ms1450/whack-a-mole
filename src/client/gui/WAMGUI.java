package client.gui;

import client.Observer;
import client.WAMBoard;
import client.WAMClient;

import common.WAMException;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

public class WAMGUI extends Application implements Observer<WAMBoard>{
    // TODO Dade
    private WAMClient client;
    private WAMBoard board;
    private GridPane holes;
    private Label score;

    @Override
    public void init() throws WAMException {
        this.board = new WAMBoard();
        this.client = new WAMClient("localhost", 12345, board);
    }

    @Override
    public void start(Stage stage) throws Exception {

    }

    @Override
    public void update(WAMBoard wamBoard) {

    }

}
