import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CompteurCI extends Application{
    
    private Button b1 ;
    private Button b2 ;
    private Button b3 ;

    @Override
    public void start(Stage primaryStage) throws Exception {
        
    b1 = new Button("0");
    b1.setOnAction(new MyBtnHandler());
    b2 = new Button("0");
    b2.setOnAction(new MyBtnHandler());
    b3 = new Button("0");
    b3.setOnAction(new MyBtnHandler());
    
    HBox hbox = new HBox(3);
    
    hbox.getChildren().add(b1);
    hbox.getChildren().add(b2);
    hbox.getChildren().add(b3);
    
    Scene scene = new Scene(hbox);
    primaryStage.setScene(scene);
    primaryStage.sizeToScene();
    primaryStage.show();

    }
    class MyBtnHandler implements EventHandler<ActionEvent>{
        int compteur1 = 0;
        int compteur2 = 0;
        int compteur3 = 0;
        @Override
        public void handle(ActionEvent event) {
            Button numeroBouton = (Button) event.getSource();
        
            if(numeroBouton == b1) {
                compteur1++;
                b1.setText(""+compteur1);
            } else if (numeroBouton == b2) {
                compteur2++;
                b2.setText(""+compteur2);
            } else {
                compteur3++;
                b3.setText(""+compteur3);
            }
        }
    }
    public static void main (String[] args) {
        launch (args);
    }
}

