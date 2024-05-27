package controleurs;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import utilitaires.MorphingAbstract;
import utilitaires.MorphismeComplexe;
import utilitaires.Point;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;

public class MorphingComplexeHandler extends MorphingAbstract implements EventHandler<ActionEvent> {
	public int nbEtapes = Integer.parseInt(getChampEtapes().getText());
    public int delai = Integer.parseInt(getChampDelai().getText());
    public final static String cheminDelaunayInter = "./TriangulationDelaunay/";
	public final static String cheminRepertoire = "./FormesComplexes/";
	
	public MorphingComplexeHandler(TextField champEtapes, TextField champDelai, ImageView imageGauche,
			ImageView imageDroite,PointsControleHandler handler) {
		super(champEtapes, champDelai, imageGauche,imageDroite, handler);
	}
	
	
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


	    
	 protected static void pointMapEnListe(Map<Character, Point> pointsDepartMap, Map<Character, Point> pointsFinMap, List<Point> pointsDepart, List<Point> pointsFin) {
		    // On vide les listes au cas où
		    pointsDepart.clear();
		    pointsFin.clear();
		    //On rajoute des points de contrôles fixes aux 4 coins de notre image par nécessité (début et fin)
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
		    
		    //Les points de contrôles donnent des valeurs (trop) précises donc je préfère arrondir au cas où
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
	    
	    
	    
	 protected static BufferedImage imageEnBufferedImage(Image image) {
		 	//Le FX nous renvoie une image, on la converti car notre morphing utilise des bufferedImage
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

	 
    @Override
    public void handle(ActionEvent event) {
        //Creation d'un dossier propre pour le morph et la triangulation
        dossierFormeComplexes();
        //Récupération des images du FX
        javafx.scene.image.Image imageG = getImageGauche().getImage();
        javafx.scene.image.Image imageD = getImageDroite().getImage();
        //Conversion image en BufferedImage
        BufferedImage imgDepart = imageEnBufferedImage(imageG);
        BufferedImage imgFin = imageEnBufferedImage(imageD);
        //Conversion Maps en Listes
        Map<Character, Point> pointsDepartMap = PointsControleHandler.getPointsControleDebut();
        Map<Character, Point> pointsFinMap = PointsControleHandler.getPointsControleFin();
        List<Point> pointsDepart = new ArrayList<>();
        List<Point> pointsFin = new ArrayList<>();
        pointMapEnListe(pointsDepartMap, pointsFinMap, pointsDepart, pointsFin);
        //DEBUG
        //System.out.println(pointsDepart.toString());
        //System.out.println(pointsFin.toString());

        MorphismeComplexe.realiserMorphisme(pointsDepart, pointsFin, nbEtapes, imgDepart, imgFin);
       
        
        //Conversion des images en gif
        ConvertisseurGIF convertisseur = new ConvertisseurGIF();
        String cheminDossier = "./FormesComplexes/";
        convertisseur.convertirEnGif(delai,cheminDossier);
        getHandler().handleReset(event);
    }
    
}
