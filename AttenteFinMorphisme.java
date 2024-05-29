package controleurs;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;


/* Pour l'utiliser il faut juste mettre 
 * 
 * AttenteFinMorphisme.creerFenetre(); dans les EventHandler qui gèrent le lancement du Morphing
 * 
 * AttenteFinMorphisme.fermerFenetre(); à l'endroit ou le morphing est sensé être terminé
 * 
 */


public class AttenteFinMorphisme {


	public static void creerFenetre() {		//Creation de la fenetre "Attente" avec GIF
		
		Stage attendre = new Stage();
		attendre.setTitle("Attente");
		StackPane pane = new StackPane();
		Scene sccene = new Scene(pane);
		
		Image i = new Image("ressources/giphy2.gif");
		ImageView imv = new ImageView(i);
		pane.getChildren().add(imv);
		
		attendre.setScene(sccene);	
		attendre.show();

	}
	
	
	public static void fermerFenetre() {	//Fermeture de la fenetre "Attente" si elle existe
		
		String string = new String("Attente");
        ObservableList<Window> windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof Stage) {
                Stage stage = (Stage) window;
                if (string.equals(stage.getTitle())) {
                    stage.close();
                    break; // Arrete la boucle apres avoir trouve et ferme la fenetre
                }
            }
        }

	}
	
	
	
	
	
}
