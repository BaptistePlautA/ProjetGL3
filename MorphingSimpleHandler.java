
package controleurs;

import javafx.scene.image.ImageView;
import utilitaires.*;
import java.io.File;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;



public class MorphingSimpleHandler extends MorphingAbstract implements EventHandler<ActionEvent> {
    
    public MorphingSimpleHandler(TextField champEtapes, TextField champDelai, ImageView imageGauche, PointsControleHandler handler) {
        super(champEtapes, champDelai, imageGauche, handler); 
    }

    @Override
    public void handle(ActionEvent event) {

       	
        Thread thread1 = new Thread(() -> {		//Création de la fenêtre
               	
            Platform.runLater(() -> {	//gère le Fx, ça fonctionne pas sans ça
            
            	System.out.println(1);
            	AttenteFinMorphisme.creerFenetre();
            	
            });
     	
        });
        
    
        
        Thread thread2 = new Thread(() -> {		//Traitement du morphisme, se fait une fois que la fenêtrer est créé
            try {
                thread1.join();  //Attend que thread1 se termine
                
                int nbEtapes = Integer.parseInt(getChampEtapes().getText());
                int delai = Integer.parseInt(getChampDelai().getText());
                
                dossierFormeSimples();
                
                javafx.scene.image.Image image = getImageGauche().getImage();

                //si image non nulle, récupère le chemin de l'image et le stocke (en enlevant le dÃ©but de la chaine 'file:\\'
                if (image != null) {
                    String imagePath = image.getUrl();
                    
                    File file = new File(imagePath);
                    String cheminImage = file.getPath();
                
                    cheminImage = cheminImage.replace("%20", " ").replace("\\", "\\\\");
                    setImagePath(cheminImage.substring("file:\\".length()));
                    System.out.println(getImagePath());

                }
                
                //creer le tableau de pixel de l'image et unifie son fond et le stocke
                ImageM imageFondModifie = modifFondImage(new ImageM(getImagePath()));

                //colore les points de contrÃ´le de dÃ©but, trace les droites entre ceux-ci et colore
                colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleDebut());
                
                //boucle tant qu'on a pas atteint le nombre d'etapes demande
                while(nbEtapes>0) {
                	calculEnsemblePointSuivant(nbEtapes);
                	modifFondImage(imageFondModifie);
                	colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleDebut());
                	nbEtapes-=1;
                }
                
                //convertit les images en gif
                Platform.runLater(() -> {			//gère le Fx, ça fonctionne pas sans ça
                	
                	System.out.println(2);
                    ConvertisseurGIF convertisseur = new ConvertisseurGIF();
                    convertisseur.convertirEnGif(delai);
                    getHandler().handleReset(event);
                 
                });
                
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        
        
        Thread thread3 = new Thread(() -> {		//Ferme la fenêtre une fois que le traitement a été effectué
            try {
            	
                thread2.join();  	// Attend que thread2 termine    
                
                Platform.runLater(() -> {	//gère le Fx, ça fonctionne pas sans ça
                	System.out.println(3);
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