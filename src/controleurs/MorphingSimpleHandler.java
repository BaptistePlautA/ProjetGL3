
package controleurs;

import javafx.scene.image.ImageView;
import utilitaires.*;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

public class MorphingSimpleHandler extends MorphingAbstract implements EventHandler<ActionEvent> {
    
    public MorphingSimpleHandler(TextField champEtapes, TextField champDelai, ImageView imageGauche, PointsControleHandler handler) {
        super(champEtapes, champDelai, imageGauche, handler); 
    }

    @Override
    public void handle(ActionEvent event) {

        int nbEtapes = Integer.parseInt(getChampEtapes().getText());
        int delai = Integer.parseInt(getChampDelai().getText());
        
        dossierFormeSimples();
        
        javafx.scene.image.Image image = getImageGauche().getImage();
        
        //si image non nulle, recupere le chemin de l'image et le stocke (en enlevant le début de la chaine 'file:\\'
        if (image != null) {
            String imagePath = image.getUrl();
            
            File file = new File(imagePath);
            String cheminImage = file.getPath();
        
            setImagePath(cheminImage.substring("file:\\".length()));
        }
        
        //creer le tableau de pixel de l'image et unifie son fond et le stocke
        ImageM imageFondModifie = modifFondImage(new ImageM(getImagePath()));
        
        //colore les points de contrôle de début, trace les droites entre ceux-ci et colore
        colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleDebut());
        
        //boucle tant qu'on a pas atteint le nombre d'etapes demande
        while(nbEtapes>0) {
        	calculEnsemblePointSuivant(nbEtapes);
        	modifFondImage(imageFondModifie);
        	colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleDebut());
        	nbEtapes-=1;
        }
        
        //convertie les images en gif
        ConvertisseurGIF convertisseur = new ConvertisseurGIF();
        convertisseur.convertirEnGif(delai);
        getHandler().handleReset(event);
    }
}