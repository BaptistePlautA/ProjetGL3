import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ColorationIMGComplexe {
	//DEBUT COLORATION
    private static double aire(double x1, double y1, double x2, double y2, double x3, double y3) {
        return Math.abs((x1*(y2-y3) + x2*(y3-y1) + x3*(y1-y2)) / 2.0);
    }

    private static double[] calculerCoordonneesBarycentriques(Triangle triangle, Point p) {
    	//On supprime les artefacts visuels le plus possibles
    	double artefact = 0.01;
        double x1 = triangle.getP0().getX(), y1 = triangle.getP0().getY();
        double x2 = triangle.getP1().getX(), y2 = triangle.getP1().getY();
        double x3 = triangle.getP2().getX(), y3 = triangle.getP2().getY();

        double px = p.getX(), py = p.getY();

        double aireABC = aire(x1, y1, x2, y2, x3, y3);
        double airePBC = aire(px, py, x2, y2, x3, y3);
        double airePCA = aire(px, py, x3, y3, x1, y1);
        double airePAB = aire(px, py, x1, y1, x2, y2);

        double alpha = airePBC / aireABC;
        double beta = airePCA / aireABC;
        double gamma = airePAB / aireABC;
        
        alpha = Math.max(0, Math.min(1, alpha));
        beta = Math.max(0, Math.min(1, beta));
        gamma = Math.max(0, Math.min(1, gamma));
        if ((alpha + beta + gamma > 1 + artefact) || (alpha < artefact && beta < artefact && gamma < artefact)) {
            return new double[] {0, 0, 0};
        }
        return new double[] {alpha,beta,gamma};
    }

    protected Point appliquerCoordonneesBarycentriques(int tailleIMGMax, Point p0, Point p1, Point p2, double[] coords) {    
        double x = coords[0] * p0.getX() + coords[1] * p1.getX() + coords[2] * p2.getX();
        double y = coords[0] * p0.getY() + coords[1] * p1.getY() + coords[2] * p2.getY();
        
        if (x < 0 || x > tailleIMGMax || y < 0 || y > tailleIMGMax) {
        //DEBUG
    	System.out.println("x : " + x + " y : " + y);
        }
        return new Point(x, y);
    }

    protected Pixel getCouleur(BufferedImage img, Point p) {
        int x = (int) p.getX();
        int y = (int) p.getY();
        
        if (x < 0 || x >= img.getWidth() || y < 0 || y >= img.getHeight()) {
            System.out.println("Pixel en dehors x : " + x + " y : " + y);
            return new Pixel(255, 255, 255);  //On met un pixel noir si un point est hors de l'image
        }
        
        int rgb = img.getRGB(x, y);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        
        return new Pixel(r, g, b);
    }
    
    protected Triangle trouverTriangle(List<Triangle> triangles, Point p) {
        for (Triangle triangle : triangles) {
            if (triangle.contientPoint(p)) {
                return triangle;
            }
        }
        return null;
    }
    
    protected BufferedImage getImageInter(int tailleIMGMax,BufferedImage imgDepart, BufferedImage imgFin, List<Point> pointsDepart, List<Triangle> trianglesFin, List<Point> pointsEtape, List<Triangle> trianglesIntermediaires, double ratio) {
        int largeur = tailleIMGMax;
        int hauteur = tailleIMGMax;

        BufferedImage imgEtape = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_RGB);
        
        //System.out.println("Depart" + pointsDepart.toString());
        //System.out.println("Arrivee" + pointsFin.toString());
        
        //on triangule uniquement les POINTS DE DEPART (pas l'arrivée non plus sous risque d'avoir un morphing déformé)
        TriangulationDelaunay tdepart = new TriangulationDelaunay();
        List<Triangle> trianglesDepart = tdepart.trianguler(pointsDepart);

        //DEBUG
        //System.out.println("TrianglesDepart: " + trianglesDepart.toString());
        //System.out.println("TrianglesFin: " + trianglesFin.toString());
        //System.out.println("TrianglesIntermediaires: " + trianglesIntermediaires.toString());

        //DEBUG²
        if (trianglesDepart.size() != trianglesFin.size() || trianglesDepart.size() != trianglesIntermediaires.size()) {
            String errorMessage = String.format("Pas le meme nombre de triangles de depart et de fin !"
            		+ " points trop proches du bord (?): "
                    + "\n trianglesDepart=%d, trianglesFin=%d, trianglesIntermediaires=%d",
                    trianglesDepart.size(), trianglesFin.size(), trianglesIntermediaires.size());
            throw new IllegalArgumentException(errorMessage);
        }

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                Point pixel = new Point(x, y);
                Triangle triangleIntermediaire = trouverTriangle(trianglesIntermediaires, pixel);
                if (triangleIntermediaire != null) {
                    double[] coords = calculerCoordonneesBarycentriques(triangleIntermediaire, pixel);

                    int triangleIndex = trianglesIntermediaires.indexOf(triangleIntermediaire);
                    if (triangleIndex < 0 || triangleIndex >= trianglesDepart.size()) {
                        continue;
                    }

                    Triangle triangleDepart = trianglesDepart.get(triangleIndex);
                    Triangle triangleFin = trianglesFin.get(triangleIndex);
                    //DEBUG
                    //System.out.println("TrianglesDepart: " + triangleDepart.toString());
                    //System.out.println("TrianglesFin: " + triangleFin.toString());

                    if (triangleDepart != null && triangleFin != null) {
                        Point pDepart = appliquerCoordonneesBarycentriques(tailleIMGMax,triangleDepart.getP0(), triangleDepart.getP1(), triangleDepart.getP2(), coords);
                        Point pFin = appliquerCoordonneesBarycentriques(tailleIMGMax,triangleFin.getP0(), triangleFin.getP1(), triangleFin.getP2(), coords);

                        Pixel couleurDepart = getCouleur(imgDepart, pDepart);
                        Pixel couleurFin = getCouleur(imgFin, pFin);

                        int r = (int) ((1 - ratio) * couleurDepart.getR() + ratio * couleurFin.getR());
                        int g = (int) ((1 - ratio) * couleurDepart.getV() + ratio * couleurFin.getV());
                        int b = (int) ((1 - ratio) * couleurDepart.getB() + ratio * couleurFin.getB());

                        imgEtape.setRGB(x, y, new Color(r, g, b).getRGB());
                    }
                }
            }
        }

        return imgEtape;
    }
    ///FIN COLORATION
}
