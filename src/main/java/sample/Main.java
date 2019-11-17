package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * the entry of the game
 */
public class Main extends Application {
    /**
     * start the javaFx application by showing the scene
     * @param primaryStage the stage of the game
     * @throws Exception unexpected behaviour
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Tower Defence");
        primaryStage.setScene(new Scene(root, 600, 480));
        primaryStage.show();
        MyController appController = loader.getController();
        appController.createArena();
    }

    /**
     * the main function
     * @param args the programme argument
     */
    public static void main(String[] args) {
        launch(args);
    }
}
