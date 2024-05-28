
package controleurs;

import javafx.scene.image.ImageView;
import utilitaires.*;
import java.io.File;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

public class MorphingSimpleHandler extends MorphingAbstract implements EventHandler<ActionEvent> {
    private PointsControleHandler handler; 

    public MorphingSimpleHandler(TextField champEtapes, TextField champDelai, ImageView imageGauche, PointsControleHandler handler) {
        super(champEtapes, champDelai, imageGauche); 
        this.handler = handler; 
    }

    @Override
    public void handle(ActionEvent event) {
        long tempsDépart = System.currentTimeMillis();
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

        Map<Character, Point> pointsControleDebut = PointsControleHandler.getPointsControleFin();

        // Afficher chaque élément de la map
        for (Map.Entry<Character, Point> entry : pointsControleDebut.entrySet()) {
            Character key = entry.getKey();
            Point value = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + value.toString());
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
        long tempsFin = System.currentTimeMillis();
        
        //calcul temps morphing en secondes 
        double tempsMorphing = (tempsFin - tempsDépart) / 1000.0;
        System.out.println("Temps de morphing : " + tempsMorphing + " s");
        
        //convertie les images en gif
        ConvertisseurGIF convertisseur = new ConvertisseurGIF();
        convertisseur.convertirEnGif(delai);
        handler.handleReset(event);
    }
}
