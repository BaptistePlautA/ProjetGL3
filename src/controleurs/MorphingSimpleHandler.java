
package controleurs;

import javafx.scene.image.ImageView;
import utilitaires.*;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import java.util.List;

public class MorphingSimpleHandler extends MorphingAbstract implements EventHandler<ActionEvent> {
	private PointsControleHandler handler;
    
    public MorphingSimpleHandler(TextField champEtapes, TextField champDelai, ImageView imageGauche, PointsControleHandler handler) {
        super(champEtapes, champDelai, imageGauche);
        this.handler = handler;
    }

    @Override
    public void handle(ActionEvent event) {

        int nbEtapes = Integer.parseInt(getChampEtapes().getText());
        int delai = Integer.parseInt(getChampDelai().getText());
        
        dossierFormeSimples();
        
        javafx.scene.image.Image image = getImageGauche().getImage();
        
        if (image != null) {
            String imagePath = image.getUrl();
            
            File file = new File(imagePath);
            String cheminImage = file.getPath();
        
            setImagePath(cheminImage.substring("file:\\".length()));
        }
        
        ImageM imageBase = new ImageM(getImagePath());
        
        //obtient le nombre de couleurs de l'image
        List<int[]> listeCouleur = getNombreCouleur(imageBase);
        
        //colore la forme en fonction du nombre de couleurs qu'elle contient
        colorFormeComplet(nbEtapes, imageBase, listeCouleur, null);
               
        //convertie les images en gif
        ConvertisseurGIF convertisseur = new ConvertisseurGIF();
        convertisseur.convertirEnGif(delai);
        handler.handleReset(event);
    }
}