import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class Inter2 {
	final static String cheminDelaunayInter = "./TriangulationDelaunay/";
	final static String cheminRepertoire = "./ImageMorphIntermediaire/";

    
    
    protected void sauvegarderMorphisme(BufferedImage img, String nomFichier) {
        File fichierDeSortie = new File(cheminRepertoire + nomFichier);

        try {
            ImageIO.write(img, "jpg", fichierDeSortie);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static boolean supprimerDossier(File dossier) {
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
    
    protected static void effectuerTriangulation(int tailleIMGMax, List<Point> pointsDepart, List<Point> pointsFin, int nbEtapes) {
        InterpolationIMGComplexe interpolation = new InterpolationIMGComplexe();
        TriangulationDelaunay td = new TriangulationDelaunay();
        List<Triangle> triangulation = td.trianguler(pointsDepart);
        List<List<Point>> pointsInterpoles = interpolation.interpolerPoints(pointsDepart, pointsFin, nbEtapes);
        List<List<Triangle>> trianglesInterpoles = interpolation.interpolerTriangles(pointsDepart, pointsFin, triangulation, nbEtapes);

        for (int i = 0; i < pointsInterpoles.size(); i++) {
            List<Point> pointsEtape = pointsInterpoles.get(i);
            List<Triangle> trianglesEtape = trianglesInterpoles.get(i);
            String nomFichier = System.currentTimeMillis() + ".jpg";
            File fichierDeSortie = new File(cheminDelaunayInter + nomFichier);
            td.dessinerEtapesTriangulation(pointsEtape, trianglesEtape, fichierDeSortie, tailleIMGMax);
        }
    }
    
    protected static void effectuerMorphisme(int tailleIMGMax, List<Point> pointsDepart, List<Point> pointsFin, List<List<Point>> pointsInterpoles, List<List<Triangle>> trianglesInterpoles, int nbEtapes) {
        BufferedImage imgDepart = null;
        BufferedImage imgFin = null;
        try {
            imgDepart = ImageIO.read(new File("./image2.jpg"));
            imgFin = ImageIO.read(new File("./image3.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Inter2 itp = new Inter2();
        ColorationIMGComplexe coloration = new ColorationIMGComplexe();

        for (int i = 0; i < pointsInterpoles.size(); i++) {
            List<Point> pointsEtape = pointsInterpoles.get(i);
            List<Triangle> trianglesEtape = trianglesInterpoles.get(i);
            List<Triangle> trianglesFin = trianglesInterpoles.get(nbEtapes);
            BufferedImage imgIntermediaire = coloration.getImageInter(tailleIMGMax, imgDepart, imgFin, pointsDepart, trianglesFin, pointsEtape, trianglesEtape, (double) i / nbEtapes);
            itp.sauvegarderMorphisme(imgIntermediaire, System.currentTimeMillis() + ".jpg");
        }
    }
    
    
    public static void main(String[] args) throws IOException {
    	int tailleIMGMax = 300 ; 
        List<Point> pointsDepart = new ArrayList<>();
        List<Point> pointsFin = new ArrayList<>();
        
        File rep = new File(cheminRepertoire);
        if (rep.exists()) {
            supprimerDossier(rep);
            rep.mkdirs();
        } else {
        	rep.mkdirs();
        }
        
        File rep2 = new File(cheminDelaunayInter);
        if (rep2.exists()) {
            supprimerDossier(rep2);
            rep2.mkdirs();
        } else {
        	rep2.mkdirs();
        }

        pointsDepart.add(new Point(0, 0, 0));
        pointsDepart.add(new Point(tailleIMGMax, 0, 1));
        pointsDepart.add(new Point(tailleIMGMax, tailleIMGMax, 2));
        pointsDepart.add(new Point(0, tailleIMGMax, 3));

        pointsDepart.add(new Point(120, 5, 4));
        pointsDepart.add(new Point(83, 92, 5));
        pointsDepart.add(new Point(175, 86, 6));
        pointsDepart.add(new Point(108, 70, 7));
        pointsDepart.add(new Point(142, 67, 8));
        pointsDepart.add(new Point(125, 84, 9));
        pointsDepart.add(new Point(135, 132, 10));
        pointsDepart.add(new Point(61, 150, 11));
        pointsDepart.add(new Point(240, 149, 12));
        pointsDepart.add(new Point(298, 283, 13));
        pointsDepart.add(new Point(10, 280, 14));
        pointsDepart.add(new Point(100, 268, 15));
        pointsDepart.add(new Point(214, 268, 16));

        pointsFin.add(new Point(0, 0, 0));
        pointsFin.add(new Point(tailleIMGMax, 0, 1));
        pointsFin.add(new Point(tailleIMGMax, tailleIMGMax, 2));
        pointsFin.add(new Point(0, tailleIMGMax, 3));
        pointsFin.add(new Point(120, 5, 4));
        pointsFin.add(new Point(92, 82, 5));
        pointsFin.add(new Point(163, 74, 6));
        pointsFin.add(new Point(102, 51, 7));
        pointsFin.add(new Point(139, 41, 8));
        pointsFin.add(new Point(119, 58, 9));
        pointsFin.add(new Point(124, 101, 10));
        pointsFin.add(new Point(40, 154, 11));
        pointsFin.add(new Point(245, 143, 12));
        pointsFin.add(new Point(290, 280, 13));
        pointsFin.add(new Point(30, 270, 14));
        pointsFin.add(new Point(131, 282, 15));
        pointsFin.add(new Point(193, 285, 16));

        int nbEtapes = 15;
        
        //TRIANGULATION DE DELAUNAY (Utilise la classe du même nom)
        effectuerTriangulation(tailleIMGMax, pointsDepart, pointsFin, nbEtapes);
        //TRIANGULATION DE DELAUNAY

        //MORPHISME D'IMAGE (Utilise la classe InterpolationIMGComplexe et ColorationIMGComplexe)  
        // Interpolation des points
        InterpolationIMGComplexe interpolation = new InterpolationIMGComplexe();
        List<List<Point>> pointsInterpoles = interpolation.interpolerPoints(pointsDepart, pointsFin, nbEtapes);
        List<Triangle> triangulation = new TriangulationDelaunay().trianguler(pointsDepart);
        List<List<Triangle>> trianglesInterpoles = interpolation.interpolerTriangles(pointsDepart, pointsFin, triangulation, nbEtapes);
        // Morphisme
        effectuerMorphisme(tailleIMGMax, pointsDepart, pointsFin, pointsInterpoles, trianglesInterpoles, nbEtapes);
        
        //MORPHISME D'IMAGE

        
        GIFConverter convertisseur = new GIFConverter();
        convertisseur.convertirEnGif(100);
        System.out.println("Creation des etapes intermediaires terminee.");
    }
}
