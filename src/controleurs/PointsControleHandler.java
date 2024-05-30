package controleurs;

import utilitaires.Point;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.input.MouseEvent; 
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
 
/**
 * Classe PointsControleHandler
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class PointsControleHandler implements EventHandler<ActionEvent> {
    private StackPane leftPane; 
    private StackPane rightPane;

    private Point pointEnCoursDeDeplacement;
    private PointsControleIntermediairesPlacerHandler controleur;

    private int indexPoint = 3; 

    private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private int[] alphabetIndex = {0};

    private static Map<Character, Point> pointsControleGauche = new HashMap<>();
    private static Map<Character, Point> pointsControleDroite = new HashMap<>();

    public PointsControleHandler (StackPane leftPane, StackPane rightPane, PointsControleIntermediairesPlacerHandler controleur){
        this.leftPane = leftPane; 
        this.rightPane = rightPane; 
        this.controleur = controleur; 
    }

    public int getIndexPoint() {
		return indexPoint;
	}

	public void setIndexPoint(int indexPoint) {
		this.indexPoint = indexPoint;
	}

    public static Map<Character, Point>getPointsControleGauche() {
    	return pointsControleGauche;
    }
    public static Map<Character, Point>getPointsControleDroite() {
    	return pointsControleDroite;
    }

    @Override
    public void handle(ActionEvent arg0) {}

    /**
     * Procédure pour ajouter un point sur l'image de gauche 
     * @param event
     */
    public void handleLeftPaneClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        setIndexPoint(getIndexPoint() + 1);

        if ((0<=x && x<=300) && (0<=y && y<+300)){
            Point pointGauche = new Point(x, y,getIndexPoint());
            Point pointDroite = new Point(x, y,getIndexPoint());
    
            char label = alphabet.charAt(alphabetIndex[0]);
            alphabetIndex[0]++;
    
            pointsControleGauche.put(label, pointGauche);
            pointsControleDroite.put(label, pointDroite);
            
            afficher(pointGauche, pointDroite, label);
        }
        
    }

    public void handlePanePress(MouseEvent event, Point p) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        pointEnCoursDeDeplacement = new Point(mouseX, mouseY); 
    }

    /**
     * Procédure pour déplacer un point (avec son label et son cercle)
     * @param event
     * @param p
     * @param cerclePointControle
     * @param indiceText
     */
    public void handlePaneDrag(MouseEvent event, Point p, Circle cerclePointControle, Text indiceText) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        double deltaX = mouseX - pointEnCoursDeDeplacement.getX(); 
        double deltaY = mouseY - pointEnCoursDeDeplacement.getY(); 

        double coordonneeX = cerclePointControle.getTranslateX() + deltaX; 
        double coordonneeY = cerclePointControle.getTranslateY() + deltaY; 

        //check cercle x pas en dehors du pane 
        if (coordonneeX < 0 - rightPane.getWidth()/2){
            cerclePointControle.setTranslateX(0 - rightPane.getWidth()/2); 
        }else if (coordonneeX > 0 + rightPane.getWidth()/2){
            cerclePointControle.setTranslateX(0 + rightPane.getWidth()/2);
            
        }else{
            cerclePointControle.setTranslateX(coordonneeX);
            indiceText.setTranslateX(indiceText.getTranslateX() + deltaX);
        }
        //check cercle y pas en dehors du pane
        if (coordonneeY < 0 - rightPane.getHeight()/2){
            cerclePointControle.setTranslateY(0 - rightPane.getHeight()/2); 
        }else if (coordonneeY > 0 + rightPane.getHeight()/2){
            cerclePointControle.setTranslateY(0 + rightPane.getHeight()/2);
        }else{
            cerclePointControle.setTranslateY(coordonneeY);
            indiceText.setTranslateY(indiceText.getTranslateY() + deltaY);
        }

        double x = p.getX() + deltaX; 
        double y = p.getY() + deltaY;

        //check point x pas en dehors du pane 
        if (x<299 && x>0){
            p.setX(x); 
        }else if (x<0){
            p.setX(0);
        }else{
            p.setX(299);
        }

        //check point y pas en dehors du pane 
        if (y<299 && y>0){
            p.setY(y);
        }else if (y<0){
            p.setY(0);
        } else{
            p.setY(299);
        }   

        //si on est dans le cas des formes arrondies, quand le point est déplacé, il faut retracer la droite entre les points de contrôle 
        if (controleur != null){
            controleur.dessinerCourbe(controleur.getPointsControleDroite(), controleur.getPointsIntermediairesDroite(), controleur.getRightPane());
        }
    }

    public void handleMouseRelease(MouseEvent event) {
        pointEnCoursDeDeplacement = null;
    }

    /**
     * Procédure pour afficher les points (cercle et label)
     * @param pointGauche
     * @param pointDroite
     * @param label
     */
    public void afficher(Point pointGauche, Point pointDroite, Character label) {
        double x1 = pointGauche.getX(); 
        double y1 = pointGauche.getY(); 
        Circle pointControleGauche = new Circle(4, Color.web("#2c333e"));
        pointControleGauche.setTranslateX(x1 - (leftPane.getWidth()/2)); 
        pointControleGauche.setTranslateY(y1 - (leftPane.getHeight()/2));

        Text indiceText = new Text(String.valueOf(label));
        indiceText.setTranslateX(x1 - (leftPane.getWidth()/2) + 14);
        indiceText.setTranslateY(y1 - (leftPane.getHeight()/2) - 14);
        leftPane.getChildren().addAll(pointControleGauche, indiceText);

        PointsControleHandler controleur2 = new PointsControleHandler(leftPane, rightPane, controleur);
    
        double x2 = pointDroite.getX(); 
        double y2 = pointDroite.getY();
        Circle pointControleDroite = new Circle(4, Color.web("#2c333e"));
        pointControleDroite.setTranslateX(x2 - (rightPane.getWidth()/2)); 
        pointControleDroite.setTranslateY(y2 - (rightPane.getHeight()/2));

        Text indiceText2 = new Text(String.valueOf(label));
        indiceText2.setTranslateX(x2 - (rightPane.getWidth()/2) + 14);
        indiceText2.setTranslateY(y2 - (rightPane.getHeight()/2) - 14);
        rightPane.getChildren().addAll(pointControleDroite, indiceText2);

        //on applique les contrôleurs sur le point de l'image de droite (ie la possibilité de le déplacer) 
        pointControleDroite.setOnMousePressed(event -> controleur2.handlePanePress(event, pointDroite));
        pointControleDroite.setOnMouseDragged(event -> controleur2.handlePaneDrag(event, pointDroite, pointControleDroite, indiceText2));
        pointControleDroite.setOnMouseReleased(event -> controleur2.handleMouseRelease(event));
    }

    /**
     * Procédure pour reset les points de contrôle 
     * @param event
     */
    public void handleReset(ActionEvent event){
        pointsControleGauche.clear();
        pointsControleDroite.clear();
        for (int i = leftPane.getChildren().size() - 1; i > 0; i--){
            leftPane.getChildren().remove(i); 
        }
        for (int i = rightPane.getChildren().size() - 1; i > 0; i--){
            rightPane.getChildren().remove(i); 
        }
        indexPoint = 3;
        alphabetIndex[0]=0;  
    }
}
