package controleurs;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import utilitaires.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class MorphingArrondiHandler extends MorphingAbstract implements EventHandler<ActionEvent> {
    private ImageView imageGauche;
    private String imagePath;
    private PointsControleHandler handler;

    public MorphingArrondiHandler(TextField champEtapes, TextField champDelai, ImageView imageGauche, PointsControleHandler handler) {
        super(champEtapes, champDelai); 
        this.imageGauche= imageGauche;
        this.handler = handler;
    }

    @Override
    public void handle(ActionEvent event) {
        
        int nbEtapes = Integer.parseInt(getChampEtapes().getText());
        int delai = Integer.parseInt(getChampDelai().getText());
        
        System.out.println("Nombre d'etapes : " + nbEtapes + ", delai (ms) : " + delai);

        dossierFormeSimples(); 
        javafx.scene.image.Image image = imageGauche.getImage();

        //si image non nulle, recupere le chemin de l'image et le stocke (en enlevant le début de la chaine 'file:\\'
        if (image != null) {
            String imagePath = image.getUrl();
            
            File file = new File(imagePath);
            String cheminImage = file.getPath();
            
            this.imagePath = cheminImage.substring("file:\\".length());
        }

        //creer le tableau de pixel de l'image et unifie son fond et le stocke
        ImageM imageFondModifie = modifFondImage(new ImageM(imagePath));
        

        //colore les points de contrôle de début, trace les droites entre ceux-ci et colore
        colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleDebut());
        Map<Character, Point> pointsCalcules = new HashMap<>(); 
        //boucle tant qu'on a pas atteint le nombre d'etapes demande
        while(nbEtapes>0) {
        	calculEnsemblePointSuivant(nbEtapes);
        	modifFondImage(imageFondModifie);
            pointsCalcules = traceCourbeBezier(PointsControleHandler.getPointsControleDebut()); 
        	colorPointsDeControle(imageFondModifie, pointsCalcules);
        	nbEtapes-=1;
        }

        System.out.println("Morphing terminé !");
        try {
            ConvertisseurGIF converter = new ConvertisseurGIF();
            converter.convertirEnGif(delai);
        }
        catch(Exception exceptionGIF){
            System.err.println("Erreur lors de la mise en GIF");
        }
        handler.handleReset(event);
    }

    public Map<Character, Point> traceCourbeBezier(Map<Character, Point> controlPoints) {
        Map<Character, Point> pointsCalcules = new HashMap<>(); 
        Character lettre = 'A';

        Character[] keys = controlPoints.keySet().toArray(new Character[0]);
        int numSegments = keys.length / 4;

        for (int i = 0; i < numSegments; i++) {
            Point p0 = controlPoints.get(keys[i * 4]);
            Point p1 = controlPoints.get(keys[i * 4 + 1]);
            Point p2 = controlPoints.get(keys[i * 4 + 2]);
            Point p3 = controlPoints.get(keys[i * 4 + 3]);

            //calculer et tracer les points intermédiaires
            double step = 0.01; //intervalle pour l'échantillonnage

            for (double t = step; t <= 1.0; t += step) {
                double x = Math.pow(1 - t, 3) * p0.getX() + 3 * Math.pow(1 - t, 2) * t * p1.getX() + 3 * (1 - t) * Math.pow(t, 2) * p2.getX() + Math.pow(t, 3) * p3.getX();
                double y = Math.pow(1 - t, 3) * p0.getY() + 3 * Math.pow(1 - t, 2) * t * p1.getY() + 3 * (1 - t) * Math.pow(t, 2) * p2.getY() + Math.pow(t, 3) * p3.getY();
                pointsCalcules.put(lettre++, new Point(x, y));
            }
        }
        return pointsCalcules; 
    }
}