import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Forme {
	private Point[] points; 
	
	public Forme(Point[] points) {
		this.setPoints(points); 
	}

	public Point[] getPoints() {
		return points;
	}

	public void setPoints(Point[] points) {
		this.points = points;
	}

	public void dessiner(GraphicsContext gc) {
		if (points.length < 2) {
			return; 
		}

		gc.beginPath();
		gc.moveTo(points[0].getX(), points[0].getY());
	
		//relie chaque point avec le précédent
		for (int i = 1; i < points.length; i++) {
			gc.lineTo(points[i].getX(), points[i].getY());
		}
		//relie dernier point au premier 
		gc.closePath();

		gc.setFill(Color.GREEN); 
		gc.fill();
	
		//contour
		gc.setStroke(Color.BLACK); 
		gc.setLineWidth(2); //épaisseur trait
		gc.stroke();
	}


}
