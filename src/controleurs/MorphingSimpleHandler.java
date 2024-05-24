package controleurs;

import javafx.scene.image.ImageView;
import utilitaires.*;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

















import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
















public class MorphingSimpleHandler extends MorphingAbstract implements EventHandler<ActionEvent> {
    
    public MorphingSimpleHandler(TextField champEtapes, TextField champDelai, ImageView imageGauche, PointsControleHandler handler) {
        super(champEtapes, champDelai, imageGauche, handler); 
    }

    @Override
    public void handle(ActionEvent event) {

        int nbEtapes = Integer.parseInt(getChampEtapes().getText());
        int delai = Integer.parseInt(getChampDelai().getText());
        
        dossierFormeSimples();
        
        javafx.scene.image.Image image = getImageGauche().getImage();
        
        //si image non nulle, recupere le chemin de l'image et le stocke (en enlevant le début de la chaine 'file:\\'
        if (image != null) {
            String imagePath = image.getUrl();
            
            File file = new File(imagePath);
            String cheminImage = file.getPath();
        
            setImagePath(cheminImage.substring("file:\\".length()));
        }
        
        
        ImageM imageBase = new ImageM(getImagePath());
        Pixel[][] tabBase = imageBase.getTab();
        List<int[]> couleurs = new ArrayList<>();
        
        
        for (int y = 0; y < imageBase.getHauteur(); y++) {  // Parcourir la hauteur
            for (int x = 0; x < imageBase.getLargeur(); x++) {  // Parcourir la largeur
                Pixel pixel = tabBase[y][x];  // Accéder correctement aux indices
                int r = pixel.getR();
                int v = pixel.getV();
                int b = pixel.getB();

                
                boolean couleurDejaExistante = false;
                for (int[] couleurExistante : couleurs) {
                    if (couleurExistante[0] == r && couleurExistante[1] == v && couleurExistante[2] == b) {
                        couleurDejaExistante = true;
                        break;
                    }
                }
                if (!couleurDejaExistante) {
                    couleurs.add(new int[]{r, v, b});
                }
            }
        }
        

        
        
        if(couleurs.size() < 3) {
        	int[] premierPixel = couleurs.get(0);
        	if (couleurs.size() < 2) {
        		//creer le tableau de pixel de l'image et unifie son fond et le stocke
                ImageM imageFondModifie = modifFondImage(imageBase, null);
        		//colore les points de contrôle de début, trace les droites entre ceux-ci et colore
                colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleDebut(), premierPixel, null);
                
              //boucle tant qu'on a pas atteint le nombre d'etapes demande
                while(nbEtapes>0) {
                	calculEnsemblePointSuivant(nbEtapes);
                	modifFondImage(imageFondModifie, null);
                	colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleDebut(), premierPixel, null);
                	nbEtapes-=1;
                }
        	}else {
        		if((premierPixel[0] == 0) && (premierPixel[1] == 0) && (premierPixel[2] == 0)){
        			//creer le tableau de pixel de l'image et unifie son fond et le stocke
                    ImageM imageFondModifie = modifFondImage(imageBase, null);
                    int[] secondPixel = couleurs.get(1);
                    
    	        	//colore les points de contrôle de début, trace les droites entre ceux-ci et colore
    	            colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleDebut(), premierPixel, secondPixel);
    	            
    	            //boucle tant qu'on a pas atteint le nombre d'etapes demande
    	            while(nbEtapes>0) {
    	            	calculEnsemblePointSuivant(nbEtapes);
    	            	modifFondImage(imageFondModifie, null);
    	            	colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleDebut(), premierPixel, secondPixel);
    	            	nbEtapes-=1;
    	            }
        		}else {
        			//creer le tableau de pixel de l'image et unifie son fond et le stocke
                    ImageM imageFondModifie = modifFondImage(imageBase, premierPixel);
                    int[] secondPixel = couleurs.get(1);
    	        	//colore les points de contrôle de début, trace les droites entre ceux-ci et colore
    	            colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleDebut(), premierPixel, secondPixel);
    	            
    	          //boucle tant qu'on a pas atteint le nombre d'etapes demande
    	            while(nbEtapes>0) {
    	            	calculEnsemblePointSuivant(nbEtapes);
    	            	modifFondImage(imageFondModifie, premierPixel);
    	            	colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleDebut(), premierPixel, secondPixel);
    	            	nbEtapes-=1;
    	            }
        		}
	        	
        	}
        }else {
        	//creer le tableau de pixel de l'image et unifie son fond et le stocke
            ImageM imageFondModifie = modifFondImage(imageBase, null);
        	//colore les points de contrôle de début, trace les droites entre ceux-ci et colore
            colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleDebut(), null, null);
            
          //boucle tant qu'on a pas atteint le nombre d'etapes demande
            while(nbEtapes>0) {
            	calculEnsemblePointSuivant(nbEtapes);
            	modifFondImage(imageFondModifie, null);
            	colorPointsDeControle(imageFondModifie, PointsControleHandler.getPointsControleDebut(), null, null);
            	nbEtapes-=1;
            }
        }
        
        
        
        
        
        
        		
        		
        		
        		
        		
        		
        		
        		
        		
        		
        		
        
        
        
        
        //convertie les images en gif
        ConvertisseurGIF convertisseur = new ConvertisseurGIF();
        convertisseur.convertirEnGif(delai);
        getHandler().handleReset(event);
    }
}