package controleurs;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import utilitaires.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class MorphingArrondiHandler implements EventHandler<ActionEvent> {
    private TextField champEtapes;
    private TextField champDelai;
    
    public MorphingArrondiHandler(TextField champEtapes, TextField champDelai) {
        this.champEtapes = champEtapes;
        this.champDelai = champDelai;
        }

    @Override
    public void handle(ActionEvent event) {
        
        int nbEtapes = Integer.parseInt(champEtapes.getText());
        int delai = Integer.parseInt(champDelai.getText());
        
        System.out.println("Nombre d'etapes : " + nbEtapes + ", delai (ms) : " + delai);

        //création dossier pour stocker images (.jpg)
        File dossier = new File("./FormesSimples");
        // Vérifier si le dossier existe
        if (dossier.exists() && dossier.isDirectory()) {
            //suppression et création du dossier
            System.out.println("Le dossier existe.");
            supprimerDossier(dossier);
            dossier.mkdirs();
        } else {
            System.out.println("Le dossier n'existe pas.");
            //création du dossier
            dossier.mkdirs();
        }

        for (int i = 0; i <= nbEtapes; i++) {
            calculEnsemblePointSuivant(nbEtapes);
            Group root = new Group();
            traceCourbeBezier(root, PointsControleHandler.pointsControleDebut);
            Scene scene = new Scene(root, 300, 300, Color.WHITE);

            saveAsImage(scene, "FormesArrondies/morphing_step_" + i + ".jpg");

            try {
                Thread.sleep(delai);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Morphing terminé !");
        try {
            ConvertisseurGIF converter = new ConvertisseurGIF();
            converter.convertirEnGif(delai);
        }
        catch(Exception exceptionGIF){
            System.err.println("Erreur lors de la mise en GIF");
        }
    }

    private void calculEnsemblePointSuivant(int nbEtapes) {
        for (Map.Entry<Character, Point> entry : PointsControleHandler.pointsControleDebut.entrySet()) {
            Character key = entry.getKey();
            Point pointDebut = entry.getValue();
            Point pointFin = PointsControleHandler.pointsControleFin.get(key);
            calculPointSuivant(pointDebut, pointFin, nbEtapes);
        }
    }
    private void calculPointSuivant(Point pointDebut, Point pointFin, int nbEtapes) {
        double diffX = pointFin.getX()-pointDebut.getX();
        double diffY = pointFin.getY()-pointDebut.getY();
        
        if(diffX>= 0) {
        	double ajoutX = diffX/nbEtapes;
        	pointDebut.setX(pointDebut.getX()+ajoutX);
        }
        else {
        	double retraitX = (-diffX)/nbEtapes;
        	pointDebut.setX(pointDebut.getX()-retraitX);
        }
        if(diffY>= 0) {
        	double ajoutY = diffY/nbEtapes;
        	pointDebut.setY(pointDebut.getY()+ajoutY);
        }
        else {
        	double retraitY = (-diffY)/nbEtapes;
        	pointDebut.setY(pointDebut.getY()-retraitY);
        }
    }

    public void traceCourbeBezier(Group root, Map<Character, Point> controlPoints) {
        Character[] keys = controlPoints.keySet().toArray(new Character[0]);
        int numSegments = keys.length / 4;

        for (int i = 0; i < numSegments; i++) {
            Point p0 = controlPoints.get(keys[i * 4]);
            Point p1 = controlPoints.get(keys[i * 4 + 1]);
            Point p2 = controlPoints.get(keys[i * 4 + 2]);
            Point p3 = controlPoints.get(keys[i * 4 + 3]);

            if (p0 == null || p1 == null || p2 == null || p3 == null) {
                System.err.println("Erreur : Un ou plusieurs points de contrôle manquent.");
                return;
            }

            //calculer et tracer les points intermédiaires
            double step = 0.01; //intervalle pour l'échantillonnage
            double prevX = p0.getX();
            double prevY = p0.getY();

            for (double t = step; t <= 1.0; t += step) {
                double x = Math.pow(1 - t, 3) * p0.getX() + 3 * Math.pow(1 - t, 2) * t * p1.getX() + 3 * (1 - t) * Math.pow(t, 2) * p2.getX() + Math.pow(t, 3) * p3.getX();
                double y = Math.pow(1 - t, 3) * p0.getY() + 3 * Math.pow(1 - t, 2) * t * p1.getY() + 3 * (1 - t) * Math.pow(t, 2) * p2.getY() + Math.pow(t, 3) * p3.getY();

                Line line = new Line(prevX, prevY, x, y);
                line.setStroke(Color.BLACK);
                root.getChildren().add(line);

                prevX = x;
                prevY = y;
            }
        }

        //relier le dernier segment au premier pour fermer la forme
        Point firstPoint = controlPoints.get(keys[0]);
        Point lastPoint = controlPoints.get(keys[keys.length - 1]);
        if (firstPoint != null && lastPoint != null) {
            Line closingLine = new Line(lastPoint.getX(), lastPoint.getY(), firstPoint.getX(), firstPoint.getY());
            closingLine.setStroke(Color.BLACK);
            root.getChildren().add(closingLine);
        }
    }

    private void saveAsImage(Scene scene, String fileName) {
        WritableImage image = new WritableImage((int) scene.getWidth(), (int) scene.getHeight());
        scene.snapshot(image);
        //attribution d'un nom unique pour chaque image générée
        String cheminFichier = "./FormesSimples/image_"+System.currentTimeMillis()+".jpg";

        //enregistrement de l'image
        File fichierImage = new File(cheminFichier);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", fichierImage);
            System.out.println("Image enregistrée avec succés.");
        } catch (IOException e) {
            System.out.println("Erreur lors de l'enregistrement de l'image : " + e.getMessage());
        }
    }
    //permet de supprimer un dossier
    public static boolean supprimerDossier(File dossier) {
        if (dossier.isDirectory()) {
            // Récupérer la liste des fichiers et sous-dossiers du dossier
            File[] fichiers = dossier.listFiles();
            if (fichiers != null) {
                for (File fichier : fichiers) {
                    // Récursivement supprimer chaque fichier ou sous-dossier
                    if (!supprimerDossier(fichier)) {
                        return false; // Arrêter si la suppression échoue pour l'un des fichiers
                    }
                }
            }
        }
        // Supprimer le dossier lui-même après avoir supprimé son contenu
        return dossier.delete();
    }
}