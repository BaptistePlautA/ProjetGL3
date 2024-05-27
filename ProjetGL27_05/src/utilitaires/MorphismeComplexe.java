package utilitaires;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class MorphismeComplexe {
	//Chemin des dossiers
	final static String cheminDelaunayInter = "./TriangulationDelaunay/";
	final static String cheminRepertoire = "./FormesComplexes/";
	
		
	   protected static void effectuerTriangulation(int tailleIMGMax, List<Point> pointsDepart, List<Point> pointsFin, int nbEtapes) {
		   	
		   	//Fonction pour alléger la main fonction et séparer le code
		   
	        InterpolationIMGComplexe interpolation = new InterpolationIMGComplexe();
	        TriangulationDelaunay td = new TriangulationDelaunay();
	        //On créé une liste de Triangles respectant la condition de Delaunay à partir de nos points de départ
	        List<Triangle> triangulation = td.trianguler(pointsDepart);
	        //On créé une liste de liste de points pour avoir l'interpolation des points à chaque étape, entre le début et la fin
	        List<List<Point>> pointsInterpoles = interpolation.interpolerPoints(pointsDepart, pointsFin, nbEtapes);
	        //On créé une liste de liste de triangles pour avoir l'interpolation de triangles (issus des points interpolés) à chaque étape, entre le début et la fin
	        List<List<Triangle>> trianglesInterpoles = interpolation.interpolerTriangles(pointsDepart, pointsFin, triangulation, nbEtapes);
	        
	        //Partie du code pas obligatoire mais permet d'avoir un dossier avec les différentes étapes de notre triangulation
	        //Pratique pour la curiosité, la vérification du code ou le debugging
	        for (int i = 0; i < pointsInterpoles.size(); i++) {
	            List<Point> pointsEtape = pointsInterpoles.get(i);
	            List<Triangle> trianglesEtape = trianglesInterpoles.get(i);
	            String nomFichier = System.currentTimeMillis() + ".jpg";
	            File fichierDeSortie = new File(cheminDelaunayInter + nomFichier);
	            td.dessinerEtapesTriangulation(pointsEtape, trianglesEtape, fichierDeSortie, tailleIMGMax);
	        }
	    }
	    
	    protected static void effectuerMorphisme(int tailleIMGMax, List<Point> pointsDepart, List<Point> pointsFin, List<List<Point>> pointsInterpoles, List<List<Triangle>> trianglesInterpoles,
	    		int nbEtapes, BufferedImage imgDepart,BufferedImage imgFin) {
		   	//Fonction pour alléger la main fonction et séparer le code

	        MorphismeComplexe mc = new MorphismeComplexe();
	        
	        ColorationIMGComplexe coloration = new ColorationIMGComplexe();
	        
	        //Pour chaque étape, une interpolation des points et triangles, et la coloration des pixels
	        for (int i = 0; i < pointsInterpoles.size(); i++) {
	            List<Point> pointsEtape = pointsInterpoles.get(i);
	            List<Triangle> trianglesEtape = trianglesInterpoles.get(i);
	            List<Triangle> trianglesFin = trianglesInterpoles.get(nbEtapes);
	            BufferedImage imgIntermediaire = coloration.getImageInter(tailleIMGMax, imgDepart, imgFin, pointsDepart, trianglesFin, pointsEtape, trianglesEtape, (double) i / nbEtapes);
	            mc.sauvegarderMorphisme(imgIntermediaire, System.currentTimeMillis() + ".jpg");
	        }
	    }
	    
	    //Save des images ainsi créées
	    protected void sauvegarderMorphisme(BufferedImage img, String nomFichier) {
	        File fichierDeSortie = new File(cheminRepertoire + nomFichier);

	        try {
	            ImageIO.write(img, "jpg", fichierDeSortie);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	
	public static void realiserMorphisme(List<Point> pointsDepart, List<Point> pointsFin, int nbEtapes, BufferedImage imageG, BufferedImage imageD) {
        //Fonction principale du morphisme
		
        //TRIANGULATION DE DELAUNAY (Utilise la classe du même nom)
        effectuerTriangulation(300, pointsDepart, pointsFin, nbEtapes);
        
        //MORPHISME D'IMAGE (Utilise la classe InterpolationIMGComplexe et ColorationIMGComplexe)  
        //On initialise le nécessaire pour le morphisme
        InterpolationIMGComplexe interpolation = new InterpolationIMGComplexe();
        List<List<Point>> pointsInterpoles = interpolation.interpolerPoints(pointsDepart, pointsFin, nbEtapes);
        List<Triangle> triangulation = new TriangulationDelaunay().trianguler(pointsDepart);
        List<List<Triangle>> trianglesInterpoles = interpolation.interpolerTriangles(pointsDepart, pointsFin, triangulation, nbEtapes);
        // Morphisme
        effectuerMorphisme(300, pointsDepart, pointsFin, pointsInterpoles, trianglesInterpoles, nbEtapes, imageG, imageD);
	}
}
