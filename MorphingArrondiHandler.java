package controleurs;

import utilitaires.*;

import java.io.File;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 * Classe MorphingArrondiHandler
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class MorphingArrondiHandler extends MorphingAbstract implements EventHandler<ActionEvent> {
    private PointsControleIntermediairesPlacerHandler controleur; 

    public MorphingArrondiHandler(TextField champEtapes, TextField champDelai, ImageView imageGauche, PointsControleIntermediairesPlacerHandler controleur) {
        super(champEtapes, champDelai, imageGauche); 
        this.controleur = controleur; 
    }

    @Override
    public void handle(ActionEvent event) {
    	
        Thread thread1 = new Thread(() -> {		//CrÈation de la fenÍtre
           	
            Platform.runLater(() -> {	//gËre le Fx, Áa fonctionne pas sans Áa
            

            	AttenteFinMorphisme.creerFenetre();
            	
            });
     	
        });
        
        
        Thread thread2 = new Thread(() -> {		//Traitement du morphisme, se fait une fois que la fenÍtre est crÈÈ
            try {
                thread1.join();  //Attend que thread1 se termine
                

                long tempsDepart = System.currentTimeMillis();
                int nbEtapes = Integer.parseInt(getChampEtapes().getText());
                int delai = Integer.parseInt(getChampDelai().getText());

                dossierFormeSimples(); 
                javafx.scene.image.Image image = getImageGauche().getImage();

                //si image non nulle, recupere le chemin de l'image et le stocke (en enlevant le d√©but de la chaine 'file:\\'
                if (image != null) {
                    String imagePath = image.getUrl();
                    
                    File file = new File(imagePath);
                    String cheminImage = file.getPath();
                    
                    cheminImage = cheminImage.replace("%20", " ").replace("\\", "\\\\");	//A ENLEVER SI PB CHEMIN IMAGE
                    setImagePath(cheminImage.substring("file:\\".length()));
                }

                ImageM imageBase = new ImageM(getImagePath()); 

                List<int[]> listeCouleur = getNombreCouleur(imageBase); 

                Map<Character, Point> pointsCalculesImageDebut = traceCourbeBezier(PointsControleIntermediairesPlacerHandler.getPointsMorphingDebut()); 
                colorFormeComplet(nbEtapes, imageBase, listeCouleur, pointsCalculesImageDebut); 

                System.out.println("Morphing termin√© !");
                
                
                //calcul temps morphing en secondes 
                long tempsFin = System.currentTimeMillis();
                double tempsMorphing = (tempsFin - tempsDepart) / 1000.0;
                System.out.println("Temps de morphing : " + tempsMorphing + " s");              
                
                //convertit les images en gif
                Platform.runLater(() -> {			//gËre le Fx, Áa fonctionne pas sans Áa
  
                    try {
                        ConvertisseurGIF convertisseur = new ConvertisseurGIF();
                        convertisseur.convertirEnGif(delai, "./Formes");
                        controleur.handleReset(event);
                    }
                    catch(Exception exceptionGIF){
                        System.err.println("Erreur lors de la mise en GIF");
                    }
                	
                 
                });
                
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
       
        
        
        Thread thread3 = new Thread(() -> {		//Ferme la fenÍtre une fois que le traitement a ÈtÈ effectuÈ
            try {
            	
                thread2.join();  	// Attend que thread2 termine    
                
                Platform.runLater(() -> {	//gËre le Fx, Áa fonctionne pas sans Áa
    
                	AttenteFinMorphisme.fermerFenetre();
                });
 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
    	
            	
        thread1.start();
        thread2.start();
        thread3.start();
        
        
    }

    @Override 
    protected void calculEnsemblePointSuivant(int nbEtapes) {
        for (Map.Entry<Character, Point> entry : PointsControleIntermediairesPlacerHandler.getPointsMorphingDebut().entrySet()) {
            Character clef = entry.getKey();
            Point pointDebut = entry.getValue();
            Point pointFin = PointsControleIntermediairesPlacerHandler.getPointsMorphingFin().get(clef);
            calculPointSuivant(pointDebut, pointFin, nbEtapes);
        }
    }

    /**
     * Fonction qui calcule les coordonn√©es des points interm√©diaires pour tracer les courbes de b√©zier 
     * @param pointsControle
     * @return retourne une map qui contient les points interm√©diaires
     */
    public static Map<Character, Point> traceCourbeBezier(Map<Character, Point> pointsControle) {
        Map<Character, Point> pointsCalcules = new HashMap<>(); 
        Character lettre = 'A';

        Character[] clef = pointsControle.keySet().toArray(new Character[0]);
        int numSegments = clef.length / 4;

        for (int i = 0; i < numSegments; i++) {
            Point p0 = pointsControle.get(clef[i * 4]);
            Point p1 = pointsControle.get(clef[i * 4 + 1]);
            Point p2 = pointsControle.get(clef[i * 4 + 2]);
            Point p3 = pointsControle.get(clef[i * 4 + 3]);

            //calculer et tracer les points interm√©diaires
            double step = 0.01; //intervalle pour l'√©chantillonnage

            for (double t = step; t <= 1.0; t += step) {
                double x = Math.pow(1 - t, 3) * p0.getX() + 3 * Math.pow(1 - t, 2) * t * p1.getX() + 3 * (1 - t) * Math.pow(t, 2) * p2.getX() + Math.pow(t, 3) * p3.getX();
                double y = Math.pow(1 - t, 3) * p0.getY() + 3 * Math.pow(1 - t, 2) * t * p1.getY() + 3 * (1 - t) * Math.pow(t, 2) * p2.getY() + Math.pow(t, 3) * p3.getY();
                pointsCalcules.put(lettre++, new Point(x, y));
            }
        }
        return pointsCalcules; 
    }
}