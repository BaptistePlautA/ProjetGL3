package controleurs;

import utilitaires.*;

import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

import java.io.File;

import java.util.List;

/**
 * Classe MorphingSimpleHandler
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class MorphingSimpleHandler extends MorphingAbstract implements EventHandler<ActionEvent> {
    private PointsControleHandler controleur; 

    public MorphingSimpleHandler(TextField champEtapes, TextField champDelai, ImageView imageGauche, PointsControleHandler controleur) {
        super(champEtapes, champDelai, imageGauche); 
        this.controleur = controleur; 
    }

    @Override
    public void handle(ActionEvent event) {
    	
    	
        Thread thread1 = new Thread(() -> {		//Cr�ation de la fen�tre
           	
            Platform.runLater(() -> {	//g�re le Fx, �a fonctionne pas sans �a
            

            	AttenteFinMorphisme.creerFenetre();
            	
            });
     	
        });
        
        
        
        Thread thread2 = new Thread(() -> {		//Traitement du morphisme, se fait une fois que la fen�tre est cr��
            try {
                thread1.join();  //Attend que thread1 se termine
                
                
                long tempsDepart = System.currentTimeMillis();
                int nbEtapes = Integer.parseInt(getChampEtapes().getText());
                int delai = Integer.parseInt(getChampDelai().getText());
                
                dossierFormeSimples();
                
                javafx.scene.image.Image image = getImageGauche().getImage();
                
                //si image non nulle, récupère le chemin de l'image et le stocke (en enlevant le début de la chaine 'file:\\'
                
                if (image != null) {
                    String imagePath = image.getUrl();
                    
                    File file = new File(imagePath);
                    String cheminImage = file.getPath();
                
                    cheminImage = cheminImage.replace("%20", " ").replace("\\", "\\\\");	//A ENLEVER SI PB CHEMIN IMAGE
                    setImagePath(cheminImage.substring("file:\\".length()));
             

                }

                ImageM imageBase = new ImageM(getImagePath()); 
                List<int[]> listeCouleur = getNombreCouleur(imageBase); 
                colorFormeComplet(nbEtapes, imageBase, listeCouleur, null); 
                
                //calcul temps morphing en secondes 
                long tempsFin = System.currentTimeMillis();
                double tempsMorphing = (tempsFin - tempsDepart) / 1000.0;
                System.out.println("Temps de morphing : " + tempsMorphing + " s");
  
                
                
                
                
                //convertit les images en gif
                Platform.runLater(() -> {			//g�re le Fx, �a fonctionne pas sans �a
                	
                    ConvertisseurGIF convertisseur = new ConvertisseurGIF();
                    convertisseur.convertirEnGif(delai,  "./Formes");
                    controleur.handleReset(event);
                 
                });
                
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        
        
        
        Thread thread3 = new Thread(() -> {		//Ferme la fen�tre une fois que le traitement a �t� effectu�
            try {
            	
                thread2.join();  	// Attend que thread2 termine    
                
                Platform.runLater(() -> {	//g�re le Fx, �a fonctionne pas sans �a
    
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
}