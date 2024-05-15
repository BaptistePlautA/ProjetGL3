package handlers;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
