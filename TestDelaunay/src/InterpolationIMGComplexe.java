import java.util.ArrayList;
import java.util.List;

public class InterpolationIMGComplexe {
	protected List<List<Point>> interpolerPoints(List<Point> pointsDepart, List<Point> pointsFin, int nbEtapes) {
	        List<List<Point>> pointsInterpoles = new ArrayList<>();
	        for (int i = 0; i <= nbEtapes; i++) {
	            double ratio = (double) i / nbEtapes;
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
	        for (int i = 0; i <= nbEtapes; i++) {
	            double ratio = (double) i / nbEtapes;
	            List<Point> pointsInterpoles = new ArrayList<>();
	            for (int j = 0; j < pointsDepart.size(); j++) {
	                double x = interpolerDebutFin(pointsDepart.get(j).getX(), pointsFin.get(j).getX(), ratio);
	                double y = interpolerDebutFin(pointsDepart.get(j).getY(), pointsFin.get(j).getY(), ratio);
	                pointsInterpoles.add(new Point(x, y, pointsDepart.get(j).getIndex()));
	            }
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
	        Point pointDepart = pointsDepart.get(sommet.getIndex());
	        Point pointFin = pointsFin.get(sommet.getIndex());
	        double x = interpolerDebutFin(pointDepart.getX(), pointFin.getX(), ratio);
	        double y = interpolerDebutFin(pointDepart.getY(), pointFin.getY(), ratio);
	        return new Point(x, y, sommet.getIndex());
	    }

	protected double interpolerDebutFin(double debut, double fin, double ratio) {
	        return debut + (fin - debut) * ratio;
	    }
}
