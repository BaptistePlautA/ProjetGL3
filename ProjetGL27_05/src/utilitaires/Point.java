package utilitaires;

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

    public double getY() {
        return y;
    }

    public int getIndex() {
        return index;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean equals(Point p) {
        return (this.index == p.getIndex() && this.x == p.x && this.y == p.y);
    }

    @Override
    public String toString() {
        return "Point(" + x + ", " + y + ", index=" + index + ")";
    }
}
