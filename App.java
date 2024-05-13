import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App extends Application {

    private static final int LARGEUR_CANVAS = 300;
    private static final int HAUTEUR_CANVAS = 200;
    private Canvas canvasGauche;
    private Canvas canvasDroite;
    private Canvas canvasResultat;
    private GraphicsContext gcGauche;
    private GraphicsContext gcDroite;
    private GraphicsContext gcResultat;
    private Map<Character, Point> pointsControleDebut = new HashMap<>();
    private Map<Character, Point> pointsControleFin = new HashMap<>();
    private Forme formeDebut;
    private Forme formeFin;
    private MorphingFormesSimples moteurMorphing;
    private List<Forme> formes;
    private int etapeCourante;
    private Point pointEnCoursDeDeplacement;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Application de Morphing");

        //création zones de dessin 
        canvasGauche = new Canvas(LARGEUR_CANVAS, HAUTEUR_CANVAS);
        gcGauche = canvasGauche.getGraphicsContext2D();

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        //indice pour suivre la lettre à ajouter en label du point 
        final int[] alphabetIndex = {0};

        //pour add les points de contrôle quand clique sur canva de gauche 
        canvasGauche.setOnMouseClicked(event -> {
            if (formeFin == null) {
                //affiche alerte si forme de fin n'a pas été sélectionnée
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner la forme de fin avant d'ajouter des points de contrôle sur la forme de début.");
                alert.showAndWait();
                return;
            }
            double x = event.getX();
            double y = event.getY();

            //crée un nouveau point avec les coordonnées du clic
            Point pointDébut = new Point(x, y);
            Point pointFin = new Point(x, y);

            char label = alphabet.charAt(alphabetIndex[0]);
            alphabetIndex[0]++; 

            //ajoute le point à la liste des points de contrôle de la forme de début 
            pointsControleDebut.put(label,pointDébut);
            //ajoute le point à la liste des points de contrôle de la forme de fin 
            pointsControleFin.put(label,pointFin);

            //redessine la forme de gauche avec les nouveaux points de contrôle
            dessiner(); 
        });


        canvasDroite = new Canvas(LARGEUR_CANVAS, HAUTEUR_CANVAS);
        gcDroite = canvasDroite.getGraphicsContext2D();

        canvasDroite.setOnMousePressed(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();
        
            //vérifie si la souris est sur un point de contrôle existant de la forme de fin
            for (Map.Entry<Character, Point> entry : pointsControleFin.entrySet()) {
                Point point = entry.getValue();
                if (isMouseOnPoint(mouseX, mouseY, point)) {
                    pointEnCoursDeDeplacement = point;
                    break;
                }
            }
        });
        
        canvasDroite.setOnMouseDragged(event -> {
            if (pointEnCoursDeDeplacement != null) {
                double mouseX = event.getX();
                double mouseY = event.getY();
                pointEnCoursDeDeplacement.setX(mouseX);
                pointEnCoursDeDeplacement.setY(mouseY);
                dessiner();
            }
        });
        
        canvasDroite.setOnMouseReleased(event -> {
            pointEnCoursDeDeplacement = null;
        });
        

        canvasResultat = new Canvas(LARGEUR_CANVAS * 2, HAUTEUR_CANVAS);
        gcResultat = canvasResultat.getGraphicsContext2D();

        //choix formes début et fin 
        ComboBox<String> choixFormeDebutComboBox = new ComboBox<>();
        choixFormeDebutComboBox.getItems().addAll("Rectangle", "Triangle", "Autre");
        choixFormeDebutComboBox.setValue("Rectangle");
        choixFormeDebutComboBox.setOnAction(event -> {
            String formeDebut = choixFormeDebutComboBox.getValue();
            this.formeDebut = creerForme(formeDebut);
            dessiner();
        });

        ComboBox<String> choixFormeFinComboBox = new ComboBox<>();
        choixFormeFinComboBox.getItems().addAll("Rectangle", "Triangle", "Autre");
        choixFormeFinComboBox.setValue("Rectangle");
        choixFormeFinComboBox.setOnAction(event -> {
            String formeFin = choixFormeFinComboBox.getValue();
            this.formeFin = creerForme(formeFin);
            dessiner();
        });

        Button boutonReset = new Button("Réinitialiser");
        boutonReset.setOnAction(event -> {
            //efface les points de contrôle de début et de fin
            pointsControleDebut.clear();
            pointsControleFin.clear();
            alphabetIndex[0]=0;
            //dessine à nouveau les formes pour effacer les points de contrôle du canvas
            dessiner();
        });

        HBox choixFormesHBox = new HBox(10);
        choixFormesHBox.getChildren().addAll(choixFormeDebutComboBox, choixFormeFinComboBox, boutonReset);
        choixFormesHBox.setAlignment(Pos.CENTER);
        HBox canvasHBox = new HBox(10);
        canvasHBox.getChildren().addAll(canvasGauche, canvasDroite);
        canvasHBox.setAlignment(Pos.CENTER);

        //champ nb d'étapes
        TextField champEtapes = new TextField("10");
        champEtapes.setPrefWidth(50);
        Label etapesLabel = new Label("Nombre d'étapes:");

        //champ délai 
        TextField champDelai = new TextField("100");
        champDelai.setPrefWidth(50);
        Label delaiLabel = new Label("Délai (ms):");

        HBox optionsHBox = new HBox(10);
        optionsHBox.getChildren().addAll(etapesLabel, champEtapes, delaiLabel, champDelai);
        optionsHBox.setAlignment(Pos.CENTER);

        //bouton pour lancer le morphing 
        Button boutonMorphing = new Button("Morphing");
        boutonMorphing.setOnAction(e -> {
            int nbEtapes = Integer.parseInt(champEtapes.getText());  //recup le nombre d'étapes
            int delai = Integer.parseInt(champDelai.getText());  //recup le délai

            //génère formes intermédiaires avec le nouveau nombre d'étapes
            moteurMorphing = new MorphingFormesSimples(formeDebut, formeFin, pointsControleDebut, pointsControleFin, nbEtapes);
            formes = moteurMorphing.genererFormesIntermediaires();

            demarrerMorphing(gcResultat, canvasResultat, delai); 
        });

        VBox optionsVBox = new VBox(10);
        optionsVBox.getChildren().addAll(optionsHBox, boutonMorphing);
        optionsVBox.setAlignment(Pos.CENTER);

        VBox rootVBox = new VBox(10);
        rootVBox.getChildren().addAll(choixFormesHBox, canvasHBox, optionsVBox, canvasResultat);
        rootVBox.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(rootVBox));
        primaryStage.show();
    }

    //crée forme selon choix user 
    private Forme creerForme(String typeForme) {
        switch (typeForme) {
            case "Rectangle":
                return creerRectangle();
            case "Triangle":
                return creerTriangle();
            case "Autre":
                //pour add autres formes 
                break;
        }
        return null;
    }

    private Forme creerRectangle() {
        Point[] points = {
                new Point(50, 50),
                new Point(250, 50),
                new Point(250, 200),
                new Point(50, 200)
        };
        return new Forme(points);
    }

    private Forme creerTriangle() {
        Point[] points = {
                new Point(100, 50),
                new Point(250, 200),
                new Point(50, 200), 
                new Point(50, 200)
        };
        return new Forme(points);
    }

    //dessine les formes et les points de contrôle 
    private void dessiner() {
        gcGauche.clearRect(0, 0, LARGEUR_CANVAS, HAUTEUR_CANVAS);
        gcDroite.clearRect(0, 0, LARGEUR_CANVAS, HAUTEUR_CANVAS);
    
        if (formeDebut != null) {
            formeDebut.dessiner(gcGauche);
        }
    
        if (formeFin != null) {
            formeFin.dessiner(gcDroite);
        }
    
        //dessine les points de contrôle pour la forme de début
        for (Map.Entry<Character, Point> entry : pointsControleDebut.entrySet()) {
            char label = entry.getKey();
            Point point = entry.getValue();
    
            double pointSize = 6; 
    
            gcGauche.setFill(Color.BLACK);
            gcGauche.fillOval(point.getX() - (pointSize / 2), point.getY() - (pointSize / 2), pointSize, pointSize);
    
            double labelOffsetX = 0; //décalage horizontal du label par rapport au point
            double labelOffsetY = -7; //décalage vertical du label par rapport au point
            gcGauche.setFill(Color.BLACK);
            gcGauche.fillText(String.valueOf(label), point.getX() + labelOffsetX, point.getY() + labelOffsetY);
        }
    
        //dessine les points de contrôle pour la forme de fin
        for (Map.Entry<Character, Point> entry : pointsControleFin.entrySet()) {
            char label = entry.getKey();
            Point point = entry.getValue();
    
            double pointSize = 6; 
    
            gcDroite.setFill(Color.BLACK);
            gcDroite.fillOval(point.getX() - (pointSize / 2), point.getY() - (pointSize / 2), pointSize, pointSize);
    
            double labelOffsetX = 0; //décalage horizontal du label par rapport au point
            double labelOffsetY = -7; //décalage vertical du label par rapport au point
            gcDroite.setFill(Color.BLACK);
            gcDroite.fillText(String.valueOf(label), point.getX() + labelOffsetX, point.getY() + labelOffsetY);
        }
    }

    private void demarrerMorphing(GraphicsContext gc, Canvas canvas, int delai) {
        etapeCourante = 0;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(delai), e -> {
            etapeCourante++;
            if (etapeCourante >= formes.size()) {
                etapeCourante = 0; 
            }
            dessinerForme(gc, formes.get(etapeCourante));
        }));
        timeline.setCycleCount(formes.size() - 1);  //animation sur nbEtapes - 1 pour s'arrêter à la dernière
        timeline.play();
    }

    //dessine formes intermédiaires
    private void dessinerForme(GraphicsContext gc, Forme forme) {
        gc.clearRect(0, 0, LARGEUR_CANVAS, HAUTEUR_CANVAS);  //effacer le canevas
        gc.setStroke(Color.BLACK);
        Point[] points = forme.getPoints();
        for (int i = 0; i < points.length; i++) {
            Point p1 = points[i];
            Point p2 = points[(i + 1) % points.length];
            gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
    }

    //vérifie si la souris est sur un point de contrôle existant
    private boolean isMouseOnPoint(double mouseX, double mouseY, Point point) {
        double distance = Math.sqrt(Math.pow(mouseX - point.getX(), 2) + Math.pow(mouseY - point.getY(), 2));
        return distance <= 5; //rayon de détection du point de contrôle
    }

    public static void main(String[] args) {
        launch(args);
    }
}
