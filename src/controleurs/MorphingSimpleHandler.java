
package controleurs;

import javafx.scene.image.ImageView;
import java.util.Map;
import utilitaires.*;

import java.io.File;

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
        
        
        colorPointsDeControle(imageFondModifie);
        while(nbEtapes>0) {
        	calculEnsemblePointSuivant(nbEtapes);
        	modifFondImage(imageFondModifie);
        	colorPointsDeControle(imageFondModifie);
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
            }
        }
        return imageMGauche;
    }
    private void colorPointsDeControle(ImageM imageMGauche) {
    	ImageM imageModifiee = new ImageM(imageMGauche.tab);
    	
    	Point pointDepart = null;
    	int tailleMap = PointsControleHandler.pointsControleDebut.size();
    	int pointEnCours = 1;
    	    	
    	for (Map.Entry<Character, Point> entry : PointsControleHandler.pointsControleDebut.entrySet()) {
        	Character key = entry.getKey();
            Point pointDebut = entry.getValue();
            
            //colore le point de contrôle en noir
            imageMGauche.tab[(int) pointDebut.getX()][(int) pointDebut.getY()].setR(0);
            imageMGauche.tab[(int) pointDebut.getX()][(int) pointDebut.getY()].setV(0);
            imageMGauche.tab[(int) pointDebut.getX()][(int) pointDebut.getY()].setB(0);
            
            if(key == 'A') {
            	pointDepart = pointDebut;
            }
            
            if(pointEnCours < tailleMap) {
            	Character cleSuivante = (char) (key + 1);
                Point pointSuivant = PointsControleHandler.pointsControleDebut.get(cleSuivante);
                
                drawLine(imageMGauche, pointDebut, pointSuivant);
            }else {
            	drawLine(imageMGauche, pointDebut, pointDepart);
            }
            
            pointEnCours += 1;
        }
    	   	
    	
        remplirForme(imageMGauche);
    	
        String outputPath = "./FormesSimples/image_+"+System.currentTimeMillis()+".jpg";
        imageModifiee.saveImage(outputPath);
    }
	private void drawLine(ImageM image, Point depart, Point arrivee) {
    	int x0 = (int) depart.getX();
        int y0 = (int) depart.getY();
        int x1 = (int) arrivee.getX();
        int y1 = (int) arrivee.getY();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (x0 != x1 || y0 != y1) {
            // Colorer le pixel ici
            image.tab[x0][y0].setR(0);
            image.tab[x0][y0].setV(0);
            image.tab[x0][y0].setB(0);

            int err2 = 2 * err;
            if (err2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (err2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }
	private void remplirForme(ImageM image) {
    	
    	for (int y = 0; y < image.getHauteur()-1; y++) {
            for (int x = 0; x < image.getLargeur()-1; x++) {
            	//si pixel noir, color la case a droite
                if ((image.tab[x][y].getR() == 0) && (image.tab[x][y].getV() == 0) && (image.tab[x][y].getB() == 0))  {
                	if ((image.tab[x+1][y].getR() == 255) && (image.tab[x+1][y].getV() == 255) && (image.tab[x+1][y].getB() == 255)) {
                		image.tab[x+1][y].setR(0);
                		image.tab[x+1][y].setV(255);
                		image.tab[x+1][y].setB(0);
                    }
                	if ((image.tab[x-1][y].getR() == 0) && (image.tab[x-1][y].getV() == 255) && (image.tab[x-1][y].getB() == 0)
                			&& (image.tab[x+1][y].getR() == 0) && (image.tab[x+1][y].getV() == 255) && (image.tab[x+1][y].getB() == 0)) {
                		image.tab[x+1][y].setR(255);
                		image.tab[x+1][y].setV(255);
                		image.tab[x+1][y].setB(255);
                    }
                }
                //pixel vert mais au dessus c'est blanc
                if ((image.tab[x][y].getR() == 0) && (image.tab[x][y].getV() == 255) && (image.tab[x][y].getB() == 0)) {
                	if ((image.tab[x][y-1].getR() == 255) && (image.tab[x][y-1].getV() == 255) && (image.tab[x][y-1].getB() == 255)) {
                		image.tab[x][y].setR(255);
                		image.tab[x][y].setV(255);
                		image.tab[x][y].setB(255);
                    }
                }
                //si pixel vert, color la case de droite
                if ((image.tab[x][y].getR() == 0) && (image.tab[x][y].getV() == 255) && (image.tab[x][y].getB() == 0)) {
                	//si case blanche a droite
                	if ((image.tab[x+1][y].getR() == 255) && (image.tab[x+1][y].getV() == 255) && (image.tab[x+1][y].getB() == 255)) {
                		image.tab[x+1][y].setR(0);
                		image.tab[x+1][y].setV(255);
                		image.tab[x+1][y].setB(0);
                    }
                }
                /*if ((image.tab[x][y].getR() == 0) && (image.tab[x][y].getV() == 255) && (image.tab[x][y].getB() == 0)) {
                	if ((image.tab[x-1][y].getR() == 0) && (image.tab[x-1][y].getV() == 0) && (image.tab[x-1][y].getB() == 0)) {
                		image.tab[x+1][y].setR(255);
                		image.tab[x+1][y].setV(0);
                		image.tab[x+1][y].setB(0);
                    }
                }*/
            }
        }
    }

}