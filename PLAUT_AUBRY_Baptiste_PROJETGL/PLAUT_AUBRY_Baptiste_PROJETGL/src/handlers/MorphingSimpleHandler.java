package handlers;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

public class MorphingSimpleHandler implements EventHandler<ActionEvent> {
    private TextField champEtapes;
    private TextField champDelai;

    public MorphingSimpleHandler(TextField champEtapes, TextField champDelai) {
        this.champEtapes = champEtapes;
        this.champDelai = champDelai;
    }

    @Override
    public void handle(ActionEvent event) {
        int nbEtapes = Integer.parseInt(champEtapes.getText());
        int delai = Integer.parseInt(champDelai.getText());
        try {
        	ConvertisseurGIF convertisseur = new ConvertisseurGIF();
            convertisseur.convertirEnGif(delai);
        }
        catch(Exception gifException){
        	System.out.println("Erreur GIF Ferme, ou erreur de conversion");
        }
        System.out.println("Nombre d'etapes : " + nbEtapes + ", delai (ms) : " + delai);

        // Ajoutez ici votre méthode de morphing simple
    }
}
