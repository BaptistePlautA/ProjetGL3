
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
        
        //vide Dossier
        videDossierFormeSimples();
        
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
        colorPointsDeControle(imageFondModifie);
        
        //boucle tant qu'on a pas atteint le nombre d'etapes demande
        while(nbEtapes>0) {
        	calculEnsemblePointSuivant(nbEtapes);
        	modifFondImage(imageFondModifie);
        	colorPointsDeControle(imageFondModifie);
        	nbEtapes-=1;
        }
        
        //convertie les images en gif
        ConvertisseurGIF convertisseur = new ConvertisseurGIF();
        convertisseur.convertirEnGif(delai);
        
    }
    
    public void videDossierFormeSimples() {
    	String directoryPath = "./FormesSimples";
        File directory = new File(directoryPath);
        
        //si repertoire trouve, supprime tous les fichiers contenu dans ce repertoire
        if (directory.exists() && directory.isDirectory()) {
            File[] contents = directory.listFiles();
            if (contents != null) {
                for (File file : contents) {
                    file.delete(); // Supprime le fichier ou le dossier
                }
            }
        }
    }
    
    public void calculEnsemblePointSuivant(int nbEtapes) {
    	
    	//boucle qui calcule pour chaque point de controle, la position de ce point a l'etape +1
    	for (Map.Entry<Character, Point> entry : PointsControleHandler.getPointsControleDebut().entrySet()) {
        	Character key = entry.getKey();
            Point pointDebut = entry.getValue();
            Point pointFin = PointsControleHandler.getPointsControleFin().get(key);
            
            calculPointSuivant(pointDebut, pointFin, nbEtapes);
        }
    }
    
    public void calculPointSuivant(Point pointDebut, Point pointFin, int nbEtapes) {
    	
    	//calcule les differences d'abscisses et d'ordonnes
    	double diffX = pointFin.getX()-pointDebut.getX();
        double diffY = pointFin.getY()-pointDebut.getY();
        
        //gestion des valeurs d'ajout/retrait pour atteindre les points suivants (en gros atteindre l'etape suivante)
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
    
    public ImageM modifFondImage(ImageM imageMGauche) {
        
    	//boucle sur tous les pixels de l'image pour les rendre blancs
        for (int y = 0; y < imageMGauche.getLargeur(); y++) {
            for (int x = 0; x < imageMGauche.getHauteur(); x++) {
                
            	imageMGauche.tab[x][y].setR(255);
            	imageMGauche.tab[x][y].setV(255);
            	imageMGauche.tab[x][y].setB(255);
            }
        }
        return imageMGauche;
    }
    
    public void colorPointsDeControle(ImageM imageMGauche) {
    	
    	ImageM imageModifiee = new ImageM(imageMGauche.tab);
    	
    	Point pointDepart = null;
    	int tailleMap = PointsControleHandler.getPointsControleDebut().size();
    	int pointEnCours = 1;
    	
    	//boucle sur les valeurs d'entree de la map 'pointsControleDebut' pour colorer chaque point en noir
    	for (Map.Entry<Character, Point> entry : PointsControleHandler.getPointsControleDebut().entrySet()) {
        	Character key = entry.getKey();
            Point pointDebut = entry.getValue();
            
            //colore le point de contrôle en noir
            imageMGauche.tab[(int) pointDebut.getX()][(int) pointDebut.getY()].setR(0);
            imageMGauche.tab[(int) pointDebut.getX()][(int) pointDebut.getY()].setV(0);
            imageMGauche.tab[(int) pointDebut.getX()][(int) pointDebut.getY()].setB(0);
            
            //stocke le premier point de la map
            if(key == 'A') {
            	pointDepart = pointDebut;
            }
            
            //tant qu'on atteint pas la fin de la map, je lie le point a son suivant (tracage de la ligne entre n et n+1)
            if(pointEnCours < tailleMap) {
            	Character cleSuivante = (char) (key + 1);
                Point pointSuivant = PointsControleHandler.getPointsControleDebut().get(cleSuivante);
                
                drawLine(imageMGauche, pointDebut, pointSuivant);
            }
            //atteinte de la fin de la map, lie le dernier point au premier (pour fermer la forme)
            else {
            	drawLine(imageMGauche, pointDebut, pointDepart);
            }
            
            pointEnCours += 1;
        }
    	   	
    	
        remplirForme(imageMGauche);
    	
        String outputPath = "./FormesSimples/image_+"+System.currentTimeMillis()+".jpg";
        imageModifiee.saveImage(outputPath);
    }
    public void drawLine(ImageM image, Point depart, Point arrivee) {
    	
    	//algorithme de Bresenham
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
    public void remplirForme(ImageM image) {

    	//boucle pour colorer en vert tous les pixels se situants a droite d'un pixel noir, et de colorer en blanc tous les pixels verts situes sous un pixel blanc
    	for (int y = 0; y < image.getHauteur()-1; y++) {
            for (int x = 0; x < image.getLargeur()-1; x++) {
            	//si pixel est noir et case a droite est blanche, colore la case de droite en vert
                if ((image.tab[x][y].getR() == 0) && (image.tab[x][y].getV() == 0) && (image.tab[x][y].getB() == 0))  {
                	if ((image.tab[x+1][y].getR() == 255) && (image.tab[x+1][y].getV() == 255) && (image.tab[x+1][y].getB() == 255)) {
                		image.tab[x+1][y].setR(0);
                		image.tab[x+1][y].setV(255);
                		image.tab[x+1][y].setB(0);
                    }
                }
                //si pixel est vert et pixel du dessus est blanc, colore la case en blanc
                if ((image.tab[x][y].getR() == 0) && (image.tab[x][y].getV() == 255) && (image.tab[x][y].getB() == 0)) {
                	if ((image.tab[x][y-1].getR() == 255) && (image.tab[x][y-1].getV() == 255) && (image.tab[x][y-1].getB() == 255)) {
                		image.tab[x][y].setR(255);
                		image.tab[x][y].setV(255);
                		image.tab[x][y].setB(255);
                    }
                }
                //si pixel vert et pixel de droite est blanc, colore la case de droite en vert
                if ((image.tab[x][y].getR() == 0) && (image.tab[x][y].getV() == 255) && (image.tab[x][y].getB() == 0)) {
                	if ((image.tab[x+1][y].getR() == 255) && (image.tab[x+1][y].getV() == 255) && (image.tab[x+1][y].getB() == 255)) {
                		image.tab[x+1][y].setR(0);
                		image.tab[x+1][y].setV(255);
                		image.tab[x+1][y].setB(0);
                    }
                }
            }
        }
    	
    	//boucle pour colorer en blanc tous les pixels verts situes au dessus/a gauche d'une case dont le pixel est blanc
    	for (int y = image.getHauteur() - 1; y >= 0; y--) {
    	    for (int x = image.getLargeur() - 1; x >= 0; x--) {
    	    	//si pixel est vert et pixel de droite/dessous est blanc, colore la case en blanc
    	        if ((image.tab[x][y].getR() == 0) && (image.tab[x][y].getV() == 255) && (image.tab[x][y].getB() == 0)) {
    	            if (x + 1 < image.getLargeur() && (image.tab[x + 1][y].getR() == 255) && (image.tab[x + 1][y].getV() == 255) && (image.tab[x + 1][y].getB() == 255)) {
    	                image.tab[x][y].setR(255);
    	                image.tab[x][y].setV(255);
    	                image.tab[x][y].setB(255);
    	            }
    	            if (y - 1 >= 0 && (image.tab[x][y + 1].getR() == 255) && (image.tab[x][y + 1].getV() == 255) && (image.tab[x][y + 1].getB() == 255)) {
    	                image.tab[x][y].setR(255);
    	                image.tab[x][y].setV(255);
    	                image.tab[x][y].setB(255);
    	            }
    	        }
    	        //si pixel est noir, le colore en vert
    	        if ((image.tab[x][y].getR() == 0) && (image.tab[x][y].getV() == 0) && (image.tab[x][y].getB() == 0)) {
	                image.tab[x][y].setR(0);
	                image.tab[x][y].setV(255);
	                image.tab[x][y].setB(0);
	            }
    	    }
        }
	}


}