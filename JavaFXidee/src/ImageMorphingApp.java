import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import java.io.IOException;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;

public class ImageMorphingApp extends Application {

    private Scene sceneArrondie;
    private Scene mainMenuScene;
    private Scene sceneSimple;
    private static final int LARGEUR_CANVAS = 300;
    private static final int HAUTEUR_CANVAS = 300;
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

        // Setup de l'icône de l'app
        Image icon = new Image(getClass().getResourceAsStream("icon.png"));
        primaryStage.getIcons().add(icon);

        ////////////////////////////////////Scene 0 : Menu
        Button MorphSimpleButton = new Button("Morphing de formes simples");
        MorphSimpleButton.setOnAction(e -> primaryStage.setScene(sceneSimple));
        MorphSimpleButton.getStyleClass().add("Menu_Button");


        Button MorphArrondiButton = new Button("Morphing de formes arrondies");
        MorphArrondiButton.setOnAction(e -> primaryStage.setScene(sceneArrondie));
        MorphArrondiButton.getStyleClass().add("Menu_Button");


        Button MorphImages = new Button("Morphing d'images");
        MorphImages.getStyleClass().add("Menu_Button");


        VBox mainMenu = new VBox(15);
        mainMenu.getChildren().addAll(MorphSimpleButton, MorphArrondiButton, MorphImages);
        mainMenu.setPadding(new Insets(20));
        mainMenu.setAlignment(Pos.CENTER);

        mainMenuScene = new Scene(mainMenu, 1020, 700);
        mainMenuScene.getStylesheets().add("app.css");
        primaryStage.setScene(mainMenuScene);
        ////////////////////////////////////FIN Scene 0 : Menu

        
        
        
        ////////////////////////////////////Scene 1 : Morphisme d'images prédéfinies
        
        ///TOP Box
        //On init le bouton de retour
        Button returnButton1 = createReturnButton(mainMenuScene);
        returnButton1.setOnAction(e -> primaryStage.setScene(mainMenuScene));
        ///FIN TOP BOX
        
        
        
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

        ////////////////////////Listes de séléction
        //Liste de gauche
        ComboBox<String> choixFormeDebutComboBox = new ComboBox<>();
        choixFormeDebutComboBox.getItems().addAll("Rectangle", "Triangle", "Carre","Losange","Autre");
        choixFormeDebutComboBox.setValue("Rectangle");
        choixFormeDebutComboBox.setOnAction(event -> {
            String formeDebut = choixFormeDebutComboBox.getValue();
            System.out.println("Forme selectionnee : " + formeDebut);
            this.formeDebut = creerForme(formeDebut);
            dessiner();
        });
        //Fin liste de gauche


        //Liste de Droite
        ComboBox<String> choixFormeFinComboBox = new ComboBox<>();
        choixFormeFinComboBox.getItems().addAll("Rectangle", "Triangle", "Carre","Losange","Autre");
        choixFormeFinComboBox.setValue("Rectangle");
        choixFormeFinComboBox.setOnAction(event -> {
            String formeFin = choixFormeFinComboBox.getValue();
            this.formeFin = creerForme(formeFin);
            dessiner();
        });
        //Fin liste de droite
        ////////////////////////Fin liste de séléction

        ////////////////////MidBox
        Button resetButton2 = new Button("Réinitialiser");
        resetButton2.setOnAction(event -> {
            //efface les points de contrôle de début et de fin
            pointsControleDebut.clear();
            pointsControleFin.clear();
            alphabetIndex[0]=0;
            gcResultat.clearRect(0, 0, LARGEUR_CANVAS, HAUTEUR_CANVAS);  //effacer le canevas

            //dessine à nouveau les formes pour effacer les points de contrôle du canvas
            dessiner();
        });
        
        //champ nb d'étapes
        TextField champEtapes = createIntegerField("10");
        Label etapesLabel = new Label("Nombre d'étapes:");

        //champ délai 
        TextField champDelai = createIntegerField("10");
        Label delaiLabel = new Label("Délai (ms):");
        
