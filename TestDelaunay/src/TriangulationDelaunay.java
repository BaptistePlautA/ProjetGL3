import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class TriangulationDelaunay {
	protected Triangle superTriangle(List<Point> points) {
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (Point point : points) {
            if (point.getX() < minX) minX = point.getX();
            if (point.getY() < minY) minY = point.getY();
            if (point.getX() > maxX) maxX = point.getX();
            if (point.getY() > maxY) maxY = point.getY();
        }

        double dx = (maxX - minX) * 10;
        double dy = (maxY - minY) * 10;

        Point p0 = new Point(minX - dx, minY - dy * 3);
        Point p1 = new Point(minX - dx, maxY + dy);
        Point p2 = new Point(maxX + dx * 3, maxY + dy);
        //On assigne une valeur d'index bidon à notre nouveau triangle
        return new Triangle(p0, p1, p2,-1);
    }
	
	 protected List<Triangle> trianguler(List<Point> points) {
	        Triangle superTriangle = superTriangle(points);
	        List<Triangle> triangles = new ArrayList<>();
	        triangles.add(superTriangle);

	        //On rajoute chaque point un par un pour vérifier la condition à chaque étape
	        for (Point point : points) {
	            List<Triangle> trianglesASupprimer = new ArrayList<>();
	            List<Arete> nouvellesAretes = new ArrayList<>();

	            //1.Parcours des triangles
	            for (Triangle triangle : triangles) {
	                //=> Si le point est à  l'intérieur d'un triangle : on supprime le triangle
	                if (triangle.dansCercleCirconscrit(point)) {
	                    trianglesASupprimer.add(triangle);

	                    nouvellesAretes.add(new Arete(triangle.getP0(), triangle.getP1()));
	                    nouvellesAretes.add(new Arete(triangle.getP1(), triangle.getP2()));
	                    nouvellesAretes.add(new Arete(triangle.getP2(), triangle.getP0()));
	                }
	            }

	            //On dégage tout
	            triangles.removeAll(trianglesASupprimer);
	            //Verif rapide
	            nouvellesAretes = aretesUniques(nouvellesAretes);

	            //2. On a ainsi nos nouveaux triangles qui respectent la condition de la triangulation
	            for (Arete arete : nouvellesAretes) {
	                triangles.add(new Triangle(arete.getP0(), arete.getP1(), point,-1));
	            }
	        }
	        triangles.removeIf(triangle -> triangle.contientSommet(superTriangle));
	        
	        return triangles;
	    }

	 protected List<Arete> aretesUniques(List<Arete> aretes) {
        List<Arete> aretesUniques = new ArrayList<>();
        for (Arete arete : aretes) {
            boolean estUnique = true;
            for (Arete autreArete : aretes) {
                if (arete != autreArete && arete.equals(autreArete)) {
                    estUnique = false;
                    break;
                }
            }
            if (estUnique) {
            	aretesUniques.add(arete);
            }
        }
        return aretesUniques;
    }
	 
	 protected void dessinerEtapesTriangulation(List<Point> pointsEtape, List<Triangle> triangles, File fichierDeSortie, int tailleIMGMax) {
	        int largeur = tailleIMGMax;
	        int hauteur = tailleIMGMax;
	        BufferedImage image = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_RGB);
	        Graphics2D g2d = image.createGraphics();
	        g2d.setColor(Color.WHITE);
	        g2d.fillRect(0, 0, largeur, hauteur);

	        g2d.setColor(Color.BLACK);
	        for (Triangle triangle : triangles) {
	            g2d.drawLine((int) triangle.getP0().getX(), (int) triangle.getP0().getY(), (int) triangle.getP1().getX(), (int) triangle.getP1().getY());
	            g2d.drawLine((int) triangle.getP1().getX(), (int) triangle.getP1().getY(), (int) triangle.getP2().getX(), (int) triangle.getP2().getY());
	            g2d.drawLine((int) triangle.getP2().getX(), (int) triangle.getP2().getY(), (int) triangle.getP0().getX(), (int) triangle.getP0().getY());
	        }
	        
	        g2d.setColor(Color.RED);
	        for (Point point : pointsEtape) {
	            g2d.fillOval((int) point.getX() - 2, (int) point.getY() - 2, 5, 5);
	        }
	        
	        g2d.dispose();

	        try {
	            ImageIO.write(image, "jpg", fichierDeSortie);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}