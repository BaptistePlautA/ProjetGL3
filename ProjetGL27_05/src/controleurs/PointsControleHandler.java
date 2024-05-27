package controleurs;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.input.MouseEvent; 
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import utilitaires.Point; 

public class PointsControleHandler implements EventHandler<ActionEvent> {
    private StackPane leftPane; 
    private StackPane rightPane;
    private int indexPoint = 3; //Initialisé à 3 par défaut car les 4 premiers points (0 à 3) de contrôle sont placés auto


    private Point pointEnCoursDeDeplacement;

    private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private int[] alphabetIndex = {0};

    private static Map<Character, Point> pointsControleDebut = new HashMap<>();
    private static Map<Character, Point> pointsControleFin = new HashMap<>();

    public PointsControleHandler (StackPane leftPane, StackPane rightPane){
        this.leftPane = leftPane; 
        this.rightPane = rightPane;
    }

    @Override
    public void handle(ActionEvent arg0) {}

    public void handleLeftPaneClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        setIndexPoint(getIndexPoint() + 1);
        
        if ((0<=x && x<=300) && (0<=y && y<+300)){
            Point pointDebut = new Point(x, y,getIndexPoint());
            Point pointFin = new Point(x, y,getIndexPoint());
            System.out.println(pointDebut.toString());
            System.out.println(pointFin.toString());

    
            char label = alphabet.charAt(alphabetIndex[0]);
            alphabetIndex[0]++;
    
            pointsControleDebut.put(label, pointDebut);
            pointsControleFin.put(label, pointFin);
            
            afficher(pointDebut, pointFin, label);
        }
    }

    public void handlePanePress(MouseEvent event, Point p) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        pointEnCoursDeDeplacement = new Point(mouseX, mouseY); 
    }

    public void handlePaneDrag(MouseEvent event, Point p, Circle cerclePointControle, Text indiceText) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        double deltaX = mouseX - pointEnCoursDeDeplacement.getX(); 
        double deltaY = mouseY - pointEnCoursDeDeplacement.getY(); 

        double coordonnéeX = cerclePointControle.getTranslateX() + deltaX; 
        double coordonnéeY = cerclePointControle.getTranslateY() + deltaY; 

        //check point x pas en dehors du pane 
        if (coordonnéeX < 0 - rightPane.getWidth()/2){
            cerclePointControle.setTranslateX(0 - rightPane.getWidth()/2); 
        }else if (coordonnéeX > 0 + rightPane.getWidth()/2){
            cerclePointControle.setTranslateX(0 + rightPane.getWidth()/2);
            
        }else{
            cerclePointControle.setTranslateX(coordonnéeX);
            indiceText.setTranslateX(indiceText.getTranslateX() + deltaX);
        }
        //check point y pas en dehors du pane
        if (coordonnéeY < 0 - rightPane.getHeight()/2){
            cerclePointControle.setTranslateY(0 - rightPane.getHeight()/2); 
        }else if (coordonnéeY > 0 + rightPane.getHeight()/2){
            cerclePointControle.setTranslateY(0 + rightPane.getHeight()/2);
        }else{
            cerclePointControle.setTranslateY(coordonnéeY);
            indiceText.setTranslateY(indiceText.getTranslateY() + deltaY);
        }

        double x = p.getX() + deltaX; 
        double y = p.getY() + deltaY;

        if (x<299 && x>0){
            p.setX(x); 
        }else if (x<0){
            p.setX(0);
        }else{
            p.setX(299);
        }

        if (y<299 && y>0){
            p.setY(y);
        }else if (y<0){
            p.setY(0);
        } else{
            p.setY(299);
        }
    }

    public void handleMouseRelease(MouseEvent event) {
        pointEnCoursDeDeplacement = null;
    }

    public void afficher(Point pointDebut, Point pointFin, Character label) {

        double x1 = pointDebut.getX(); 
        double y1 = pointDebut.getY(); 
        Circle pointControleDebut = new Circle(4, Color.RED);
        pointControleDebut.setTranslateX(x1 - (leftPane.getWidth()/2)); 
        pointControleDebut.setTranslateY(y1 - (leftPane.getHeight()/2));

        Text indiceText = new Text(String.valueOf(label));
        indiceText.setTranslateX(x1 - (leftPane.getWidth()/2) + 14);
        indiceText.setTranslateY(y1 - (leftPane.getHeight()/2) - 14);
        leftPane.getChildren().addAll(pointControleDebut, indiceText);

        PointsControleHandler handler = new PointsControleHandler(leftPane, rightPane);
    
        double x2 = pointFin.getX(); 
        double y2 = pointFin.getY();
        Circle pointControleFin = new Circle(4, Color.RED);
        pointControleFin.setTranslateX(x2 - (rightPane.getWidth()/2)); 
        pointControleFin.setTranslateY(y2 - (rightPane.getHeight()/2));

        Text indiceText2 = new Text(String.valueOf(label));
        indiceText2.setTranslateX(x2 - (rightPane.getWidth()/2) + 14);
        indiceText2.setTranslateY(y2 - (rightPane.getHeight()/2) - 14);
        rightPane.getChildren().addAll(pointControleFin, indiceText2);

        pointControleFin.setOnMousePressed(event -> handler.handlePanePress(event, pointFin));
        pointControleFin.setOnMouseDragged(event -> handler.handlePaneDrag(event, pointFin, pointControleFin, indiceText2));
        pointControleFin.setOnMouseReleased(event -> handler.handleMouseRelease(event));
    }

    public void handleReset(ActionEvent event){
        pointsControleDebut.clear();
        pointsControleFin.clear();
        for (int i = leftPane.getChildren().size() - 1; i > 0; i--){
            leftPane.getChildren().remove(i); 
        }
        for (int i = rightPane.getChildren().size() - 1; i > 0; i--){
            rightPane.getChildren().remove(i); 
        }
        indexPoint = 3;
        alphabetIndex[0]=0;  
    }
    public static Map<Character, Point> getPointsControleDebut() {
    	return pointsControleDebut;
    }
    public static Map<Character, Point> getPointsControleFin() {
    	return pointsControleFin;
    }

	public int getIndexPoint() {
		return indexPoint;
	}

	public void setIndexPoint(int indexPoint) {
		this.indexPoint = indexPoint;
	}
}
