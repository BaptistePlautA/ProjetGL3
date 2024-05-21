import javax.imageio.ImageIO;
import java.util.List;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

public class TriangulationDelaunay {
    final static String cheminRepertoire = "./ImageMorphIntermediaire/";
    final static String cheminDelaunayInter = "./TriangulationDelaunay/";

    protected Triangle creerSuperTriangle(List<Point> points) {
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (Point point : points) {
            if (point.getX() < minX) minX = point.getX();
            if (point.getY() < minY) minY = point.getY();
            if (point.getX() > maxX) maxX = point.getX();
            if (point.getY() > maxY) maxY = point.getY();
        }

        double dx = (maxX - minX) * 10;
        double dy = (maxY - minY) * 10;

        Point p0 = new Point(minX - dx, minY - dy * 3);
        Point p1 = new Point(minX - dx, maxY + dy);
        Point p2 = new Point(maxX + dx * 3, maxY + dy);

        return new Triangle(p0, p1, p2);
    }

    protected List<Triangle> trianguler(List<Point> points) {
        Triangle superTriangle = creerSuperTriangle(points);
        List<Triangle> triangles = new ArrayList<>();
        triangles.add(superTriangle);

        for (Point point : points) {
            List<Arete> polygone = new ArrayList<>();
            List<Triangle> mauvaisTriangles = new ArrayList<>();

            for (Triangle triangle : triangles) {
                if (triangle.dansCercleCirconscrit(point)) {
                    mauvaisTriangles.add(triangle);
                    polygone.add(new Arete(triangle.p0, triangle.p1));
                    polygone.add(new Arete(triangle.p1, triangle.p2));
                    polygone.add(new Arete(triangle.p2, triangle.p0));
                }
            }

            triangles.removeAll(mauvaisTriangles);

            polygone = arretesUniques(polygone);

            for (Arete arete : polygone) {
                triangles.add(new Triangle(arete.p0, arete.p1, point));
            }
        }

        triangles.removeIf(triangle -> triangle.p0.equals(superTriangle.p0) || triangle.p0.equals(superTriangle.p1) ||
                                         triangle.p0.equals(superTriangle.p2) || triangle.p1.equals(superTriangle.p0) ||
                                         triangle.p1.equals(superTriangle.p1) || triangle.p1.equals(superTriangle.p2) ||
                                         triangle.p2.equals(superTriangle.p0) || triangle.p2.equals(superTriangle.p1) ||
                                         triangle.p2.equals(superTriangle.p2));

        return triangles;
    }

    protected List<Arete> arretesUniques(List<Arete> aretes) {
        List<Arete> arretesUniques = new ArrayList<>();
        for (Arete arete : aretes) {
            boolean estUnique = true;
            for (Arete autreArete : aretes) {
                if (arete != autreArete && arete.equals(autreArete)) {
                    estUnique = false;
                    break;
                }
            }
            if (estUnique) {
                arretesUniques.add(arete);
            }
        }
        return arretesUniques;
    }
    
    ///Dessiner les triangulation de Delaunay à chaque étapes de l'interpolation
    protected void dessinerImagesInter(List<Point> points, List<Triangle> triangles, String nomFichier, int tailleIMGMax) {
        int largeur = tailleIMGMax;
        int hauteur = tailleIMGMax;
        BufferedImage image = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, largeur, hauteur);
        
        g2d.setColor(Color.BLACK);
        for (Triangle triangle : triangles) {
            g2d.drawLine((int) triangle.p0.getX(), (int) triangle.p0.getY(), (int) triangle.p1.getX(), (int) triangle.p1.getY());
            g2d.drawLine((int) triangle.p1.getX(), (int) triangle.p1.getY(), (int) triangle.p2.getX(), (int) triangle.p2.getY());
            g2d.drawLine((int) triangle.p2.getX(), (int) triangle.p2.getY(), (int) triangle.p0.getX(), (int) triangle.p0.getY());
        }

        g2d.setColor(Color.RED);
        for (Point point : points) {
            g2d.fillOval((int) point.getX() - 2, (int) point.getY() - 2, 5, 5);
        }

        g2d.dispose();

        File fichierDeSortie = new File(cheminDelaunayInter + nomFichier);

