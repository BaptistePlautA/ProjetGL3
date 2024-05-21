class Triangle {
    Point p0, p1, p2;
    Cercle cercleCirconscrit;

    Triangle(Point p0, Point p1, Point p2) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
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


    boolean dansCercleCirconscrit(Point p) {
    	//Check si un point est dans le cercle
        double dx = cercleCirconscrit.getCentre().getX() - p.getX();
        double dy = cercleCirconscrit.getCentre().getY() - p.getY();
        return Math.sqrt(dx * dx + dy * dy) <= cercleCirconscrit.getRayon();
    }

    public Cercle calculerCercleCirconscrit(Point p0, Point p1, Point p2) {
    	//Dans la triangulationTriangulation de Delaunay (pas visible par l'utilisateur)
        double ax = p0.getX(), ay = p0.getY();
        double bx = p1.getX(), by = p1.getY();
        double cx = p2.getX(), cy = p2.getY();

        double d = 2 * (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by));
        double ux = ((ax * ax + ay * ay) * (by - cy) + (bx * bx + by * by) * (cy - ay) + (cx * cx + cy * cy) * (ay - by)) / d;
        double uy = ((ax * ax + ay * ay) * (cx - bx) + (bx * bx + by * by) * (ax - cx) + (cx * cx + cy * cy) * (bx - ax)) / d;
        
        Point centre = new Point(ux, uy);
        double rayon = Math.sqrt((ux - ax) * (ux - ax) + (uy - ay) * (uy - ay));
        
        return new Cercle(centre, rayon);
    }
    

    public boolean contientPoint(Point point) {
        double s = p0.getY() * p2.getX() - p0.getX() * p2.getY() + (p2.getY() - p0.getY()) * point.getX() + (p0.getX() - p2.getX()) * point.getY();
        double t = p0.getX() * p1.getY() - p0.getY() * p1.getX() + (p0.getY() - p1.getY()) * point.getX() + (p1.getX() - p0.getX()) * point.getY();
        
        if ((s < 0) != (t < 0))
            return false;
        
        double A = -p1.getY() * p2.getX() + p0.getY() * (p2.getX() - p1.getX()) + p0.getX() * (p1.getY() - p2.getY()) + p1.getX() * p2.getY();
        
        return A < 0 ?
            (s <= 0 && s + t >= A) :
            (s >= 0 && s + t <= A);
    }    

}