        Button boutonMorphing = new Button("Morphisme");
        boutonMorphing.setOnAction(e -> {
            int nbEtapes = Integer.parseInt(champEtapes.getText());  //recup le nombre d'étapes
            int delai = Integer.parseInt(champDelai.getText());  //recup le délai

            //génère formes intermédiaires avec le nouveau nombre d'étapes
            moteurMorphing = new MorphingFormesSimples(formeDebut, formeFin, pointsControleDebut, pointsControleFin, nbEtapes);
            formes = moteurMorphing.genererFormesIntermediaires();

            demarrerMorphing(gcResultat, canvasResultat, delai); 
        });
        
        Pane canvasResContainer = new Pane();
        canvasResContainer.getChildren().add(canvasResultat);
        //Setup de la position de notre canva
        canvasResultat.setLayoutX(60); 
        canvasResultat.setLayoutY(0); 

        VBox midBox2 = new VBox(10, boutonMorphing, resetButton2,champEtapes,etapesLabel,champDelai,delaiLabel,canvasResContainer);
        midBox2.setAlignment(Pos.CENTER);
        /////////////////////////Fin MidBox
        
        ///Bottom Box
        // Bouton pour afficher les informations du projet
        Button infoButton2 = new Button("?");
        infoButton2.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.google.com"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        HBox bottomHBox = new HBox(10,infoButton2);
        //Fin Bottom Box
        
        ///Settings CSS
        choixFormeDebutComboBox.getStyleClass().add("LR_Button");
        choixFormeDebutComboBox.getStyleClass().add("combo-box");
        choixFormeFinComboBox.getStyleClass().add("LR_Button");
        choixFormeFinComboBox.getStyleClass().add("combo-box");
        infoButton2.getStyleClass().add("Info_Button");
        boutonMorphing.getStyleClass().add("Mid_Button");
        resetButton2.getStyleClass().add("Mid_Button");
        champEtapes.getStyleClass().add("Mid_TextField");
        champDelai.getStyleClass().add("Mid_TextField");
        etapesLabel.getStyleClass().add("Mid_Label");
        delaiLabel.getStyleClass().add("Mid_Label");


        VBox leftVBox2 = new VBox(10, canvasGauche, choixFormeDebutComboBox);
        leftVBox2.setAlignment(Pos.CENTER);

        VBox rightVBox2 = new VBox(10, canvasDroite, choixFormeFinComboBox);
        rightVBox2.setAlignment(Pos.CENTER);

        BorderPane bp2 = new BorderPane();
        bp2.getStyleClass().add("root");
        bp2.setBottom(bottomHBox);
        bp2.setLeft(leftVBox2);
        bp2.setCenter(midBox2);
        bp2.setRight(rightVBox2);
        bp2.setTop(returnButton1);
        bp2.setPadding(new javafx.geometry.Insets(20));
        sceneSimple = new Scene(bp2, 1020, 700);
        sceneSimple.getStylesheets().add("app.css");
        ////////////////////////////////////FIN : Scene 1 : Morphisme d'images prédéfinies


        
        
        
        
        
        
        
        ////////////////////////////////////Scene 2 : Morphisme de forme Arrondie
        ImageView leftImageView2 = createImageView();
        ImageView rightImageView2 = createImageView();

        Image returnImage2 = new Image(getClass().getResourceAsStream("return.png"));
        ImageView returnImageView2 = new ImageView(returnImage2);
        returnImageView2.setFitWidth(15);
        returnImageView2.setFitHeight(15);
        Button ReturnBtn = new Button();
        ReturnBtn.setGraphic(returnImageView2);
        ReturnBtn.setOnAction(e -> primaryStage.setScene(mainMenuScene));

        Button leftImageBtn = new Button("Choisir l'image de gauche");
        leftImageBtn.setOnAction(e -> chooseImage(leftImageView2));

