package controleurs;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.scene.input.MouseEvent; 
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import utilitaires.Point; 

/**
 * Classe PointsControleIntermediairesPlacerHandler
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class PointsControleIntermediairesPlacerHandler implements EventHandler<MouseEvent> {
    private StackPane leftPane; 
    private StackPane rightPane;
    private boolean isDragging = false;

    private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private int[] alphabetIndex = {0};

    private static Map<Character, Point> pointsControleGauche = new HashMap<>();
    private static Map<Character, Point> pointsControleDroite = new HashMap<>();

    private Map<Character, Point> pointsIntermediairesGauche = new HashMap<>();
    private Map<Character, Point> pointsIntermediairesDroite = new HashMap<>();

    private static Map<Character, Point> pointsMorphingDebut = new LinkedHashMap<>();
    private static Map<Character, Point> pointsMorphingFin = new LinkedHashMap<>();

    public PointsControleIntermediairesPlacerHandler (StackPane leftPane, StackPane rightPane){
        this.leftPane = leftPane; 
        this.rightPane = rightPane;
        this.isDragging = false;
    }

    public void setIsDragging(boolean b) {
        this.isDragging = b;
    }

    public static Map<Character, Point> getPointsMorphingDebut (){
        return pointsMorphingDebut; 
    }

    public static Map<Character, Point> getPointsMorphingFin (){
        return pointsMorphingFin; 
    }

    public Map<Character, Point> getPointsIntermediairesDroite(){
        return pointsIntermediairesDroite; 
    }

    public Map<Character, Point> getPointsControleDroite(){
        return pointsControleDroite; 
    }

    public StackPane getRightPane(){
        return rightPane; 
    }


    @Override
    public void handle(MouseEvent event) {
        //empêche le fait que ça place un point apres en avoir déplacé un autre 
        if (isDragging) {
            this.isDragging = false;
            return;
        }

        double x = event.getX();
        double y = event.getY();

        if ((0<=x && x<=300) && (0<=y && y<+300)){
            Point pointGauche = new Point(x, y);
            Point pointDroite = new Point(x, y);
    
            char label = alphabet.charAt(alphabetIndex[0]);
            alphabetIndex[0]++;
    
            pointsControleGauche.put(label, pointGauche);
            pointsControleDroite.put(label, pointDroite);
            
            afficher(pointGauche, pointDroite, label);
            if (pointsControleGauche.size() % 2 == 0) {
                ajouterPointsIntermediaires(label);
            }
        }
    }

    /**
     * Procédure qui affiche les points (avec cercle et label)
     * @param pointGauche
     * @param pointDroite
     * @param label
     */
    public void afficher(Point pointGauche, Point pointDroite, Character label) {

        double x1 = pointGauche.getX(); 
        double y1 = pointGauche.getY(); 
        Circle cerclepointGauche = new Circle(4, Color.web("#2c333e"));
        cerclepointGauche.setTranslateX(x1 - (leftPane.getWidth()/2)); 
        cerclepointGauche.setTranslateY(y1 - (leftPane.getHeight()/2));

        Text indiceText = new Text(String.valueOf(label));
        indiceText.setTranslateX(x1 - (leftPane.getWidth()/2) + 14);
        indiceText.setTranslateY(y1 - (leftPane.getHeight()/2) - 14);
        leftPane.getChildren().addAll(cerclepointGauche, indiceText);

        PointsControleHandler controleur = new PointsControleHandler(leftPane, rightPane, this);

        double x2 = pointDroite.getX(); 
        double y2 = pointDroite.getY();
        Circle cerclepointDroite = new Circle(4, Color.web("#2c333e"));
        cerclepointDroite.setTranslateX(x2 - (rightPane.getWidth()/2)); 
        cerclepointDroite.setTranslateY(y2 - (rightPane.getHeight()/2));

        Text indiceText2 = new Text(String.valueOf(label));
        indiceText2.setTranslateX(x2 - (rightPane.getWidth()/2) + 14);
        indiceText2.setTranslateY(y2 - (rightPane.getHeight()/2) - 14);
        rightPane.getChildren().addAll(cerclepointDroite, indiceText2);

        cerclepointDroite.setOnMousePressed(event -> controleur.handlePanePress(event, pointDroite));
        cerclepointDroite.setOnMouseDragged(event -> controleur.handlePaneDrag(event, pointDroite, cerclepointDroite, indiceText2));
        cerclepointDroite.setOnMouseReleased(event -> controleur.handleMouseRelease(event));
        
    }

    /**
     * Procédure qui ajoute 2 points intermédiaires à équidistance lorsque l'user a ajouté 2 points de contrôle classique 
     * @param label
     */
    public void ajouterPointsIntermediaires(char label) {
        Point p0 = pointsControleGauche.get((char) (label - 1));
        Point p1 = pointsControleGauche.get(label);
        Point p2 = pointsControleDroite.get((char) (label - 1));
        Point p3 = pointsControleDroite.get(label);

        //calcul des points intermédiaires à équidistance
        Point i0 = new Point((2 * p0.getX() + p1.getX()) / 3, (2 * p0.getY() + p1.getY()) / 3);
        Point i1 = new Point((p0.getX() + 2 * p1.getX()) / 3, (p0.getY() + 2 * p1.getY()) / 3);

        Point i2 = new Point((2 * p2.getX() + p3.getX()) / 3, (2 * p2.getY() + p3.getY()) / 3);
        Point i3 = new Point((p2.getX() + 2 * p3.getX()) / 3, (p2.getY() + 2 * p3.getY()) / 3);

        char label1 = alphabet.charAt(alphabetIndex[0]);
        alphabetIndex[0]++;

        //ajout 1er point intermédiaire dans les maps et affichage 
        pointsIntermediairesGauche.put(label1, i0);
        pointsIntermediairesDroite.put(label1,i2); 
        afficherPointIntermediaire(i0, label1, pointsIntermediairesGauche, leftPane, pointsControleGauche);
        afficherPointIntermediaire(i2, label1, pointsIntermediairesDroite, rightPane, pointsControleDroite);

        char label2 = alphabet.charAt(alphabetIndex[0]);
        alphabetIndex[0]++;

        //ajout 2e point intermédiaire dans les maps et affichage 
        pointsIntermediairesGauche.put(label2, i1);
        pointsIntermediairesDroite.put(label2, i3); 
        afficherPointIntermediaire(i1, label2, pointsIntermediairesGauche, leftPane, pointsControleGauche);
        afficherPointIntermediaire(i3, label2, pointsIntermediairesDroite, rightPane, pointsControleDroite);

        //add dans map pour morphing 
        pointsMorphingDebut.put((char)(label-1), p0); 
        pointsMorphingDebut.put(label1, i0); 
        pointsMorphingDebut.put(label2, i1); 
        pointsMorphingDebut.put((char)(label), p1); 

        //add dans map pour morphing 
        //(pour le traitement du morphing il faut que tous les points soient dans une même map)
        pointsMorphingFin.put((char) (label - 1), p2); 
        pointsMorphingFin.put(label1, i2); 
        pointsMorphingFin.put(label2, i3); 
        pointsMorphingFin.put((char)(label), p3);

        dessinerCourbe(pointsControleGauche, pointsIntermediairesGauche, leftPane);
        dessinerCourbe(pointsControleDroite, pointsIntermediairesDroite, rightPane);
    }

    /**
     * Procédure pour afficher les points intermédiaires (cercle et label)
     * @param point
     * @param label
     * @param pointsIntermediaires
     * @param pane
     * @param pointsControle
     */
    public void afficherPointIntermediaire(Point point, Character label, Map<Character, Point> pointsIntermediaires, StackPane pane, Map<Character, Point> pointsControle) {
        double x = point.getX();
        double y = point.getY();

        Circle cerclePointIntermediaire = new Circle(4, Color.web("#C3ACD0"));
        cerclePointIntermediaire.setTranslateX(x - (leftPane.getWidth() / 2));
        cerclePointIntermediaire.setTranslateY(y - (leftPane.getHeight() / 2));

        Text indiceText = new Text(String.valueOf(label));
        indiceText.setTranslateX(x - (leftPane.getWidth() / 2) + 14);
        indiceText.setTranslateY(y - (leftPane.getHeight() / 2) - 14);
        pane.getChildren().addAll(cerclePointIntermediaire, indiceText);

        PointsControleIntermediairesDeplacerHandler controleur = new PointsControleIntermediairesDeplacerHandler(this, point,cerclePointIntermediaire, indiceText, pane, pointsIntermediaires, pointsControle);

        cerclePointIntermediaire.setOnMousePressed(controleur);
        cerclePointIntermediaire.setOnMouseDragged(controleur);
        cerclePointIntermediaire.setOnMouseReleased(controleur);
    }

    /**
     * Procédure qui va regrouper les points et appeler la méthode pour tracer les courbes (sur la pane concernée ie l'image de début ou de fin)
     * @param pointsControle
     * @param pointsIntermediaires
     * @param pane
     */
    public void dessinerCourbe(Map<Character, Point> pointsControle, Map<Character, Point> pointsIntermediaires, StackPane pane) {
        //delete les courbes existantes
        pane.getChildren().removeIf(node -> node instanceof Line);

        Map<Character, Point> allControlPoints = new LinkedHashMap<>();

        //permet de regrouper les points de contrôles dans une seule map (pour le traitement dans la méthode de calcul pour les courbes de bézier)
        for (int i = 0; i < pointsControle.size() / 2; i++) {
            char labelDebut = alphabet.charAt(4 * i);
            char labelFin = alphabet.charAt(4 * i + 1);
            char labelI0 = alphabet.charAt(4 * i + 2);
            char labelI1 = alphabet.charAt(4 * i + 3);
            
            if (pointsControle.containsKey(labelDebut) && pointsControle.containsKey(labelFin) &&
                pointsIntermediaires.containsKey(labelI0) && pointsIntermediaires.containsKey(labelI1)) {
                Point p0 = pointsControle.get(labelDebut);
                Point i0 = pointsIntermediaires.get(labelI0);
                Point i1 = pointsIntermediaires.get(labelI1);
                Point p1 = pointsControle.get(labelFin);

                //ajouter les points dans le bon ordre 
                allControlPoints.put(labelDebut, p0);
                allControlPoints.put(labelI0, i0);
                allControlPoints.put(labelI1, i1);
                allControlPoints.put(labelFin, p1);
            }
        }
    
        //tracer les courbes de Bézier
        traceCourbeBezier(pane, allControlPoints);
    }
    

    /**
     * Procédure qui prend les points de contrôles 4 par 4 et calcule les points intermédiaires entre ces derniers puis les relie 
     * @param pane
     * @param controlPoints
     */
    public void traceCourbeBezier(StackPane pane, Map<Character, Point> controlPoints) {
        pane.getChildren().removeIf(node -> node instanceof Line); //delete les lignes existantes 

        Character[] keys = controlPoints.keySet().toArray(new Character[0]);
    
        for (int i = 0; i <= keys.length - 4; i += 4) {
            Point p0 = controlPoints.get(keys[i]);
            Point p1 = controlPoints.get(keys[i + 1]);
            Point p2 = controlPoints.get(keys[i + 2]);
            Point p3 = controlPoints.get(keys[i + 3]);
            //DEBUG
            //System.out.println("p0 : "+p0+" p1 : "+p1+" p2 : "+p2+" p3 : "+p3);

            if (p0 == null || p1 == null || p2 == null || p3 == null) {
                System.err.println("Erreur : Un ou plusieurs points de contrôle manquent.");
                return;
            }
    
            //calculer et tracer les points intermédiaires
            double step = 0.01; //intervalle pour l'échantillonnage
            double prevX = p0.getX();
            double prevY = p0.getY();
    
            for (double t = step; t <= 1.0; t += step) {
                double x = Math.pow(1 - t, 3) * p0.getX() + 3 * Math.pow(1 - t, 2) * t * p1.getX() + 3 * (1 - t) * Math.pow(t, 2) * p2.getX() + Math.pow(t, 3) * p3.getX();
                double y = Math.pow(1 - t, 3) * p0.getY() + 3 * Math.pow(1 - t, 2) * t * p1.getY() + 3 * (1 - t) * Math.pow(t, 2) * p2.getY() + Math.pow(t, 3) * p3.getY();

                //relie les points entre eux 
                Line line = new Line(prevX, prevY, x, y);
                line.setTranslateX((prevX+x-pane.getWidth())/2);
                line.setTranslateY((prevY+y-pane.getHeight())/2);
                line.setStroke(Color.BLACK);
                pane.getChildren().add(1, line);
    
                prevX = x;
                prevY = y;
            }
        }
    }

    /**
     * Procédure qui reset les points de contrôle 
     * @param event
     */
    public void handleReset(ActionEvent event){
        pointsControleGauche.clear();
        pointsControleDroite.clear();
        pointsIntermediairesGauche.clear(); 
        pointsIntermediairesDroite.clear();
        pointsMorphingDebut.clear(); 
        pointsMorphingFin.clear(); 
        for (int i = leftPane.getChildren().size() - 1; i > 0; i--){
            leftPane.getChildren().remove(i); 
        }
        for (int i = rightPane.getChildren().size() - 1; i > 0; i--){
            rightPane.getChildren().remove(i); 
        }
        alphabetIndex[0]=0;  
    }
}
