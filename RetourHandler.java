package controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe RetourHandler
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class RetourHandler implements EventHandler<ActionEvent> {
    private Stage primaryStage;
    private Scene mainMenuScene;

    public RetourHandler(Stage primaryStage, Scene mainMenuScene) {
        this.primaryStage = primaryStage;
        this.mainMenuScene = mainMenuScene;
    }

    @Override
    public void handle(ActionEvent event) {
        primaryStage.setScene(mainMenuScene);
    }
}
