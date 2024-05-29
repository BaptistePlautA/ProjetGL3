package utilitaires;
import java.util.Objects;

/**
 * Classe Arete
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class Arete {
    private Point p0;
    private Point p1;

    public Arete(Point p0, Point p1) {
        this.p0 = p0;
        this.p1 = p1;
    }
  
    public Point getP0() {
        return p0;
    }

    public Point getP1() {
        return p1;
    }
  
    public boolean contient(Point p) {
	    //check si un point est contenu dans une arête
        return p.equals(p0) || p.equals(p1);
    }
  
    @Override
    public boolean equals(Object o) {
        Arete a = (Arete) o; 
        return (this.p0.equals(a.p0) && this.p1.equals(a.p1)) || (this.p0.equals(a.p1) && this.p1.equals(a.p0));
    }
  
    @Override
    public int hashCode() {
        return Objects.hash(p0) + Objects.hash(p1);
    }
  
    @Override
    public String toString() {
        return "Arête : (" +"p0=" + p0 +", p1=" + p1 +')';
    }
}