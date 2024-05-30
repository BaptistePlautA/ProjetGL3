package controleurs;

import utilitaires.*;

import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

import java.io.File;

import java.util.List;

/**
 * Classe MorphingSimpleHandler
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class MorphingSimpleHandler extends MorphingAbstract implements EventHandler<ActionEvent> {
    private PointsControleHandler controleur; 

    public MorphingSimpleHandler(TextField champEtapes, TextField champDelai, ImageView imageGauche, PointsControleHandler controleur) {
        super(champEtapes, champDelai, imageGauche); 
        this.controleur = controleur; 
    }

    @Override
    public void handle(ActionEvent event) {
        long tempsDépart = System.currentTimeMillis();
        int nbEtapes = Integer.parseInt(getChampEtapes().getText());
        int delai = Integer.parseInt(getChampDelai().getText());
        
        dossierFormeSimples();
        
        javafx.scene.image.Image image = getImageGauche().getImage();
        
        //si image non nulle, récupère le chemin de l'image et le stocke (en enlevant le début de la chaine 'file:\\'
        if (image != null) {
            String imagePath = image.getUrl();
            
            File file = new File(imagePath);
            String cheminImage = file.getPath();
        
            setImagePath(cheminImage.substring("file:\\".length()));
        }

        ImageM imageBase = new ImageM(getImagePath()); 
        List<int[]> listeCouleur = getNombreCouleur(imageBase); 
        colorFormeComplet(nbEtapes, imageBase, listeCouleur, null); 
        
        //calcul temps morphing en secondes 
        long tempsFin = System.currentTimeMillis();
        double tempsMorphing = (tempsFin - tempsDépart) / 1000.0;
        System.out.println("Temps de morphing : " + tempsMorphing + " s");
        
        //convertie les images en gif
        ConvertisseurGIF convertisseur = new ConvertisseurGIF();
        convertisseur.convertirEnGif(delai,  "./Formes");
        controleur.handleReset(event);
    }
}