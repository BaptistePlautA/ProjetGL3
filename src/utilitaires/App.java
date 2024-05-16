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
import utilitaires.Point;

import java.io.File;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import controleurs.ChoixIMGHandler;
import controleurs.InfoHandler;
import controleurs.MorphingSimpleHandler;
import controleurs.PointsControleHandler;
import controleurs.RetourHandler;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {

	 public static Map<Character, Point> pointsControleDebut = new HashMap<>();
	 public static Map<Character, Point> pointsControleFin = new HashMap<>();

    //private Scene sceneArrondie;
    private Scene mainMenuScene;
    private Scene sceneSimple;
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
       // MorphArrondiButton.setOnAction(e -> primaryStage.setScene(sceneArrondie));
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
        boutonMorphing.setOnAction(new MorphingSimpleHandler(champEtapes, champDelai));
        
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
