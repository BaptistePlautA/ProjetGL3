package utilitaires;

import java.util.List;
import java.util.ArrayList;

/**
 * Classe Triangle
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
class Triangle {
    private Point p0, p1, p2;
    private Cercle cercleCirconscrit;
    private int index;
    
    Triangle(Point p0, Point p1, Point p2, int index) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.index = index;
        this.cercleCirconscrit = calculerCercleCirconscrit(p0, p1, p2);
    }
    
    public Point getP0() {
        return p0;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }
    
    public void setP0(Point point) {
		this.p0 = point;
	}

    public void setP1(Point point) {
		this.p1 = point;
	}
    
    public void setP2(Point point) {
		this.p2 = point;
	}
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Fonction qui vérifie si un point est dans le cercle 
     * @param p   
     * @return un true si le point est dedans, false sinon 
     */
    public boolean dansCercleCirconscrit(Point p) {
        double dx = cercleCirconscrit.getCentre().getX() - p.getX();
        double dy = cercleCirconscrit.getCentre().getY() - p.getY();
        return Math.sqrt(dx * dx + dy * dy) <= cercleCirconscrit.getRayon();
    }

    /**
     * Fonction qui calcule le cercle circonscrit  
     * @param p0
     * @param p1
     * @param p2
     * @return le cercle
     */
    public Cercle calculerCercleCirconscrit(Point p0, Point p1, Point p2) {
    	//Dans la Triangulation de Delaunay (pas visible par l'utilisateur, ni tracé sur la triangulation)
        double ax = p0.getX(), ay = p0.getY();
        double bx = p1.getX(), by = p1.getY();
        double cx = p2.getX(), cy = p2.getY();

        double d = 2 * (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by));
        if (d == 0) {
        	//System.out.println(ax + bx + cx + ay + by + cy);
            throw new IllegalArgumentException("Les points sont colinéaires");
        }
        double ux = ((ax * ax + ay * ay) * (by - cy) + (bx * bx + by * by) * (cy - ay) + (cx * cx + cy * cy) * (ay - by)) / d;
        double uy = ((ax * ax + ay * ay) * (cx - bx) + (bx * bx + by * by) * (ax - cx) + (cx * cx + cy * cy) * (bx - ax)) / d;
        
        Point centre = new Point(ux, uy);
        double rayon = Math.sqrt((ux - ax) * (ux - ax) + (uy - ay) * (uy - ay));
        
        return new Cercle(centre, rayon);
    }

    /**
     * @return List<Arete>
     */
    public List<Arete> aretes() {
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(p0, p1));
        aretes.add(new Arete(p1, p2));
        aretes.add(new Arete(p2, p0));
        return aretes;
    }

    /**
     * Fonction qui vérifie si triangle contient sommet   
     * @param triangle
     * @return true si oui, sinon false 
     */
    public boolean contientSommet(Triangle triangle) {
        return triangle.getP0().equals(p0) || triangle.getP0().equals(p1) || triangle.getP0().equals(p2)
            || triangle.getP1().equals(p0) || triangle.getP1().equals(p1) || triangle.getP1().equals(p2)
            || triangle.getP2().equals(p0) || triangle.getP2().equals(p1) || triangle.getP2().equals(p2);
    }

    /**
     * Fonction qui vérifie si le point est contenu    
     * @param point
     * @return true si oui, sinon false 
     */
    public boolean contientPoint(Point point) {
        double det1 = p0.getY() * p2.getX() - p0.getX() * p2.getY() + (p2.getY() - p0.getY()) * point.getX() + (p0.getX() - p2.getX()) * point.getY();
        double det2 = p0.getX() * p1.getY() - p0.getY() * p1.getX() + (p0.getY() - p1.getY()) * point.getX() + (p1.getX() - p0.getX()) * point.getY();
        
        if ((det1 < 0) != (det2 < 0))
            return false;
        
        double Aire = -p1.getY() * p2.getX() + p0.getY() * (p2.getX() - p1.getX()) + p0.getX() * (p1.getY() - p2.getY()) + p1.getX() * p2.getY();
        
        return Aire < 0 ? (det1 <= 0 && det1 + det2 >= Aire) : (det1 >= 0 && det1 + det2 <= Aire);
    }    
    
    @Override
    public String toString() {
        return "Triangle: " + p0 + ", " + p1 + ", " + p2;
    }

   
}