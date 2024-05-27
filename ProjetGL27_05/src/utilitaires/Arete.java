package utilitaires;
import java.util.Objects;

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
	  //Check si un point est contenu dans une arête
      return p.equals(p0) || p.equals(p1);
  }
  
  //Note à moi même: bien écrire le equals
  //sous peine d'avoir des résultats complètement FAUX :))
  boolean equals(Arete e) {
      return (this.p0.equals(e.p0) && this.p1.equals(e.p1)) || (this.p0.equals(e.p1) && this.p1.equals(e.p0));
  }
  
  @Override
  public int hashCode() {
      return Objects.hash(p0) + Objects.hash(p1);
  }
  
  @Override
  public String toString() {
      return "Arete(" +"p0=" + p0 +", p1=" + p1 +')';
  }
}