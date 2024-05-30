package utilitaires;

import controleurs.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 * Classe MorphingAbstract
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public abstract class MorphingAbstract {

    private TextField champEtapes;
    private TextField champDelai;
    private ImageView imageGauche;
    private String imagePath;
    private ImageView imageDroite;

    public MorphingAbstract(TextField champEtapes, TextField champDelai,ImageView imageGauche) {
        this.champEtapes = champEtapes;
        this.champDelai = champDelai;
        this.imageGauche= imageGauche;
    }

    public MorphingAbstract(TextField champEtapes, TextField champDelai,ImageView imageGauche,ImageView imageDroite) {
        this.champEtapes = champEtapes;
        this.champDelai = champDelai;
        this.imageGauche= imageGauche;
        this.imageDroite= imageDroite;
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

    public ImageView getImageDroite(){
        return imageDroite; 
    }

    public String getImagePath(){
        return imagePath; 
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath; 
    }
    
    /**
     * Fonction qui permet d'appeler sur tous les points de contrôle la méthode de calcul pour les points suivants  
     * @param nbEtapes
     */
    protected void calculEnsemblePointSuivant(int nbEtapes) {
        for (Map.Entry<Character, Point> entry : PointsControleHandler.getPointsControleGauche().entrySet()) {
            Character key = entry.getKey();
            Point pointDebut = entry.getValue();
            Point pointFin = PointsControleHandler.getPointsControleDroite().get(key);
            calculPointSuivant(pointDebut, pointFin, nbEtapes);
        }
    }

    /**
     * Fonction qui fait les calculs pour les points suivants 
     * @param pointDebut 
     * @param pointFin 
     * @param nbEtapes
     */
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

    /**
     * Fonction qui supprime puis recrée le dossier qui contient les images étapes par étapes du morphing s'il existe déjà, sinon elle le crée   
     */
    protected void dossierFormeSimples(){
        File dossier = new File("./Formes");
        //vérifier si le dossier existe
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
    
    /**
     * Fonction qui supprime le dossier qui contient les images pour générer le gif  
     * @param dossier  
     * @return un boolean 
     */
    protected boolean supprimerDossier(File dossier) {
        if (dossier.isDirectory()) {
            //récupérer la liste des fichiers et sous-dossiers du dossier
            File[] fichiers = dossier.listFiles();
            if (fichiers != null) {
                for (File fichier : fichiers) {
                    //récursivement supprimer chaque fichier ou sous-dossier
                    if (!supprimerDossier(fichier)) {
                        return false; //arrêter si la suppression échoue pour l'un des fichiers
                    }
                }
            }
        }
        //supprimer le dossier lui-même après avoir supprimé son contenu
        return dossier.delete();
    }

    /**
     * Fonction qui modifie le fond de l'image  
     * @param imageMGauche  
     * @param pixelFond
     * @return une imageM avec le fond modifié 
     */
    public ImageM modifFondImage(ImageM imageMGauche, int[] pixelFond) {
        Pixel[][] tab = imageMGauche.getTab();
        int[] pixelGlobal = {255, 255, 255};
        
        if(pixelFond != null) {
        	pixelGlobal = pixelFond;
		}
        
        for (int y = 0; y < imageMGauche.getLargeur(); y++) {
            for (int x = 0; x < imageMGauche.getHauteur(); x++) {
            	tab[x][y].setR(pixelGlobal[0]);
            	tab[x][y].setV(pixelGlobal[1]);
            	tab[x][y].setB(pixelGlobal[2]);
            }
        }
        return imageMGauche;
    }

    /**
     * Fonction qui s'occupe d'appeler la méthode pour tracer les lignes entre chaque points de contrôles 
     * @param imageMGauche  
     * @param pointsCalcules
     * @param premierPixel 
     * @param secondPixel 
     */
    public void colorPointsDeControle(ImageM imageMGauche, Map<Character, Point> pointsCalcules, int[] premierPixel, int[] secondPixel) {
    	Pixel[][] tab = imageMGauche.getTab();
    	ImageM imageModifiee = new ImageM(tab);
    	
    	Point pointDepart = null;
    	int tailleMap = pointsCalcules.size();
    	int pointEnCours = 1;

        int[] pixelFond = {255, 255, 255};
    	int[] pixelForme = {44, 51, 62};//couleur par défaut pour tracer  
        //int[] pixelForme = {0,255,0}; 
    	if(premierPixel != null){
    		pixelFond = premierPixel;
    	}
    	if(secondPixel != null) {
			pixelForme = secondPixel;
		}
    	
    	//boucle sur les valeurs d'entree de la map 'pointsCalcules' pour colorer chaque point en noir
    	for (Map.Entry<Character, Point> entry : pointsCalcules.entrySet()) {
        	Character key = entry.getKey();
            Point pointDebut = entry.getValue();
            
            //colore le point de contrôle en noir
            /*tab[(int) Math.round(pointDebut.getX())][(int) Math.round(pointDebut.getY())].setR(0);
            tab[(int) Math.round(pointDebut.getX())][(int) Math.round(pointDebut.getY())].setV(0);
            tab[(int) Math.round(pointDebut.getX())][(int) Math.round(pointDebut.getY())].setB(0);*/

            tab[(int) pointDebut.getX()][(int) pointDebut.getY()].setR(0);
            tab[(int) pointDebut.getX()][(int) pointDebut.getY()].setV(0);
            tab[(int) pointDebut.getX()][(int) pointDebut.getY()].setB(0);


            //stocke le premier point de la map
            if(key == 'A') {
            	pointDepart = pointDebut;
            }
            
            //tant qu'on atteint pas la fin de la map, on lie le point à son suivant (traçage de la ligne entre n et n+1)
            if(pointEnCours < tailleMap) {
            	Character cleSuivante = (char) (key + 1);
                Point pointSuivant = pointsCalcules.get(cleSuivante);
                tracerLigne(imageMGauche, pointDebut, pointSuivant);
            }
            //atteinte de la fin de la map, lie le dernier point au premier (pour fermer la forme)
            else {
            	tracerLigne(imageMGauche, pointDebut, pointDepart);
            }
            pointEnCours += 1;
        }
        remplirForme(imageMGauche, pixelFond, pixelForme);
    	
        String outputPath = "./Formes/image_"+System.currentTimeMillis()+".jpg";
        imageModifiee.sauvegarderImage(outputPath);
    }

    /**
     * Fonction qui trace une ligne entre un point de départ et un d'arrivée 
     * @param image  
     * @param depart
     * @param arrivee
     */
    public void tracerLigne(ImageM image, Point depart, Point arrivee) {
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

    /*public void remplirForme(ImageM image, int[] pixelFond, int[] pixelForme) {
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
                    if (x<299){
                        if ((tab[x+1][y].getR() == pixelFond[0]) && (tab[x+1][y].getV() == pixelFond[1]) && (tab[x+1][y].getB() == pixelFond[2])) {
                            tab[x+1][y].setR(pixelForme[0]);
                            tab[x+1][y].setV(pixelForme[1]);
                            tab[x+1][y].setB(pixelForme[2]);
                        }
                    }
                }
                //si pixel est vert et pixel du dessus est blanc, colore la case en blanc
                if ((tab[x][y].getR() == pixelForme[0]) && (tab[x][y].getV() == pixelForme[1]) && (tab[x][y].getB() == pixelForme[2])&&(y>1)) {
                    if (y>1){
                        if ((tab[x][y-1].getR() == pixelFond[0]) && (tab[x][y-1].getV() == pixelFond[1]) && (tab[x][y-1].getB() == pixelFond[2])) {
                            tab[x][y].setR(pixelFond[0]);
                            tab[x][y].setV(pixelFond[1]);
                            tab[x][y].setB(pixelFond[2]);
                        }
                    }
                	
                }
                //si pixel vert et pixel de droite est blanc, colore la case de droite en vert
                if ((tab[x][y].getR() == pixelForme[0]) && (tab[x][y].getV() == pixelForme[1]) && (tab[x][y].getB() == pixelForme[2]) && (x<image.getLargeur()-1)) {
                    if (x<299){
                        if ((tab[x+1][y].getR() == pixelFond[0]) && (tab[x+1][y].getV() == pixelFond[1]) && (tab[x+1][y].getB() == pixelFond[2])) {
                            tab[x+1][y].setR(pixelForme[0]);
                            tab[x+1][y].setV(pixelForme[1]);
                            tab[x+1][y].setB(pixelForme[2]);
                        }
                    }
                }
            }
        }
    	
    	//boucle pour colorer en blanc tous les pixels verts situes au dessus/a gauche d'une case dont le pixel est blanc
    	for (int y = image.getHauteur() - 1; y >= 0; y--) {
    	    for (int x = image.getLargeur() - 1; x >= 0; x--) {
    	    	//si pixel est vert et pixel de droite/dessous est blanc, colore la case en blanc
    	        if ((tab[x][y].getR() == pixelForme[0]) && (tab[x][y].getV() == pixelForme[1]) && (tab[x][y].getB() == pixelForme[2])) {
                    if (x<299){
                        if (x + 1 < image.getLargeur() && (tab[x + 1][y].getR() == pixelFond[0]) && (tab[x + 1][y].getV() == pixelFond[1]) && (tab[x + 1][y].getB() == pixelFond[2])) {
                            tab[x][y].setR(pixelFond[0]);
                            tab[x][y].setV(pixelFond[1]);
                            tab[x][y].setB(pixelFond[2]);
                        }
                    }
                    if (y>1){
                        if (y - 1 > 0 && (tab[x][y + 1].getR() == pixelFond[0]) && (tab[x][y + 1].getV() == pixelFond[1]) && (tab[x][y + 1].getB() == pixelFond[2])) {
                            tab[x][y].setR(pixelFond[0]);
                            tab[x][y].setV(pixelFond[1]);
                            tab[x][y].setB(pixelFond[2]);
                        }
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
	}*/


    /**
     * Fonction qui rempli la forme tracée 
     * @param image  
     * @param pixelFond
     * @param pixelForme
     */
    public void remplirForme(ImageM image, int[] pixelFond, int[] pixelForme) {
        Pixel[][] tab = image.getTab();
        boolean fondNoir = false;
        
        if((pixelFond[0] == 0) && (pixelFond[1] == 0) && (pixelFond[2] == 0)) {
        	fondNoir = true;
        	pixelFond[0] = 255;
        	pixelFond[1] = 255;
        	pixelFond[2] = 255;
        }
        
    	//boucle pour colorier tous les pixels se situant à droite d'un pixel noir, et de colorier en blanc tous les pixels de la couleur par défaut (ou couleur de l'image) situés sous un pixel blanc
        //on colorie les pixels soit avec la couleur par défaut, soit si l'image contient seulement 2 couleurs, une de fond et une dans la forme, alors on colorie le pixel avec la couleur de la forme (sur l'image)
    	for (int y = 0; y < image.getHauteur()-1; y++) {
            for (int x = 0; x < image.getLargeur()-1; x++) {
            	//si le pixel est noir et la case à droite est blanche, colorie la case de droite
            	if ((tab[x][y].getR() == 0) && (tab[x][y].getV() == 0) && (tab[x][y].getB() == 0) && (x<image.getLargeur()-1))  {
                	if ((tab[x+1][y].getR() == pixelFond[0]) && (tab[x+1][y].getV() == pixelFond[1]) && (tab[x+1][y].getB() == pixelFond[2])) {
                		tab[x+1][y].setR(pixelForme[0]);
                		tab[x+1][y].setV(pixelForme[1]);
                		tab[x+1][y].setB(pixelForme[2]);
                    }
                }
                //si le pixel est colorié et le pixel du dessus est blanc, colorie la case en blanc
            	if ((tab[x][y].getR() == pixelForme[0]) && (tab[x][y].getV() == pixelForme[1]) && (tab[x][y].getB() == pixelForme[2]) && (y>0)) {
                	if ((tab[x][y-1].getR() == pixelFond[0]) && (tab[x][y-1].getV() == pixelFond[1]) && (tab[x][y-1].getB() == pixelFond[2])) {
                		tab[x][y].setR(pixelFond[0]);
                		tab[x][y].setV(pixelFond[1]);
                		tab[x][y].setB(pixelFond[2]);
                    }
                }
                //si le pixel est colorié et le pixel de droite est blanc, colorie la case de droite
                if ((tab[x][y].getR() == pixelForme[0]) && (tab[x][y].getV() == pixelForme[1]) && (tab[x][y].getB() == pixelForme[2]) && (x<image.getLargeur()-1)) {
                	if ((tab[x+1][y].getR() == pixelFond[0]) && (tab[x+1][y].getV() == pixelFond[1]) && (tab[x+1][y].getB() == pixelFond[2])) {
                		tab[x+1][y].setR(pixelForme[0]);
                		tab[x+1][y].setV(pixelForme[1]);
                		tab[x+1][y].setB(pixelForme[2]);
                    }
                }
            }
        }
    	
    	//boucle pour colorier en blanc tous les pixels coloriés situés au dessus/à gauche d'une case dont le pixel est blanc
    	for (int y = image.getHauteur() - 1; y >= 0; y--) {
    	    for (int x = image.getLargeur() - 1; x >= 0; x--) {
    	    	//si le pixel est colorié et le pixel de droite/dessous est blanc, colorie la case en blanc
    	    	if ((tab[x][y].getR() == pixelForme[0]) && (tab[x][y].getV() == pixelForme[1]) && (tab[x][y].getB() == pixelForme[2])) {
    	            if (x + 1 < image.getLargeur() && (tab[x + 1][y].getR() == pixelFond[0]) && (tab[x + 1][y].getV() == pixelFond[1]) && (tab[x + 1][y].getB() == pixelFond[2])) {
    	                tab[x][y].setR(pixelFond[0]);
    	                tab[x][y].setV(pixelFond[1]);
    	                tab[x][y].setB(pixelFond[2]);
    	            }
    	            if (y - 1 >= 0 && (tab[x][y + 1].getR() == pixelFond[0]) && (tab[x][y + 1].getV() == pixelFond[1]) && (tab[x][y + 1].getB() == pixelFond[2])) {
    	                tab[x][y].setR(pixelFond[0]);
    	                tab[x][y].setV(pixelFond[1]);
    	                tab[x][y].setB(pixelFond[2]);
    	            }
    	        }
    	        //si le pixel est noir, le colorie (couleur défaut ou couleur forme)
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

    /**
     * Fonction qui récupère le nombre de couleur sur l'image  
     * @param imageBase  
     * @return List<int[]> 
     */
    public List<int[]> getNombreCouleur(ImageM imageBase) {
    	
    	Pixel[][] tabBase = imageBase.getTab();
    	List<int[]> couleurs = new ArrayList<>();
    	
    	for (int y = 0; y < imageBase.getHauteur(); y++) {  //parcourir la hauteur
            for (int x = 0; x < imageBase.getLargeur(); x++) {  //parcourir la largeur
                Pixel pixel = tabBase[y][x];  //accéder correctement aux indices
                int r = pixel.getR();
                int v = pixel.getV();
                int b = pixel.getB();

                
                boolean couleurDejaExistante = false;
                for (int[] couleurExistante : couleurs) {
                    if (couleurExistante[0] == r && couleurExistante[1] == v && couleurExistante[2] == b) {
                        couleurDejaExistante = true;
                        break;
                    }
                }
                if (!couleurDejaExistante) {
                    couleurs.add(new int[]{r, v, b});
                }
            }
        }
    	return couleurs;
    }

    /**
     * Fonction qui colorie la forme 
     * @param nbEtapes 
     * @param imageBase
     * @param couleurs 
     * @param pointsCalculesImageDebut 
     */
    public void colorFormeComplet(int nbEtapes, ImageM imageBase, List<int[]> couleurs, Map<Character, Point> pointsCalculesImageDebut) {
    	
    	if(couleurs.size() < 3) {
        	int[] premierPixel = couleurs.get(0);
        	
        	if (couleurs.size() < 2) {
                ImageM imageFondModifie = modifFondImage(imageBase, premierPixel);
                if(pointsCalculesImageDebut == null) {
                	colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleGauche(), premierPixel, null);
                }else {
                	colorPointsDeControle(imageFondModifie, pointsCalculesImageDebut, premierPixel, null);
                }
                	
                //boucle tant qu'on a pas atteint le nombre d'étapes demandées
                while(nbEtapes>0) {
                	calculEnsemblePointSuivant(nbEtapes);
                	modifFondImage(imageFondModifie, premierPixel);
                	if(pointsCalculesImageDebut == null) {
                		colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleGauche(), premierPixel, null);
                	}else {
	            		Map<Character, Point> pointsCalcules = MorphingArrondiHandler.traceCourbeBezier(PointsControleIntermediairesPlacerHandler.getPointsMorphingDebut()); 
                    	colorPointsDeControle(imageFondModifie, pointsCalcules, premierPixel, null);
                    }
            		nbEtapes-=1;
                }
        	}else {
        		if((premierPixel[0] == 0) && (premierPixel[1] == 0) && (premierPixel[2] == 0)){
                    ImageM imageFondModifie = modifFondImage(imageBase, null);
                    int[] secondPixel = couleurs.get(1);
                    if(pointsCalculesImageDebut == null) {
    	            	colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleGauche(), premierPixel, secondPixel);
                    }else {
                    	colorPointsDeControle(imageFondModifie, pointsCalculesImageDebut, premierPixel, secondPixel);
                    }
                    
    	            //boucle tant qu'on a pas atteint le nombre d'étapes demandées
    	            while(nbEtapes>0) {
    	            	calculEnsemblePointSuivant(nbEtapes);
    	            	modifFondImage(imageFondModifie, null);
    	            	
    	            	if(pointsCalculesImageDebut == null) {
    	            		colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleGauche(), premierPixel, secondPixel);
    	            	}else {
    	            		Map<Character, Point> pointsCalcules = MorphingArrondiHandler.traceCourbeBezier(PointsControleIntermediairesPlacerHandler.getPointsMorphingDebut()); 
                        	colorPointsDeControle(imageFondModifie, pointsCalcules, premierPixel, secondPixel);
                        }
    	            	nbEtapes-=1;
    	            }
        		}else {
                    ImageM imageFondModifie = modifFondImage(imageBase, premierPixel);
                    int[] secondPixel = couleurs.get(1);
                    if(pointsCalculesImageDebut == null) {
                    	colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleGauche(), premierPixel, secondPixel);
                    }
                    else {
                    	colorPointsDeControle(imageFondModifie, pointsCalculesImageDebut, premierPixel, secondPixel);
                    }
    	            //boucle tant qu'on a pas atteint le nombre d'étapes demandées
    	            while(nbEtapes>0) {
    	            	calculEnsemblePointSuivant(nbEtapes);
    	            	modifFondImage(imageFondModifie, premierPixel);
    	            	if(pointsCalculesImageDebut == null) {
    	            		colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleGauche(), premierPixel, secondPixel);
    	            	}else {
    	            		Map<Character, Point> pointsCalcules = MorphingArrondiHandler.traceCourbeBezier(PointsControleIntermediairesPlacerHandler.getPointsMorphingDebut()); 
                        	colorPointsDeControle(imageFondModifie, pointsCalcules, premierPixel, secondPixel);
                        }
    	            	nbEtapes-=1;
    	            }
        		}
        	}
        }else {
            ImageM imageFondModifie = modifFondImage(imageBase, null);
            if(pointsCalculesImageDebut == null) {
            	colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleGauche(), null, null);
            }else {
            	colorPointsDeControle(imageFondModifie, pointsCalculesImageDebut, null, null);
            }
            
            //boucle tant qu'on a pas atteint le nombre d'étapes demandées
            while(nbEtapes>0) {
            	calculEnsemblePointSuivant(nbEtapes);
            	modifFondImage(imageFondModifie, null);
            	if(pointsCalculesImageDebut == null) {
            		colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleGauche(), null, null);
            	}else {
            		Map<Character, Point> pointsCalcules = MorphingArrondiHandler.traceCourbeBezier(PointsControleIntermediairesPlacerHandler.getPointsMorphingDebut()); 
                	colorPointsDeControle(imageFondModifie, pointsCalcules, null, null);
                }
            	nbEtapes-=1;
            }
        }
    }
}