        try {
            ImageIO.write(image, "jpg", fichierDeSortie);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected void dessinerMorphisme(BufferedImage img, String nomFichier) {
        File fichierDeSortie = new File(cheminRepertoire + nomFichier);

        try {
            ImageIO.write(img, "jpg", fichierDeSortie);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean supprimerDossier(File dossier) {
        if (dossier.isDirectory()) {
            File[] fichiers = dossier.listFiles();
            if (fichiers != null) {
                for (File fichier : fichiers) {
                    if (!supprimerDossier(fichier)) {
                        return false;
                    }
                }
            }
        }
        return dossier.delete();
    }

    protected Triangle trouverTriangle(List<Triangle> triangles, Point p) {
        for (Triangle triangle : triangles) {
            if (triangle.contientPoint(p)) {
                return triangle;
            }
        }
        return null;
    }

    private static double[] calculerCoordonneesBarycentriques(Triangle triangle, Point p) {
        double x0 = triangle.p0.getX(), y0 = triangle.p0.getY();
        double x1 = triangle.p1.getX(), y1 = triangle.p1.getY();
        double x2 = triangle.p2.getX(), y2 = triangle.p2.getY();

        double detT = (y1 - y2) * (x0 - x2) + (x2 - x1) * (y0 - y2);
        double l1 = ((y1 - y2) * (p.getX() - x2) + (x2 - x1) * (p.getY() - y2)) / detT;
        double l2 = ((y2 - y0) * (p.getX() - x2) + (x0 - x2) * (p.getY() - y2)) / detT;
        double l3 = 1.0 - l1 - l2;

        return new double[]{l1, l2, l3};
    }

    protected Point appliquerCoordonneesBarycentriques(Point p0, Point p1, Point p2, double[] coords, List<Point> points) {    
        double x = coords[0] * p0.getX() + coords[1] * p1.getX() + coords[2] * p2.getX();
        double y = coords[0] * p0.getY() + coords[1] * p1.getY() + coords[2] * p2.getY();
        return new Point(x, y);
    }

    protected Color getCouleur(BufferedImage img, Point p) {
        int x = (int) p.getX();
        int y = (int) p.getY();
        
        //Pas nécessaire (en théorie) vu qu'on scale l'image en entrée mais vaut mieux prévenir que guérir
        if (x < 0 || x >= img.getWidth() || y < 0 || y >= img.getHeight()) {
            return Color.BLACK;
        }
        return new Color(img.getRGB(x, y));
    }
    
    protected void morphImages(BufferedImage imgDepart, BufferedImage imgFin, List<List<Point>> pointsInterpoles, int NBetapes, List<Point> pointsDepart, List<Point> pointsFin, int tailleIMGMax) {
        for (int i = 0; i <= NBetapes; i++) {
            double ratio = (double) i / NBetapes;
            List<Point> pointsEtape = pointsInterpoles.get(i);
            List<Triangle> trianglesIntermediaires = trianguler(pointsEtape);
            
            BufferedImage imgEtape = getImageInter(imgDepart, imgFin, pointsDepart, pointsFin, pointsEtape, trianglesIntermediaires, ratio);
            
            dessinerMorphisme(imgEtape, System.currentTimeMillis() + ".jpg");
        }
    }

    protected BufferedImage getImageInter(BufferedImage imgDepart, BufferedImage imgFin, List<Point> pointsDepart, List<Point> pointsFin, List<Point> pointsEtape, List<Triangle> trianglesIntermediaires, double ratio) {
        int largeur = imgDepart.getWidth();
        int hauteur = imgDepart.getHeight();
        BufferedImage imgEtape = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                Point p = new Point(x, y);
                Triangle triangleIntermediaire = trouverTriangle(trianglesIntermediaires, p);
                if (triangleIntermediaire != null) {
                    double[] coords = calculerCoordonneesBarycentriques(triangleIntermediaire, p);
                    Triangle triangleDepart = trouverTriangle(trianguler(pointsDepart), p);
                    Triangle triangleFin = trouverTriangle(trianguler(pointsFin), p);
                    if (triangleDepart != null && triangleFin != null) {
                        Point pDepart = appliquerCoordonneesBarycentriques(triangleDepart.p0, triangleDepart.p1, triangleDepart.p2, coords, pointsDepart);
                        Point pFin = appliquerCoordonneesBarycentriques(triangleFin.p0, triangleFin.p1, triangleFin.p2, coords, pointsFin);
                        Color couleurDepart = getCouleur(imgDepart, pDepart);
                        Color couleurFin = getCouleur(imgFin, pFin);
                        int r = (int) ((1 - ratio) * couleurDepart.getRed() + ratio * couleurFin.getRed());
                        int g = (int) ((1 - ratio) * couleurDepart.getGreen() + ratio * couleurFin.getGreen());
                        int b = (int) ((1 - ratio) * couleurDepart.getBlue() + ratio * couleurFin.getBlue());
                        imgEtape.setRGB(x, y, new Color(r, g, b).getRGB());
                    }
                }
            }
        }

        return imgEtape;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedImage imgDepart = ImageIO.read(new File("./image2.jpg"));
        BufferedImage imgFin = ImageIO.read(new File("./image3.jpg"));
        List<Point> pointsDepart = new ArrayList<>();
        List<Point> pointsFin = new ArrayList<>();

        int tailleIMGMax = 300;
        int NBetapes = 15;

        File repertoire = new File(cheminRepertoire);
        if (repertoire.exists()) {
            supprimerDossier(repertoire);
            repertoire.mkdirs();
        } else {
            repertoire.mkdirs();
        }
        File repertoire2 = new File(cheminDelaunayInter);
        if (repertoire2.exists()) {
            supprimerDossier(repertoire2);
            repertoire2.mkdirs();
        } else {
        	repertoire2.mkdirs();
        }
        
        pointsDepart.add(new Point(0, 0));
        pointsDepart.add(new Point(tailleIMGMax, 0));
        pointsDepart.add(new Point(tailleIMGMax, tailleIMGMax));
        pointsDepart.add(new Point(0, tailleIMGMax));
        
        pointsDepart.add(new Point(118, 3));
        pointsDepart.add(new Point(83, 92));
        pointsDepart.add(new Point(175, 86));
        pointsDepart.add(new Point(108, 70));
        pointsDepart.add(new Point(142, 67));
        pointsDepart.add(new Point(125, 84));
        pointsDepart.add(new Point(135, 132));
        pointsDepart.add(new Point(61, 150));
        pointsDepart.add(new Point(240, 149));
        pointsDepart.add(new Point(298, 283));
        pointsDepart.add(new Point(2, 281));
        pointsDepart.add(new Point(100, 268));
        pointsDepart.add(new Point(214, 268));

        pointsFin.add(new Point(0, 0));
        pointsFin.add(new Point(tailleIMGMax, 0));
        pointsFin.add(new Point(tailleIMGMax, tailleIMGMax));
        pointsFin.add(new Point(0, tailleIMGMax));
        pointsFin.add(new Point(117, 1));
        pointsFin.add(new Point(92, 82));
        pointsFin.add(new Point(163, 74));
        pointsFin.add(new Point(102, 51));
        pointsFin.add(new Point(139, 41));
        pointsFin.add(new Point(119, 58));
        pointsFin.add(new Point(124, 101));
        pointsFin.add(new Point(40, 154));
        pointsFin.add(new Point(245, 143));
        pointsFin.add(new Point(292, 280));
        pointsFin.add(new Point(27, 270));
        pointsFin.add(new Point(131, 282));
        pointsFin.add(new Point(193, 285));

        ///TRIANGULATION DELAUNAY A PART
        TriangulationDelaunay td2 = new TriangulationDelaunay();
        // Interpolation
        InterpolationDelaunay interpolation2 = new InterpolationDelaunay();
        List<List<Point>> pointsInterpoles2 = interpolation2.interpolerPoints(pointsDepart, pointsFin, NBetapes);

        // Sauvegarde et morphing des images intermédiaires
        for (int i = 0; i <= NBetapes; i++) {
            List<Point> pointsEtape = pointsInterpoles2.get(i);
            List<Triangle> trianglesIntermediaires = td2.trianguler(pointsEtape);

            // Sauvegarder de l'image de triangulation intermédiaire (Pas nécessaire mais important pour vérifier le fonctionnement)
            td2.dessinerImagesInter(pointsEtape, trianglesIntermediaires, System.currentTimeMillis() + ".jpg", tailleIMGMax);
        }
        ////FIN TESTS TRIANGULATION
        
        //MORPHING
        TriangulationDelaunay td = new TriangulationDelaunay();
        InterpolationDelaunay interpolation = new InterpolationDelaunay();

        // Calcul des points interpolés pour chaque étape
        List<List<Point>> pointsInterpoles = interpolation.interpolerPoints(pointsDepart, pointsFin, NBetapes);

        // Morphing des images intermédiaires
        td.morphImages(imgDepart, imgFin, pointsInterpoles, NBetapes,pointsDepart,pointsFin,tailleIMGMax);
        //FIN MORPHING

        
        System.out.println("Creation des etapes intermediaires terminee.");
    }
}
