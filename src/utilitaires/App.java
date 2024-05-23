package utilitaires;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import controleurs.*; 

import java.util.HashMap;
import java.util.Map; 

public class App extends Application {

	public static Map<Character, Point> pointsControleDebut = new HashMap<>();
	public static Map<Character, Point> pointsControleFin = new HashMap<>();

    private Scene mainMenuScene;
    private Scene sceneSimple;
    private Scene sceneArrondie;
    //private Scene sceneComplexe;
    

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
        //MorphArrondiButton.setOnAction(e -> primaryStage.setScene(sceneComplexe));
        MorphImages.getStyleClass().add("Menu_Button");

        VBox mainMenu = new VBox(15);
        mainMenu.getChildren().addAll(MorphSimpleButton, MorphArrondiButton, MorphImages);
        mainMenu.setPadding(new Insets(20));
        mainMenu.setAlignment(Pos.CENTER);

        mainMenuScene = new Scene(mainMenu, 1020, 700);
        mainMenuScene.getStylesheets().add("app.css");
        primaryStage.setScene(mainMenuScene);
        ////////////////////////////////////FIN Scene 0 : Menu

        
        
        
        ////////////////////////////////////Scene 1 : Morphisme d'images simples  
        
        ///TOP
        //On init le bouton de retour
        Button boutonRetour = creerBoutonRetour(mainMenuScene);
        boutonRetour.setOnAction(new RetourHandler(primaryStage, mainMenuScene));
        ///FIN TOP
        
        ///BOTTOM
        //Rediriger vers les informations du projet
        Button boutonInfo = new Button("?");
        boutonInfo.getStyleClass().add("Info_Button");
        boutonInfo.setOnAction(new InfoHandler());
        HBox bottomHBox = new HBox(10,boutonInfo);
        ///FIN BOTTOM 
        
        ///LEFT
        ImageView imageGauche = creerImageView();
        Button boutonIMGGauche = new Button("Choisir l'image de gauche");
        boutonIMGGauche.setOnAction(new ChoixIMGHandler(imageGauche));

        //add image view dans pane pour add pt controle 
        StackPane leftPane = new StackPane(); 
        leftPane.getChildren().add(imageGauche); 
        
        VBox leftVBox = new VBox(10, leftPane, boutonIMGGauche);
        leftVBox.setAlignment(Pos.CENTER);
        
        ///FIN LEFT

        //RIGHT
        ImageView imageDroite = creerImageView();
        Button boutonIMGDroite = new Button("Choisir l'image de droite");
        boutonIMGDroite.setOnAction(new ChoixIMGHandler(imageDroite));

        //add image view dans pane pour add pt controle 
        StackPane rightPane = new StackPane(); 
        rightPane.getChildren().add(imageDroite);

        VBox rightVBox = new VBox(10, rightPane, boutonIMGDroite);
        rightVBox.setAlignment(Pos.CENTER);
        ///FIN RIGHT
        
        //instance de PointsControleHandler
        PointsControleHandler handler = new PointsControleHandler(leftPane, rightPane);

        //associer les gestionnaires d'événements aux panneaux
        leftPane.setOnMouseClicked(event -> handler.handleLeftPaneClick(event));

        ///MID
        Button boutonReset = new Button("Réinitialiser");
        boutonReset.setOnAction(event -> {
            System.out.println("Points de contrôle remis à 0");
            pointsControleDebut.clear();
            pointsControleFin.clear();
        });
        boutonReset.setOnAction(event -> handler.handleReset(event)); 
        //champ nb d'étapes
        TextField champEtapes = creerChampNbEntier("24"); //On fait en sorte de n'avoir que des entiers positifs
        Label etapesLabel = new Label("Nombre d'étapes:");
        //champ délai 
        TextField champDelai = creerChampNbEntier("40"); //On fait en sorte de n'avoir que des entiers positifs
        Label delaiLabel = new Label("Délai (ms):");
        
        Button boutonMorphing = new Button("Morphisme");
        boutonMorphing.setOnAction(new MorphingSimpleHandler(champEtapes, champDelai, imageGauche, handler));
        
        VBox midBox = new VBox(10, boutonMorphing, boutonReset,champEtapes,etapesLabel,champDelai,delaiLabel);
        midBox.setAlignment(Pos.CENTER);
        ///FIN MID

        ///CREATION DU BP
        BorderPane borderpaneMethodeSimple = creerBorderPane(boutonRetour, bottomHBox, leftVBox, midBox, rightVBox);
        ///FIN CREATION DU BP
  
