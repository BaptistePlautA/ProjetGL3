package utilitaires;

import java.io.File;
import java.util.Map;

import controleurs.PointsControleHandler;
import javafx.scene.control.TextField;

public abstract class MorphingAbstract {

    private TextField champEtapes;
    private TextField champDelai;

    public MorphingAbstract(TextField champEtapes, TextField champDelai) {
        this.champEtapes = champEtapes;
        this.champDelai = champDelai;
    }

    public TextField getChampEtapes(){
        return champEtapes; 
    }

    public TextField getChampDelai(){
        return champDelai; 
    }
    
    protected void calculEnsemblePointSuivant(int nbEtapes) {
        for (Map.Entry<Character, Point> entry : PointsControleHandler.getPointsControleDebut().entrySet()) {
            Character key = entry.getKey();
            Point pointDebut = entry.getValue();
            Point pointFin = PointsControleHandler.getPointsControleFin().get(key);
            calculPointSuivant(pointDebut, pointFin, nbEtapes);
        }
    }

    protected void calculPointSuivant(Point pointDebut, Point pointFin, int nbEtapes) {
        double diffX = pointFin.getX()-pointDebut.getX();
        double diffY = pointFin.getY()-pointDebut.getY();
        
        if(diffX>= 0) {
        	double ajoutX = diffX/nbEtapes;
        	pointDebut.setX(pointDebut.getX()+ajoutX);
        }
        else {
        	double retraitX = (-diffX)/nbEtapes;
        	pointDebut.setX(pointDebut.getX()-retraitX);
        }
        if(diffY>= 0) {
        	double ajoutY = diffY/nbEtapes;
        	pointDebut.setY(pointDebut.getY()+ajoutY);
        }
        else {
        	double retraitY = (-diffY)/nbEtapes;
        	pointDebut.setY(pointDebut.getY()-retraitY);
        }
    }

    protected void dossierFormeSimples(){
        File dossier = new File("./FormesSimples");
        // Vérifier si le dossier existe
        if (dossier.exists() && dossier.isDirectory()) {
            //suppression et création du dossier
            System.out.println("Le dossier existe.");
            supprimerDossier(dossier);
            dossier.mkdirs();
        } else {
            System.out.println("Le dossier n'existe pas.");
            //création du dossier
            dossier.mkdirs();
            System.out.println("Le dossier a été créé");
        }
    }
    
    //permet de supprimer un dossier
    protected boolean supprimerDossier(File dossier) {
        if (dossier.isDirectory()) {
            // Récupérer la liste des fichiers et sous-dossiers du dossier
            File[] fichiers = dossier.listFiles();
            if (fichiers != null) {
                for (File fichier : fichiers) {
                    // Récursivement supprimer chaque fichier ou sous-dossier
                    if (!supprimerDossier(fichier)) {
                        return false; // Arrêter si la suppression échoue pour l'un des fichiers
                    }
                }
            }
        }
        // Supprimer le dossier lui-même après avoir supprimé son contenu
        return dossier.delete();
    }
    
}
