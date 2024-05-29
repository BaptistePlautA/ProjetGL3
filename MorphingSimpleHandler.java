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
    	
    	
        Thread thread1 = new Thread(() -> {		//Création de la fenêtre
           	
            Platform.runLater(() -> {	//gère le Fx, ça fonctionne pas sans ça
            

            	AttenteFinMorphisme.creerFenetre();
            	
            });
     	
        });
        
        
        
        Thread thread2 = new Thread(() -> {		//Traitement du morphisme, se fait une fois que la fenêtre est créé
            try {
                thread1.join();  //Attend que thread1 se termine
                
                
                long tempsDepart = System.currentTimeMillis();
                int nbEtapes = Integer.parseInt(getChampEtapes().getText());
                int delai = Integer.parseInt(getChampDelai().getText());
                
                dossierFormeSimples();
                
                javafx.scene.image.Image image = getImageGauche().getImage();
                
                //si image non nulle, rÃ©cupÃ¨re le chemin de l'image et le stocke (en enlevant le dÃ©but de la chaine 'file:\\'
                
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
                Platform.runLater(() -> {			//gère le Fx, ça fonctionne pas sans ça
                	
                    ConvertisseurGIF convertisseur = new ConvertisseurGIF();
                    convertisseur.convertirEnGif(delai,  "./Formes");
                    controleur.handleReset(event);
                 
                });
                
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        
        
        
        Thread thread3 = new Thread(() -> {		//Ferme la fenêtre une fois que le traitement a été effectué
            try {
            	
                thread2.join();  	// Attend que thread2 termine    
                
                Platform.runLater(() -> {	//gère le Fx, ça fonctionne pas sans ça
    
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