package utilitaires;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe InterpolationIMGComplexe
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class InterpolationIMGComplexe {
	
	/**
     * Fonction qui interpole les points 
     * @param pointsDepart  
     * @param pointsFin
	 * @param nbEtapes 
     * @return retourne un List<List<Point>>
     */
	protected List<List<Point>> interpolerPoints(List<Point> pointsDepart, List<Point> pointsFin, int nbEtapes) {
		List<List<Point>> pointsInterpoles = new ArrayList<>();
		//calcul de la position de départ de tous les points entre le début et la fin
		for (int i = 0; i <= nbEtapes; i++) {
			//le ratio des pixels à chaque étape pour calculer une transition fluide
			double ratio = (double) i / nbEtapes;
			//on stocke les points à chaque étape, puis on rajoutera cette liste dans la liste de liste de points interpolés
			List<Point> pointsEtape = new ArrayList<>();
			for (int j = 0; j < pointsDepart.size(); j++) {
				double x = interpolerDebutFin(pointsDepart.get(j).getX(), pointsFin.get(j).getX(), ratio);
				double y = interpolerDebutFin(pointsDepart.get(j).getY(), pointsFin.get(j).getY(), ratio);
				pointsEtape.add(new Point(x, y, pointsDepart.get(j).getIndex()));
			}
			pointsInterpoles.add(pointsEtape);
		}
		return pointsInterpoles;
	}

	/**
     * Fonction qui fait l'interpolation pour les triangles (ie morphing d'images)
     * @param pointsDepart  
     * @param pointsFin
	 * @param triangulation
	 * @param nbEtapes 
     * @return retourne un List<List<Triangle>>
     */
	protected List<List<Triangle>> interpolerTriangles(List<Point> pointsDepart, List<Point> pointsFin, List<Triangle> triangulation, int nbEtapes) {
		List<List<Triangle>> trianglesInterpoles = new ArrayList<>();
		//même concept que les points
		//calcul de la position de départ de tous les triangles entre le début et la fin
		for (int i = 0; i <= nbEtapes; i++) {
			//le ratio des pixels à chaque étape pour calculer une transition smoooth
			double ratio = (double) i / nbEtapes;
			List<Point> pointsInterpoles = new ArrayList<>();
			for (int j = 0; j < pointsDepart.size(); j++) {
				double x = interpolerDebutFin(pointsDepart.get(j).getX(), pointsFin.get(j).getX(), ratio);
				double y = interpolerDebutFin(pointsDepart.get(j).getY(), pointsFin.get(j).getY(), ratio);
				pointsInterpoles.add(new Point(x, y, pointsDepart.get(j).getIndex()));
			}
			//on stocke les points et triangles à chaque étape, puis on rajoutera cette liste dans la liste de liste de triangles interpolés
			List<Triangle> trianglesInterpole = new ArrayList<>();
			for (Triangle triangle : triangulation) {
				Point p0 = trouverSommetTriangleInterpole(triangle.getP0(), pointsDepart, pointsFin, ratio);
				Point p1 = trouverSommetTriangleInterpole(triangle.getP1(), pointsDepart, pointsFin, ratio);
				Point p2 = trouverSommetTriangleInterpole(triangle.getP2(), pointsDepart, pointsFin, ratio);
				trianglesInterpole.add(new Triangle(p0, p1, p2, triangle.getIndex()));
			}
			trianglesInterpoles.add(trianglesInterpole);
		}
		return trianglesInterpoles;
	}

	/**
     * Fonction qui trouve le sommet du triangle interpolé 
     * @param sommet  
     * @param pointsDepart
	 * @param pointsFin
	 * @param ratio 
     * @return retourne un Point
     */
	protected Point trouverSommetTriangleInterpole(Point sommet, List<Point> pointsDepart, List<Point> pointsFin, double ratio) {
	    try {
	        int index = sommet.getIndex();
	        //System.out.println("Index du sommet dans trouverSommetTriangleInterpole : " + index);
	        Point pointDepart = pointsDepart.get(index);
	        Point pointFin = pointsFin.get(index);
	        //on check quel point appartient à quel triangle pour ne pas avoir de bug de texture sur le rendu final
	        double x = interpolerDebutFin(pointDepart.getX(), pointFin.getX(), ratio);
	        double y = interpolerDebutFin(pointDepart.getY(), pointFin.getY(), ratio);
	        return new Point(x, y, index);
	    } catch (IndexOutOfBoundsException e) {
	        System.err.println("Erreur dans trouverSommetTriangleInterpole: " + e.getMessage());
	        e.printStackTrace();
	        return null;
	    }
	}
	
	/**
     * Fonction qui calcule les coordonnées de début et de fin de notre point selon le ratio
     * @param debut  
     * @param fin
	 * @param ratio  
     * @return retourne un double 
     */
	protected double interpolerDebutFin(double debut, double fin, double ratio) {
		return debut + (fin - debut) * ratio;
	}
}