package utilitaires;

import java.util.ArrayList;
import java.util.List;

public class InterpolationIMGComplexe {
	
	protected List<List<Point>> interpolerPoints(List<Point> pointsDepart, List<Point> pointsFin, int nbEtapes) {
	        List<List<Point>> pointsInterpoles = new ArrayList<>();
	        //Calcul de la position de départ de tous les points entre le début et la fin
	        for (int i = 0; i <= nbEtapes; i++) {
	        	//Le ratio des pixels à chaque étape pour calculer une transition smoooth
	            double ratio = (double) i / nbEtapes;
	            //On stocke les points à chaque étape, puis on rajoutera cette liste dans la liste de liste de points interpolés
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

	protected List<List<Triangle>> interpolerTriangles(List<Point> pointsDepart, List<Point> pointsFin, List<Triangle> triangulation, int nbEtapes) {
	        List<List<Triangle>> trianglesInterpoles = new ArrayList<>();
	        //Même concept que les points
	        //Calcul de la position de départ de tous les triangles entre le début et la fin
	        for (int i = 0; i <= nbEtapes; i++) {
	        	//Le ratio des pixels à chaque étape pour calculer une transition smoooth
	            double ratio = (double) i / nbEtapes;
	            //Possiblement un moyen de ne pas répêter cette étape mais c'était plus pratique à faire
	            List<Point> pointsInterpoles = new ArrayList<>();
	            for (int j = 0; j < pointsDepart.size(); j++) {
	                double x = interpolerDebutFin(pointsDepart.get(j).getX(), pointsFin.get(j).getX(), ratio);
	                double y = interpolerDebutFin(pointsDepart.get(j).getY(), pointsFin.get(j).getY(), ratio);
	                pointsInterpoles.add(new Point(x, y, pointsDepart.get(j).getIndex()));
	            }
	            //On stocke les points et triangles à chaque étape, puis on rajoutera cette liste dans la liste de liste de triangles interpolés
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

	protected Point trouverSommetTriangleInterpole(Point sommet, List<Point> pointsDepart, List<Point> pointsFin, double ratio) {
	    try {
	        int index = sommet.getIndex();
	        //System.out.println("Index du sommet dans trouverSommetTriangleInterpole : " + index);
	        Point pointDepart = pointsDepart.get(index);
	        Point pointFin = pointsFin.get(index);
	        //On check quel point appartient à quel triangle pour ne pas avoir de bug de texture sur le rendu final
	        double x = interpolerDebutFin(pointDepart.getX(), pointFin.getX(), ratio);
	        double y = interpolerDebutFin(pointDepart.getY(), pointFin.getY(), ratio);
	        return new Point(x, y, index);
	    } catch (IndexOutOfBoundsException e) {
	        System.err.println("Erreur dans trouverSommetTriangleInterpole: " + e.getMessage());
	        e.printStackTrace();
	        return null;
	    }
	}
	
	//Calcul des coordonnées de début et de fin de notre point selon le ratio
	protected double interpolerDebutFin(double debut, double fin, double ratio) {
	        return debut + (fin - debut) * ratio;
	    }
}