        ///CSS :
        //TOP
        boutonRetour.getStyleClass().add("Return_Button");
        //LEFT
        boutonIMGGauche.getStyleClass().add("LR_Button");
        //RIGHT
        boutonIMGDroite.getStyleClass().add("LR_Button");
        //BOT (cf CreerBouton)
        //MID
        boutonMorphing.getStyleClass().add("Mid_Button");
        boutonReset.getStyleClass().add("Mid_Button");
        champEtapes.getStyleClass().add("Mid_TextField");
        champDelai.getStyleClass().add("Mid_TextField");
        etapesLabel.getStyleClass().add("Mid_Label");
        delaiLabel.getStyleClass().add("Mid_Label");
        ///FIN CSS
        
        ///Génération de la Scène
        sceneSimple = new Scene(borderpaneMethodeSimple, 1020, 700);
        sceneSimple.getStylesheets().add("app.css");
        ////////////////////////////////////FIN : Scene 1 : Morphisme d'images simples

        
        
        

        ////////////////////////////////////DEBUT : Scene 2 : Morphisme d'images arrondies
        ///TOP
        //On init le bouton de retour
        Button boutonRetour2 = creerBoutonRetour(mainMenuScene);
        boutonRetour2.setOnAction(new RetourHandler(primaryStage, mainMenuScene));
        ///FIN TOP
        
        ///BOTTOM
        //Rediriger vers les informations du projet
        Button boutonInfo2 = new Button("?");
        boutonInfo2.getStyleClass().add("Info_Button");
        boutonInfo2.setOnAction(new InfoHandler());
        HBox bottomHBox2 = new HBox(10,boutonInfo2);
        ///FIN BOTTOM 
        
        ///LEFT
        ImageView imageGauche2 = creerImageView();
        Button boutonIMGGauche2 = new Button("Choisir l'image de gauche");
        boutonIMGGauche2.setOnAction(new ChoixIMGHandler(imageGauche2));

        //add image view dans pane pour add pt controle 
        StackPane leftPane2 = new StackPane(); 
        leftPane2.getChildren().add(imageGauche2); 
        
        VBox leftVBox2 = new VBox(10, leftPane2, boutonIMGGauche2);
        leftVBox2.setAlignment(Pos.CENTER);
        
        ///FIN LEFT

        //RIGHT
        ImageView imageDroite2 = creerImageView();
        Button boutonIMGDroite2 = new Button("Choisir l'image de droite");
        boutonIMGDroite2.setOnAction(new ChoixIMGHandler(imageDroite2));

        //add image view dans pane pour add pt controle 
        StackPane rightPane2 = new StackPane(); 
        rightPane2.getChildren().add(imageDroite2);

        VBox rightVBox2 = new VBox(10, rightPane2, boutonIMGDroite2);
        rightVBox2.setAlignment(Pos.CENTER);
        ///FIN RIGHT
        
        //instance de PointsControleHandler
        PointsControleHandler handler2 = new PointsControleHandler(leftPane2, rightPane2);

        //associer les gestionnaires d'événements aux panneaux
        leftPane2.setOnMouseClicked(event -> handler2.handleLeftPaneClick(event));

        ///MID
        Button boutonReset2 = new Button("Réinitialiser");
        boutonReset2.setOnAction(event -> {
            System.out.println("Points de contrôle remis à 0");
            pointsControleDebut.clear();
            pointsControleFin.clear();
        });
        boutonReset2.setOnAction(event -> handler2.handleReset(event)); 
        //champ nb d'étapes
        TextField champEtapes2 = creerChampNbEntier("24"); //On fait en sorte de n'avoir que des entiers positifs
        Label etapesLabel2 = new Label("Nombre d'étapes:");
        //champ délai 
        TextField champDelai2 = creerChampNbEntier("40"); //On fait en sorte de n'avoir que des entiers positifs
        Label delaiLabel2 = new Label("Délai (ms):");
        
        Button boutonMorphing2 = new Button("Morphisme");
        Label loadingLabel = new Label("Chargement du morphing...");
        loadingLabel.setVisible(false);
        boutonMorphing2.setOnAction(new MorphingArrondiHandler(champEtapes2, champDelai2, imageGauche2, handler2));

        VBox midBox2 = new VBox(10, loadingLabel, boutonMorphing2, boutonReset2,champEtapes2,etapesLabel2,champDelai2,delaiLabel2);
        midBox2.setAlignment(Pos.CENTER);
        ///FIN MID