        Button rightImageBtn = new Button("Choisir l'image de droite");
        rightImageBtn.setOnAction(e -> chooseImage(rightImageView2));


        Button midButton = new Button("Morphisme");
        
        Button infoButton = new Button("?");
        infoButton.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.google.com"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ReturnBtn.getStyleClass().add("Return_Button");
        leftImageBtn.getStyleClass().add("LR_Button");
        rightImageBtn.getStyleClass().add("LR_Button");
        infoButton.getStyleClass().add("Info_Button");
        midButton.getStyleClass().add("Mid_Button");

        // Création de la mise en page principale
        VBox leftVBox = new VBox(10, leftImageView2, leftImageBtn);
        leftVBox.setAlignment(Pos.CENTER);

        VBox rightVBox = new VBox(10, rightImageView2, rightImageBtn);
        rightVBox.setAlignment(Pos.CENTER);


        BorderPane bp1 = new BorderPane();
        bp1.getStyleClass().add("root");
        bp1.setBottom(infoButton);
        bp1.setLeft(leftVBox);
        bp1.setCenter(midButton);
        bp1.setRight(rightVBox);
        bp1.setTop(ReturnBtn);
        bp1.setPadding(new javafx.geometry.Insets(20));
        // Création de la scène
        sceneArrondie = new Scene(bp1, 1020, 700); // Initialiser la variable sceneArrondie
        sceneArrondie.getStylesheets().add("app.css");
        ////////////////////////////////////FIN : Scene 2 : Morphisme de forme Arrondie


