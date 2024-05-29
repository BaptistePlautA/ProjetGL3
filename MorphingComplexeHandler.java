package controleurs;

import utilitaires.*; 

import java.awt.image.BufferedImage;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;

/**
 * Classe MorphingComplexeHandler
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class MorphingComplexeHandler extends MorphingAbstract implements EventHandler<ActionEvent> {
	public int nbEtapes = Integer.parseInt(getChampEtapes().getText());
    public int delai = Integer.parseInt(getChampDelai().getText());
    public final static String cheminDelaunayInter = "./TriangulationDelaunay/";
	public final static String cheminRepertoire = "./FormesComplexes/";

    private PointsControleHandler handler; 
	
	public MorphingComplexeHandler(TextField champEtapes, TextField champDelai, ImageView imageGauche,ImageView imageDroite, PointsControleHandler handler) {
		super(champEtapes, champDelai, imageGauche,imageDroite);
        this.handler = handler; 
	}

	@Override
    public void handle(ActionEvent event) {
		
        Thread thread1 = new Thread(() -> {		//Cr�ation de la fen�tre
           	
            Platform.runLater(() -> {	//g�re le Fx, �a fonctionne pas sans �a
            

            	AttenteFinMorphisme.creerFenetre();
            	
            });
     	
        });
        
        
        Thread thread2 = new Thread(() -> {		//Traitement du morphisme, se fait une fois que la fen�tre est cr��
            try {
                thread1.join();  //Attend que thread1 se termine
                
                //création d'un dossier propre pour le morph et la triangulation
                dossierFormeComplexes();
                //récupération des images du FX
                javafx.scene.image.Image imageG = getImageGauche().getImage();
                javafx.scene.image.Image imageD = getImageDroite().getImage();
                //conversion image en BufferedImage
                BufferedImage imgDepart = imageEnBufferedImage(imageG);
                BufferedImage imgFin = imageEnBufferedImage(imageD);
                //conversion Maps en Listes
                Map<Character, Point> pointsDepartMap = PointsControleHandler.getPointsControleGauche();
                Map<Character, Point> pointsFinMap = PointsControleHandler.getPointsControleDroite();
                List<Point> pointsDepart = new ArrayList<>();
                List<Point> pointsFin = new ArrayList<>();
                pointMapEnListe(pointsDepartMap, pointsFinMap, pointsDepart, pointsFin);
                //DEBUG
                //System.out.println(pointsDepart.toString());
                //System.out.println(pointsFin.toString());

                MorphismeComplexe.realiserMorphisme(pointsDepart, pointsFin, nbEtapes, imgDepart, imgFin);
                

                //convertit les images en gif
                Platform.runLater(() -> {			//g�re le Fx, �a fonctionne pas sans �a
  
                    try {
                    	
                        ConvertisseurGIF convertisseur = new ConvertisseurGIF();
                        convertisseur.convertirEnGif(delai, "./FormesComplexes");
                        handler.handleReset(event);
                        
                    }
                    catch(Exception exceptionGIF){
                        System.err.println("Erreur lors de la mise en GIF");
                    }
                	
                 
                });
               
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
		
        
        Thread thread3 = new Thread(() -> {		//Ferme la fen�tre une fois que le traitement a �t� effectu�
            try {
            	
                thread2.join();  	// Attend que thread2 termine    
                
                Platform.runLater(() -> {	//g�re le Fx, �a fonctionne pas sans �a
    
                	AttenteFinMorphisme.fermerFenetre();
                });
 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
    	
            	
        thread1.start();
        thread2.start();
        thread3.start();
        
        
        
        
        
    } 
	
	/**
	 * Procédure qui supprime et crée les dossiers pour stocker les images intermédiaires s'ils existent déjà (sinon juste crée)
	 */
	protected void dossierFormeComplexes(){
		File dossier = new File(cheminRepertoire);
		File dossierTriangulation = new File(cheminDelaunayInter);
		// Vérifier si le dossier existe
		if (dossier.exists() && dossier.isDirectory()) {
			//suppression et création du dossier
			System.out.println("Le dossier existe.");
			supprimerDossier(dossier);
			dossier.mkdirs();
		} else {
			System.out.println("Le dossier n'existe pas.");
			dossier.mkdirs();
			System.out.println("Le dossier a été créé");
		}
		
		//Idem triangulation Delaunay
		if (dossierTriangulation.exists() && dossierTriangulation.isDirectory()) {
			//suppression et création du dossier
			System.out.println("Le dossier existe.");
			supprimerDossier(dossierTriangulation);
			dossierTriangulation.mkdirs();
		} else {
			System.out.println("Le dossier n'existe pas.");
			dossierTriangulation.mkdirs();
			System.out.println("Le dossier a été créé");
		}
	}


	/**
	 * Procédure qui passe de stocker les points de contrôles dans des map à des listes (et add des points dans les angles par défaut)
	 * @param pointsDepartMap
	 * @param pointsFinMap
	 * @param pointsDepart
	 * @param pointsFin
	 */
	protected static void pointMapEnListe(Map<Character, Point> pointsDepartMap, Map<Character, Point> pointsFinMap, List<Point> pointsDepart, List<Point> pointsFin) {
		//on vide les listes au cas où
		pointsDepart.clear();
		pointsFin.clear();
		//on rajoute des points de contrôles fixes aux 4 coins de notre image par nécessité (début et fin)
		//pour avoir une triangulation et un morphisme propres
		pointsDepart.add(new Point(0, 0, 0));
		pointsDepart.add(new Point(300, 0, 1));
		pointsDepart.add(new Point(300, 300, 2));
		pointsDepart.add(new Point(0, 300, 3));

		pointsFin.add(new Point(0, 0, 0));
		pointsFin.add(new Point(300, 0, 1));
		pointsFin.add(new Point(300, 300, 2));
		pointsFin.add(new Point(0, 300, 3));
		
		//DEBUG
		/*
		System.out.println("pointsDepartMap:");
		for (Map.Entry<Character, Point> entry : pointsDepartMap.entrySet()) {
			System.out.println("CLE: " + entry.getKey() + ", VALEUR: " + entry.getValue());
		}
		
		System.out.println("pointsFinMap:");
		for (Map.Entry<Character, Point> entry : pointsFinMap.entrySet()) {
			System.out.println("CLE: " + entry.getKey() + ", VALEUR: " + entry.getValue());
		}
		*/
		
		//Les points de contrôles donnent des valeurs (trop) précises donc on préfère arrondir au cas où
		for (Point point : pointsDepartMap.values()) {
			double x = Math.round(point.getX() * 10.0) / 10.0;
			double y = Math.round(point.getY() * 10.0) / 10.0;
			pointsDepart.add(new Point(x, y, point.getIndex()));
		}

		for (Point point : pointsFinMap.values()) {
			double x = Math.round(point.getX() * 10.0) / 10.0;
			double y = Math.round(point.getY() * 10.0) / 10.0;
			pointsFin.add(new Point(x, y, point.getIndex()));
		}		
	}
	    
	/**
	 * Fonction qui passe d'une Image à une BufferedImage 
	 * @param image
	 * @return retourne la BufferedImage 
	 */ 
	protected static BufferedImage imageEnBufferedImage(Image image) {
		//le FX nous renvoie une image, on la converti car notre morphing utilise des bufferedImage
		if (image == null) {
			throw new IllegalArgumentException("L'image ne doit pas être null.");
		}

		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		WritableImage writableImage = new WritableImage(width, height);
		PixelReader pixelReader = image.getPixelReader();
		PixelWriter pixelWriter = writableImage.getPixelWriter();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixelWriter.setArgb(x, y, pixelReader.getArgb(x, y));
			}
		}
		SwingFXUtils.fromFXImage(writableImage, bufferedImage);
		return bufferedImage;
	}
}