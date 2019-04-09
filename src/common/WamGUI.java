package common;

import client.gui.WAMBoard;
import javafx.application.Application;
import javafx.stage.Stage;

public class WamGUI extends Application implements Observer<WAMBoard> {

    @Override
    public void start(Stage stage) throws Exception {

    }

    public static void main(String[] args){
        if (args.length != 2) {
            System.out.println("Usage: java ConnectFourGUI host port");
            System.exit(-1);
        } else {
            Application.launch(args);
        }
    }

    @Override
    public void update(WAMBoard wamBoard) {

    }
}