        // Configuration de la fenêtre principale
        primaryStage.setTitle("Application de Morphing");
        primaryStage.show();

    }
    
    private TextField createIntegerField(String initialValue) {
        TextField textField = new TextField(initialValue);
        textField.setPrefWidth(50);

        // Créer un UnaryOperator pour limiter les entrées à des nombres entiers positifs
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (Pattern.matches("\\d*", newText)) {
                return change;
            }
            return null;
        };

        // Appliquer le filtre au TextFormatter
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        textField.setTextFormatter(textFormatter);

        return textField;
    }

    
    //Initialiser les images (toutes les scènes)
    private ImageView createImageView() {
        ImageView imageView = new ImageView();
        imageView.setImage(new Image("left.png"));
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        imageView.getStyleClass().add("imageView");
        return imageView;
    }

    //Update l'image avec une image choisie par l'utilisateur (Scène 2 et 3)
    private void chooseImage(ImageView imageView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            Image scaledImage = scaleImage(image, 300, 300);
            imageView.setImage(scaledImage);
        }
    }
    
    //Scale l'image (auto) Scène 1,2,3
    private Image scaleImage(Image image, double targetWidth, double targetHeight) {
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(targetWidth);
        imageView.setFitHeight(targetHeight);
        return imageView.snapshot(null, null);
    }
    
    //Méthode pour créer le bouton de retour, Scène 1,2,3
    private Button createReturnButton(Scene scene) {
        Image returnImage = new Image(getClass().getResourceAsStream("return.png"));
        ImageView returnImageView = new ImageView(returnImage);
        returnImageView.setFitWidth(15);
        returnImageView.setFitHeight(15);
        Button returnButton = new Button();
        returnButton.setGraphic(returnImageView);
        returnButton.getStyleClass().add("Return_Button");
        return returnButton;
    }
    
    
    ////////////////////Méthodes de création de formes Scène 1
    ////////////Méthode Scène 1
    //crée forme selon choix user 
    private Forme creerForme(String typeForme) {
        switch (typeForme) {
            case "Rectangle":
                return creerRectangle();
            case "Triangle":
                return creerTriangle();
            case "Carre":
                return creerCarre();
            case "Losange":
                return creerLosange();
            default :
            	System.err.println("Erreur pas de forme associée.");
        }
        return null;
    }
    
    ////Méthode Scène 1
    private Forme creerRectangle() {
        Point[] points = {
                new Point(50, 50),
                new Point(250, 50),
                new Point(250, 200),
                new Point(50, 200)
        };
        return new Forme(points);
    }
    ////Méthode Scène 1
    private Forme creerTriangle() {
        Point[] points = {
                new Point(100, 50),
                new Point(250, 200),
                new Point(50, 200), 
                new Point(50, 200)
        };
        return new Forme(points);
    }
    ////Méthode Scène 1
    private Forme creerCarre() {
        Point[] points = {
	    		new Point(50, 50),
	    	    new Point(200, 50),
	    	    new Point(200, 200),
	    	    new Point(50, 200)
        };
        return new Forme(points);
    }
    
    private Forme creerLosange() {
        Point[] points = {
                new Point(100, 0),
                new Point(200, 100),
                new Point(100, 200),
                new Point(0, 100)
        };
        return new Forme(points);
    }
    ////////////////////Fin Méthodes de création de formes Scène 1

    ////Méthode Scène 1 sur Canva
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

    ////Méthode Scène 1
    private void demarrerMorphing(GraphicsContext gc, Canvas canvas, int delai) {
        File dossier = new File("./FormesSimples");
        etapeCourante = 0;
        // Vérifier si le dossier existe
        if (dossier.exists() && dossier.isDirectory()) {
            //suppression et création du dossier
            System.out.println("Le dossier existe.");
            supprimerDossier(dossier);
            dossier.mkdirs();
        } else {
            System.out.println("Le dossier n'existe pas.");
            //création du dossier
            dossier.mkdirs();
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(delai), e -> {
            etapeCourante++;
            if (etapeCourante >= formes.size()) {
                etapeCourante = 0; 
            }
            dessinerForme(gc, formes.get(etapeCourante),canvas);
        }));
        timeline.setCycleCount(formes.size() - 1);  //animation sur nbEtapes - 1 pour s'arrêter à la dernière
        timeline.play();
    }

    ////Méthode Scène 1
    //dessine formes intermédiaires
    private void dessinerForme(GraphicsContext gc, Forme forme, Canvas canvas) {
        gc.clearRect(0, 0, LARGEUR_CANVAS, HAUTEUR_CANVAS);  //effacer le canevas
        gc.setStroke(Color.BLACK);
        Point[] points = forme.getPoints();
        for (int i = 0; i < points.length; i++) {
            Point p1 = points[i];
            Point p2 = points[(i + 1) % points.length];
            gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
        //capture d'écran du canvas
        WritableImage snapshot = canvas.snapshot(null, new WritableImage(LARGEUR_CANVAS, HAUTEUR_CANVAS));
        
        //attribution d'un nom unique pour chaque image générée
        String cheminFichier = "./FormesSimples/image_"+System.currentTimeMillis()+".png";

        //enregistrement de l'image
        File fichierImage = new File(cheminFichier);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", fichierImage);
            System.out.println("Image enregistrée avec succès.");
        } catch (IOException e) {
            System.out.println("Erreur lors de l'enregistrement de l'image : " + e.getMessage());
        }
        
    }

    //vérifie si la souris est sur un point de contrôle existant
    private boolean isMouseOnPoint(double mouseX, double mouseY, Point point) {
        double distance = Math.sqrt(Math.pow(mouseX - point.getX(), 2) + Math.pow(mouseY - point.getY(), 2));
        return distance <= 5; //rayon de détection du point de contrôle
    }
    
    //permet de supprimer un dossier
    public static boolean supprimerDossier(File dossier) {
        if (dossier.isDirectory()) {
            // Récupérer la liste des fichiers et sous-dossiers du dossier
            File[] fichiers = dossier.listFiles();
            if (fichiers != null) {
                for (File fichier : fichiers) {
                    // Récursivement supprimer chaque fichier ou sous-dossier
                    if (!supprimerDossier(fichier)) {
                        return false; // Arrêter si la suppression échoue pour l'un des fichiers
                    }
                }
            }
        }
        // Supprimer le dossier lui-même après avoir supprimé son contenu
        return dossier.delete();
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}
