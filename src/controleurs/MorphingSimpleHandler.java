
package controleurs;

import java.util.Map;
import utilitaires.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

public class MorphingSimpleHandler implements EventHandler<ActionEvent> {
    private TextField champEtapes;
    private TextField champDelai;
    
    public MorphingSimpleHandler(TextField champEtapes, TextField champDelai) {
        this.champEtapes = champEtapes;
        this.champDelai = champDelai;
        }

    @Override
    public void handle(ActionEvent event) {

        int nbEtapes = Integer.parseInt(champEtapes.getText());
        int delai = Integer.parseInt(champDelai.getText());
        
        System.out.println("Nombre d'etapes : " + nbEtapes + ", delai (ms) : " + delai);
        
        while(nbEtapes>=0) {
        	calculEnsemblePointSuivant(nbEtapes);
        	
        	nbEtapes-=1;
        }
        
    }
    private void calculEnsemblePointSuivant(int nbEtapes) {
    	
    	for (Map.Entry<Character, Point> entry : PointsControleHandler.pointsControleDebut.entrySet()) {
        	Character key = entry.getKey();
            Point pointDebut = entry.getValue();
            Point pointFin = PointsControleHandler.pointsControleFin.get(key);
            
            System.out.println(key+" : ("+pointDebut.getX()+","+pointDebut.getY()+")");
            calculPointSuivant(pointDebut, pointFin, nbEtapes);
        }
    }
    private void calculPointSuivant(Point pointDebut, Point pointFin, int nbEtapes) {
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
}