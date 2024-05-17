
package controleurs;

import javafx.scene.image.ImageView;
import java.util.Map;
import utilitaires.*;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

public class MorphingSimpleHandler implements EventHandler<ActionEvent> {
    private TextField champEtapes;
    private TextField champDelai;
    private ImageView imageGauche;
    private String imagePath;
    
    public MorphingSimpleHandler(TextField champEtapes, TextField champDelai, ImageView imageGauche) {
        this.champEtapes = champEtapes;
        this.champDelai = champDelai;
        this.imageGauche= imageGauche;
        }

    @Override
    public void handle(ActionEvent event) {

        int nbEtapes = Integer.parseInt(champEtapes.getText());
        int delai = Integer.parseInt(champDelai.getText());
        
        videDossierFormeSimples();
        
        javafx.scene.image.Image image = imageGauche.getImage();
        if (image != null) {
            String imagePath = image.getUrl();
            
            File file = new File(imagePath);
            String cheminImage = file.getPath();
            
            this.imagePath = cheminImage.substring("file:\\".length());
        }
                
        ImageM imageFondModifie = modifFondImage(new ImageM(imagePath));
        
        while(nbEtapes>=0) {
        	calculEnsemblePointSuivant(nbEtapes);
        	colorPointsDeControle(imageFondModifie);
        	modifFondImage(imageFondModifie);
        	nbEtapes-=1;
        }
        ConvertisseurGIF convertisseur = new ConvertisseurGIF();
        convertisseur.convertirEnGif(delai);
        
    }
    private void videDossierFormeSimples() {
    	String directoryPath = "./FormesSimples";
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] contents = directory.listFiles();
            if (contents != null) {
                for (File file : contents) {
                    file.delete(); // Supprime le fichier ou le dossier
                }
            }
        }
    }
    private void calculEnsemblePointSuivant(int nbEtapes) {
    	
    	for (Map.Entry<Character, Point> entry : PointsControleHandler.pointsControleDebut.entrySet()) {
        	Character key = entry.getKey();
            Point pointDebut = entry.getValue();
            Point pointFin = PointsControleHandler.pointsControleFin.get(key);
            
            //System.out.println(key+" : ("+pointDebut.getX()+","+pointDebut.getY()+")");
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
    private ImageM modifFondImage(ImageM imageMGauche) {
        
        for (int y = 0; y < imageMGauche.getLargeur(); y++) {
            for (int x = 0; x < imageMGauche.getHauteur(); x++) {
                
            	imageMGauche.tab[x][y].setR(255);
            	imageMGauche.tab[x][y].setV(255);
            	imageMGauche.tab[x][y].setB(255);
                //System.out.println("Pixel (" + x + "," + y + ")");
            }
        }
        return imageMGauche;
    }
    private void colorPointsDeControle(ImageM imageMGauche) {
    	ImageM imageModifiee = new ImageM(imageMGauche.tab);
    	
    	for (Map.Entry<Character, Point> entry : PointsControleHandler.pointsControleDebut.entrySet()) {
        	Character key = entry.getKey();
            Point pointDebut = entry.getValue();
            Point pointFin = PointsControleHandler.pointsControleFin.get((char) (key + 1));
            
            imageMGauche.tab[(int) pointDebut.getX()][(int) pointDebut.getY()].setR(0);
            imageMGauche.tab[(int) pointDebut.getX()][(int) pointDebut.getY()].setV(0);
            imageMGauche.tab[(int) pointDebut.getX()][(int) pointDebut.getY()].setB(0);
            
            System.out.println(key+" ("+pointDebut.getX()+","+pointDebut.getY()+") ->  "+(char) (key+1));
            if(PointsControleHandler.pointsControleFin.get((char) (key + 1)) == null) {
            	
            	Map<Character, Point> map = PointsControleHandler.pointsControleDebut;
            	Point premiereValeur = map.get('A');
            	System.out.println(((char) (key + 1))+" n'a pas de valeur dans la map, il faut donc utiliser "+"A -> ("+premiereValeur.getX());
            }
        }
    	    	    	
        String outputPath = "./FormesSimples/image_+"+System.currentTimeMillis()+".jpg";
        imageModifiee.saveImage(outputPath);
    }
}