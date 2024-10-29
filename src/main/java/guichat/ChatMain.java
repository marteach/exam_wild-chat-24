package guichat;


import javafx.application.Application;

import controllers.MainWindowController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Entry point for the chat application. Loads the first view onto the stage and gets everything going.
 * 
 * @author Martin Goblirsch
 */
public class ChatMain extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader rootView = new FXMLLoader(getClass().getResource("/fxml/main_window.fxml"));

        Scene scene = new Scene(rootView.load());
        stage.setScene(scene);

        stage.setOnCloseRequest((WindowEvent e) -> {
            Platform.exit();
            System.exit(0);
        });

        // Gives you the possibility to reach the active controller class to do setup if you want.
        MainWindowController activeViewController = rootView.getController();
        //activeViewController.doSomeThing()

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
