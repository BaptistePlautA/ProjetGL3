package utilitaires;

import java.io.File;
import java.util.Map;

import controleurs.PointsControleHandler;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public abstract class MorphingAbstract {

    private TextField champEtapes;
    private TextField champDelai;
    private ImageView imageGauche;
    private String imagePath;
    private PointsControleHandler handler;

    public MorphingAbstract(TextField champEtapes, TextField champDelai,ImageView imageGauche, PointsControleHandler handler) {
        this.champEtapes = champEtapes;
        this.champDelai = champDelai;
        this.imageGauche= imageGauche;
        this.handler = handler;
    }

    public TextField getChampEtapes(){
        return champEtapes; 
    }

    public TextField getChampDelai(){
        return champDelai; 
    }

    public ImageView getImageGauche(){
        return imageGauche; 
    }

    public String getImagePath(){
        return imagePath; 
    }

    public PointsControleHandler getHandler(){
        return handler; 
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath; 
    }
    
    protected void calculEnsemblePointSuivant(int nbEtapes) {
        for (Map.Entry<Character, Point> entry : PointsControleHandler.getPointsControleDebut().entrySet()) {
            Character key = entry.getKey();
            Point pointDebut = entry.getValue();
            Point pointFin = PointsControleHandler.getPointsControleFin().get(key);
            calculPointSuivant(pointDebut, pointFin, nbEtapes);
        }
    }

    protected void calculPointSuivant(Point pointDebut, Point pointFin, int nbEtapes) {
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

    protected void dossierFormeSimples(){
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
            System.out.println("Le dossier a été créé");
        }
    }
    
    //permet de supprimer un dossier
    protected boolean supprimerDossier(File dossier) {
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

    public ImageM modifFondImage(ImageM imageMGauche, int[] pixelFond) {
        Pixel[][] tab = imageMGauche.getTab();
        
        int[] pixelGlobal = {255, 255, 255};
        
        if(pixelFond != null) {
        	pixelGlobal = pixelFond;
		}
        
    	//boucle sur tous les pixels de l'image pour les rendre blancs
        for (int y = 0; y < imageMGauche.getLargeur(); y++) {
            for (int x = 0; x < imageMGauche.getHauteur(); x++) {
                
            	tab[x][y].setR(pixelGlobal[0]);
            	tab[x][y].setV(pixelGlobal[1]);
            	tab[x][y].setB(pixelGlobal[2]);
            }
        }
        return imageMGauche;
    }

    public void colorPointsDeControle(ImageM imageMGauche, Map<Character, Point> pointsCalcules, int[] premierPixel, int[] secondPixel) {
    	Pixel[][] tab = imageMGauche.getTab();
    	ImageM imageModifiee = new ImageM(tab);
    	
    	Point pointDepart = null;
    	int tailleMap = pointsCalcules.size();
    	int pointEnCours = 1;
    	
    	
    	int[] pixelFond = {255, 255, 255};
    	int[] pixelForme = {0, 255, 0};
    	if(premierPixel != null){
    		pixelFond = premierPixel;
    	}else {
        	System.out.println("fond est inexistant");
    	}
    	if(secondPixel != null) {
			pixelForme = secondPixel;
		}
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	//boucle sur les valeurs d'entree de la map 'pointsControleDebut' pour colorer chaque point en noir
    	for (Map.Entry<Character, Point> entry : pointsCalcules.entrySet()) {
        	Character key = entry.getKey();
            Point pointDebut = entry.getValue();
            
            //colore le point de contrôle en noir
            tab[(int) pointDebut.getX()][(int) pointDebut.getY()].setR(0);
            tab[(int) pointDebut.getX()][(int) pointDebut.getY()].setV(0);
            tab[(int) pointDebut.getX()][(int) pointDebut.getY()].setB(0);
            
            //stocke le premier point de la map
            if(key == 'A') {
            	pointDepart = pointDebut;
            }
            
            //tant qu'on atteint pas la fin de la map, je lie le point a son suivant (tracage de la ligne entre n et n+1)
            if(pointEnCours < tailleMap) {
            	Character cleSuivante = (char) (key + 1);
                Point pointSuivant = pointsCalcules.get(cleSuivante);
                
                drawLine(imageMGauche, pointDebut, pointSuivant);
            }
            //atteinte de la fin de la map, lie le dernier point au premier (pour fermer la forme)
            else {
            	drawLine(imageMGauche, pointDebut, pointDepart);
            }
            
            pointEnCours += 1;
        }
    	
    	
    	
    	
    	
    	
    	
    	
    	
        remplirForme(imageMGauche, pixelFond, pixelForme);
        
        
        
        
        
        
        
        
        
        
        
        
        
    	
        String outputPath = "./FormesSimples/image_+"+System.currentTimeMillis()+".jpg";
        imageModifiee.saveImage(outputPath);
    }

    public void drawLine(ImageM image, Point depart, Point arrivee) {
    	Pixel[][] tab = image.getTab();
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
            tab[x0][y0].setR(0);
            tab[x0][y0].setV(0);
            tab[x0][y0].setB(0);

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

    public void remplirForme(ImageM image, int[] pixelFond, int[] pixelForme) {
    	
        Pixel[][] tab = image.getTab();
        boolean fondNoir = false;
        
        if((pixelFond[0] == 0) && (pixelFond[1] == 0) && (pixelFond[2] == 0)) {
        	fondNoir = true;
        	pixelFond[0] = 255;
        	pixelFond[1] = 255;
        	pixelFond[2] = 255;
        }

    	//boucle pour colorer en vert tous les pixels se situants a droite d'un pixel noir, et de colorer en blanc tous les pixels verts situes sous un pixel blanc
    	for (int y = 0; y < image.getHauteur()-1; y++) {
            for (int x = 0; x < image.getLargeur()-1; x++) {
            	//si pixel est noir et case a droite est blanche, colore la case de droite en vert
                if ((tab[x][y].getR() == 0) && (tab[x][y].getV() == 0) && (tab[x][y].getB() == 0) && (x<image.getLargeur()-1))  {
                	if ((tab[x+1][y].getR() == pixelFond[0]) && (tab[x+1][y].getV() == pixelFond[1]) && (tab[x+1][y].getB() == pixelFond[2])) {
                		tab[x+1][y].setR(pixelForme[0]);
                		tab[x+1][y].setV(pixelForme[1]);
                		tab[x+1][y].setB(pixelForme[2]);
                    }
                }
                //si pixel est vert et pixel du dessus est blanc, colore la case en blanc
                if ((tab[x][y].getR() == pixelForme[0]) && (tab[x][y].getV() == pixelForme[1]) && (tab[x][y].getB() == pixelForme[2]) && (y>0)) {
                	if ((tab[x][y-1].getR() == pixelFond[0]) && (tab[x][y-1].getV() == pixelFond[1]) && (tab[x][y-1].getB() == pixelFond[2])) {
                		tab[x][y].setR(pixelFond[0]);
                		tab[x][y].setV(pixelFond[1]);
                		tab[x][y].setB(pixelFond[2]);
                    }
                }
                //si pixel vert et pixel de droite est blanc, colore la case de droite en vert
                if ((tab[x][y].getR() == pixelForme[0]) && (tab[x][y].getV() == pixelForme[1]) && (tab[x][y].getB() == pixelForme[2]) && (x<image.getLargeur()-1)) {
                	if ((tab[x+1][y].getR() == pixelFond[0]) && (tab[x+1][y].getV() == pixelFond[1]) && (tab[x+1][y].getB() == pixelFond[2])) {
                		tab[x+1][y].setR(pixelForme[0]);
                		tab[x+1][y].setV(pixelForme[1]);
                		tab[x+1][y].setB(pixelForme[2]);
                    }
                }
            }
        }
    	
    	//boucle pour colorer en blanc tous les pixels verts situes au dessus/a gauche d'une case dont le pixel est blanc
    	for (int y = image.getHauteur() - 1; y >= 0; y--) {
    	    for (int x = image.getLargeur() - 1; x >= 0; x--) {
    	    	//si pixel est vert et pixel de droite/dessous est blanc, colore la case en blanc
    	        if ((tab[x][y].getR() == pixelForme[0]) && (tab[x][y].getV() == pixelForme[1]) && (tab[x][y].getB() == pixelForme[2])) {
    	            if (x + 1 < image.getLargeur() && (tab[x + 1][y].getR() == pixelFond[0]) && (tab[x + 1][y].getV() == pixelFond[1]) && (tab[x + 1][y].getB() == pixelFond[2])) {
    	                tab[x][y].setR(pixelFond[0]);
    	                tab[x][y].setV(pixelFond[1]);
    	                tab[x][y].setB(pixelFond[2]);
    	            }
    	            if (y - 1 >= 0 && (tab[x][y + 1].getR() == 255) && (tab[x][y + 1].getV() == 255) && (tab[x][y + 1].getB() == 255)) {
    	                tab[x][y].setR(pixelFond[0]);
    	                tab[x][y].setV(pixelFond[1]);
    	                tab[x][y].setB(pixelFond[2]);
    	            }
    	        }
    	        //si pixel est noir, le colore en vert
    	        if ((tab[x][y].getR() == 0) && (tab[x][y].getV() == 0) && (tab[x][y].getB() == 0)) {
	                tab[x][y].setR(pixelForme[0]);
	                tab[x][y].setV(pixelForme[1]);
	                tab[x][y].setB(pixelForme[2]);
	            }
    	    }
        }
    	if(fondNoir) {
    		pixelFond[0] = 0;
        	pixelFond[1] = 0;
        	pixelFond[2] = 0;
	        for (int y = 0; y < image.getHauteur()-1; y++) {
	            for (int x = 0; x < image.getLargeur()-1; x++) {
	                if ((tab[x][y].getR() == 255) && (tab[x][y].getV() == 255) && (tab[x][y].getB() == 255))  {
	                		tab[x][y].setR(pixelFond[0]);
	                		tab[x][y].setV(pixelFond[1]);
	                		tab[x][y].setB(pixelFond[2]);
	                }
	            }
	        }
    	}
	}
    
}
