package utilitaires;

/**
 * Classe Point
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class Point {
	private double x; 
	private double y; 
	private int index;

    public Point(double x, double y, int index) {
        this.x = x;
        this.y = y;
        this.index = index;
    }

	public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public int getIndex() {
        return index;
    }

	public void setY(double y) {
		this.y = y;
	}

	@Override 
	public String toString(){
		return "("+getX()+", "+getY()+")"; 
	}

	@Override
	public boolean equals(Object o){
		Point p = (Point) o; 
		return (this.index == p.getIndex() && this.x == p.x && this.y == p.y);
	}
}
