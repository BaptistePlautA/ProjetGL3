package utilitaires;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Classe MorphismeComplexe
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class MorphismeComplexe {
	//Chemin des dossiers
	final static String cheminDelaunayInter = "./TriangulationDelaunay/";
	final static String cheminRepertoire = "./FormesComplexes/";
	
	/**
     * Fonction qui effectue la triangulation (permet d'alléger la main fonction et de séparer le code)
     * @param tailleIMGMax   
     * @param pointsDepart
	 * @param pointsFin
	 * @param nbEtapes
     */
	protected static void effectuerTriangulation(int tailleIMGMax, List<Point> pointsDepart, List<Point> pointsFin, int nbEtapes) {
		InterpolationIMGComplexe interpolation = new InterpolationIMGComplexe();
		TriangulationDelaunay td = new TriangulationDelaunay();

		//on créé une liste de Triangles respectant la condition de Delaunay à partir de nos points de départ
		List<Triangle> triangulation = td.trianguler(pointsDepart);

		//on créé une liste de liste de points pour avoir l'interpolation des points à chaque étape, entre le début et la fin
		List<List<Point>> pointsInterpoles = interpolation.interpolerPoints(pointsDepart, pointsFin, nbEtapes);

		//on créé une liste de liste de triangles pour avoir l'interpolation de triangles (issus des points interpolés) à chaque étape, entre le début et la fin
		List<List<Triangle>> trianglesInterpoles = interpolation.interpolerTriangles(pointsDepart, pointsFin, triangulation, nbEtapes);
		
		//permet d'avoir un dossier avec les différentes étapes de notre triangulation
		//pratique pour vérifier la triangulation sur les différentes étapes (debugging)
		for (int i = 0; i < pointsInterpoles.size(); i++) {
			List<Point> pointsEtape = pointsInterpoles.get(i);
			List<Triangle> trianglesEtape = trianglesInterpoles.get(i);
			String nomFichier = System.currentTimeMillis() + ".jpg";
			File fichierDeSortie = new File(cheminDelaunayInter + nomFichier);
			td.dessinerEtapesTriangulation(pointsEtape, trianglesEtape, fichierDeSortie, tailleIMGMax);
		}
	}
	    
	/**
     * Fonction qui effectue le morphisme (permet d'alléger la main fonction et de séparer le code)
     * @param tailleIMGMax   
     * @param pointsDepart
	 * @param pointsFin
	 * @param pointsInterpoles
	 * @param trianglesInterpoles
	 * @param nbEtapes
	 * @param imgDepart
	 * @param imgFin
     */
	protected static void effectuerMorphisme(int tailleIMGMax, List<Point> pointsDepart, List<Point> pointsFin, List<List<Point>> pointsInterpoles, List<List<Triangle>> trianglesInterpoles, int nbEtapes, BufferedImage imgDepart,BufferedImage imgFin) {
		MorphismeComplexe mc = new MorphismeComplexe();
		ColorationIMGComplexe coloration = new ColorationIMGComplexe();
		
		//pour chaque étape, une interpolation des points et triangles, et la coloration des pixels
		for (int i = 0; i < pointsInterpoles.size(); i++) {
			List<Point> pointsEtape = pointsInterpoles.get(i);
			List<Triangle> trianglesEtape = trianglesInterpoles.get(i);
			List<Triangle> trianglesFin = trianglesInterpoles.get(nbEtapes);
			BufferedImage imgIntermediaire = coloration.getImageInter(tailleIMGMax, imgDepart, imgFin, pointsDepart, trianglesFin, pointsEtape, trianglesEtape, (double) i / nbEtapes);
			mc.sauvegarderMorphisme(imgIntermediaire, System.currentTimeMillis() + ".jpg");
		}
	}
	    
	/**
     * Fonction qui save les images créées 
     * @param img   
     * @param nomFichier 
     */
	protected void sauvegarderMorphisme(BufferedImage img, String nomFichier) {
		File fichierDeSortie = new File(cheminRepertoire + nomFichier);
		try {
			ImageIO.write(img, "jpg", fichierDeSortie);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Fonction qui principale du morphisme   
     * @param pointsDepart   
     * @param pointsFin 
	 * @param nbEtapes
	 * @param imageG
	 * @param imageD
     */
	public static void realiserMorphisme(List<Point> pointsDepart, List<Point> pointsFin, int nbEtapes, BufferedImage imageG, BufferedImage imageD) {

        //TRIANGULATION DE DELAUNAY (Utilise la classe du même nom)
        effectuerTriangulation(300, pointsDepart, pointsFin, nbEtapes);
        
        //MORPHISME D'IMAGE (Utilise la classe InterpolationIMGComplexe et ColorationIMGComplexe)  
        //on initialise le nécessaire pour le morphisme
        InterpolationIMGComplexe interpolation = new InterpolationIMGComplexe();
        List<List<Point>> pointsInterpoles = interpolation.interpolerPoints(pointsDepart, pointsFin, nbEtapes);
        List<Triangle> triangulation = new TriangulationDelaunay().trianguler(pointsDepart);
        List<List<Triangle>> trianglesInterpoles = interpolation.interpolerTriangles(pointsDepart, pointsFin, triangulation, nbEtapes);
        //morphisme
        effectuerMorphisme(300, pointsDepart, pointsFin, pointsInterpoles, trianglesInterpoles, nbEtapes, imageG, imageD);
	}
}