        ///CREATION DU BP
        BorderPane borderpaneMethodeArrondi = creerBorderPane(boutonRetour2, bottomHBox2, leftVBox2, midBox2, rightVBox2);
        ///FIN CREATION DU BP
  
        ///CSS :
        //TOP
        boutonRetour2.getStyleClass().add("Return_Button");
        //LEFT
        boutonIMGGauche2.getStyleClass().add("LR_Button");
        //RIGHT
        boutonIMGDroite2.getStyleClass().add("LR_Button");
        //BOT (cf CreerBouton)
        //MID
        boutonMorphing2.getStyleClass().add("Mid_Button");
        boutonReset2.getStyleClass().add("Mid_Button");
        champEtapes2.getStyleClass().add("Mid_TextField");
        champDelai2.getStyleClass().add("Mid_TextField");
        etapesLabel2.getStyleClass().add("Mid_Label");
        delaiLabel2.getStyleClass().add("Mid_Label");
        ///FIN CSS
        
        ///Génération de la Scène
        sceneArrondie = new Scene(borderpaneMethodeArrondi, 1020, 700);
        //sceneArrondie.getStylesheets().add("app.css");
        ////////////////////////////////////FIN : Scene 2 : Morphisme d'images arrondies

        primaryStage.setTitle("Application de Morphing");
        primaryStage.show();

    }
    
    private TextField creerChampNbEntier(String initialValue) {
        TextField champTexte = new TextField(initialValue);
        champTexte.setPrefWidth(50);

        //On fait un opérateur pour limiter les entrées à des nombres entiers positifs
        UnaryOperator<TextFormatter.Change> filtre = change -> {
            String nouvText = change.getControlNewText();
            if (Pattern.matches("\\d*", nouvText)) {
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filtre);
        champTexte.setTextFormatter(textFormatter);

        return champTexte;
    }

    
    //Initialiser les images (toutes les scènes)
    private ImageView creerImageView() {
        ImageView imageView = new ImageView();
        imageView.setImage(new Image("left.png"));
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        imageView.getStyleClass().add("imageView");
        return imageView;
    }
    
    //Méthode pour créer le bouton de retour
    private Button creerBoutonRetour(Scene scene) {
        Image imageRetour = new Image(getClass().getResourceAsStream("return.png"));
        ImageView imageRetourView = new ImageView(imageRetour);
        imageRetourView.setFitWidth(15);
        imageRetourView.setFitHeight(15);
        Button boutonRetour = new Button();
        boutonRetour.setGraphic(imageRetourView);
        boutonRetour.getStyleClass().add("Return_Button");
        return boutonRetour;
    }
    
    private BorderPane creerBorderPane(Button boutonRetour, HBox bottomHBox, VBox leftVBox, VBox midBox, VBox rightVBox) {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("root");
        borderPane.setTop(boutonRetour);
        borderPane.setBottom(bottomHBox);
        borderPane.setLeft(leftVBox);
        borderPane.setCenter(midBox);
        borderPane.setRight(rightVBox);
        borderPane.setPadding(new javafx.geometry.Insets(20));
        return borderPane;
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
/* 
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.Group; 
import javafx.scene.Scene; 
import javafx.scene.paint.Color; 
import utilitaires.*;
*//*
public class App extends Application{
    @Override 
    public void start(Stage stage){
        Map<Character, Point> pointsControle = new HashMap<>();
        pointsControle.put('A', new Point(100, 100));
        pointsControle.put('B', new Point(160, 60));
        pointsControle.put('C', new Point(240, 240));
        pointsControle.put('D', new Point(300, 100));
        pointsControle.put('E', new Point(300, 100));
        pointsControle.put('F', new Point(240, 340));
        pointsControle.put('G', new Point(160, 140));
        pointsControle.put('H', new Point(100, 100));
        Group root = new Group();
        // Générer et tracer la courbe de Bézier fermée
        //traceBezierCurve(root, pointsControle);

        // Ajouter des cercles pour visualiser les points de contrôle
        for (Map.Entry<Character, Point> entry : pointsControle.entrySet()) {
            Point point = entry.getValue();
            Circle circle = new Circle(point.getX(), point.getY(), 3, Color.RED);
            root.getChildren().add(circle);
        }
        Scene scene = new Scene(root, 400,200); 
        stage.setScene(scene);
        stage.setTitle("titre");
        stage.show(); 
    }
}*/
