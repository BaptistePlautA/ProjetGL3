package controleurs;

import utilitaires.Point;

import java.util.Map;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;


/**
 * Classe PointsControleIntermediairesDeplacerHandler
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class PointsControleIntermediairesDeplacerHandler implements EventHandler<MouseEvent> {

    private Point pointEnCoursDeDeplacement;

    private PointsControleIntermediairesPlacerHandler controleur;
    private Point point;
    private Circle cerclePointIntermediaire;
    private Text indiceText;
    private StackPane pane;
    private Map<Character, Point> pointsIntermediaires;
    private Map<Character, Point> pointsControle;

    public PointsControleIntermediairesDeplacerHandler(PointsControleIntermediairesPlacerHandler controleur, Point point, Circle cerclePointIntermediaire, Text indiceText, StackPane pane, Map<Character, Point> pointsIntermediaires, Map<Character, Point> pointsControle) {
        this.controleur = controleur;
        this.point = point;
        this.cerclePointIntermediaire = cerclePointIntermediaire;
        this.indiceText = indiceText;
        this.pane = pane;
        this.pointsIntermediaires = pointsIntermediaires;
        this.pointsControle = pointsControle;
    }

    @Override
    public void handle(MouseEvent event) {

        if (event.getEventType()==MouseEvent.MOUSE_PRESSED) {
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();
            pointEnCoursDeDeplacement = new Point(mouseX, mouseY); 
            controleur.setIsDragging(false);
        }
    
        //pour déplacement d'un point intermédiaire 
        if (event.getEventType()==MouseEvent.MOUSE_DRAGGED) {
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();
    
            double deltaX = mouseX - pointEnCoursDeDeplacement.getX(); 
            double deltaY = mouseY - pointEnCoursDeDeplacement.getY(); 
    
            double coordonnéeX = cerclePointIntermediaire.getTranslateX() + deltaX; 
            double coordonnéeY = cerclePointIntermediaire.getTranslateY() + deltaY; 
    
            //check cercle x pas en dehors du pane 
            if (coordonnéeX < 0 - pane.getWidth()/2){
                cerclePointIntermediaire.setTranslateX(0 - pane.getWidth()/2); 
            }else if (coordonnéeX > 0 + pane.getWidth()/2){
                cerclePointIntermediaire.setTranslateX(0 + pane.getWidth()/2);
                
            }else{
                cerclePointIntermediaire.setTranslateX(coordonnéeX);
                indiceText.setTranslateX(indiceText.getTranslateX() + deltaX);
            }
            //check cercle y pas en dehors du pane
            if (coordonnéeY < 0 - pane.getHeight()/2){
                cerclePointIntermediaire.setTranslateY(0 - pane.getHeight()/2); 
            }else if (coordonnéeY > 0 + pane.getHeight()/2){
                cerclePointIntermediaire.setTranslateY(0 + pane.getHeight()/2);
            }else{
                cerclePointIntermediaire.setTranslateY(coordonnéeY);
                indiceText.setTranslateY(indiceText.getTranslateY() + deltaY);
            }
    
            double x = point.getX() + deltaX; 
            double y = point.getY() + deltaY;
    
            //check point x et y pas en dehors du pane 
            if (x<299 && x>0){
                point.setX(x); 
            }else if (x<0){
                point.setX(0);
            }else{
                point.setX(299);
            }
    
            if (y<299 && y>0){
                point.setY(y);
            }else if (y<0){
                point.setY(0);
            } else{
                point.setY(299);
            } 
            pointEnCoursDeDeplacement = new Point(mouseX, mouseY);
            controleur.setIsDragging(true);
    
            //redessiner la courbe lorsque les points sont déplacés
            controleur.dessinerCourbe(pointsControle, pointsIntermediaires, pane);
        }
    
        if (event.getEventType()==MouseEvent.MOUSE_RELEASED) {
            pointEnCoursDeDeplacement = null;
        }
    }
}
