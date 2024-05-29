package utilitaires;

/**
 * Classe Cercle
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class Cercle {
	private Point centre;
	private double rayon;

   	public Cercle(Point centre, double rayon) {
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