package utilitaires;
public class Cercle {
	 private Point centre;
	 private double rayon;

   Cercle(Point centre, double rayon) {
       this.setCentre(centre);
       this.setRayon(rayon);
   }

	public Point getCentre() {
		return centre;
	}

	public void setCentre(Point centre) {
		this.centre = centre;
	}

	public double getRayon() {
		return rayon;
	}

	public void setRayon(double rayon) {
		this.rayon = rayon;
	}
}