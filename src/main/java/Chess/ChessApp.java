package Chess;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ChessApp extends Application{

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Sjakk-link");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("ChessApp.fxml"))));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
