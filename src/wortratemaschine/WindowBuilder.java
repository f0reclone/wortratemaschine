package wortratemaschine;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Applikation, welche ein Wort errät und dabei die Anzahl der Versuche und die
 * Zeit anzeigt.
 * 
 * @author Andreas, Christoph
 */
public class WindowBuilder extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Wortratemaschine");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                FXMLDocumentController.shouldRun = false;
                Platform.exit();
                System.exit(0);
            }
        });
        stage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
