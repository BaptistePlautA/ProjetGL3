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

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map; 

import controleurs.*; 
import utilitaires.*;

/**
 * Classe App
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class App extends Application {

	public static Map<Character, Point> pointsControleDebut = new HashMap<>();
	public static Map<Character, Point> pointsControleFin = new HashMap<>();

    private Scene mainMenuScene;
    private Scene sceneSimple;
    private Scene sceneArrondie;
    private Scene sceneComplexe;

    @Override
    public void start(Stage primaryStage) {

        //setup de l'icône de l'app
        Image icon = new Image(getClass().getResourceAsStream("ressources/icon.png"));
        primaryStage.getIcons().add(icon);




        ////////////////////////////////////Scene 0 : Menu
        //choix du type de morphisme 

        Button MorphSimpleButton = new Button("Morphing de formes simples");
        MorphSimpleButton.setOnAction(e -> primaryStage.setScene(sceneSimple));
        MorphSimpleButton.getStyleClass().add("Bouton");
        MorphSimpleButton.getStyleClass().add("MenuBouton");


        Button MorphArrondiButton = new Button("Morphing de formes arrondies");
        MorphArrondiButton.setOnAction(e -> primaryStage.setScene(sceneArrondie));
        MorphArrondiButton.getStyleClass().add("Bouton");
        MorphArrondiButton.getStyleClass().add("MenuBouton");


        Button MorphImages = new Button("Morphing d'images");
        MorphImages.setOnAction(e -> primaryStage.setScene(sceneComplexe));
        MorphImages.getStyleClass().add("Bouton");
        MorphImages.getStyleClass().add("MenuBouton");

        VBox mainMenu = new VBox(15);
        mainMenu.getChildren().addAll(MorphSimpleButton, MorphArrondiButton, MorphImages);
        mainMenu.setPadding(new Insets(20));
        mainMenu.setAlignment(Pos.CENTER);

        mainMenuScene = new Scene(mainMenu, 1020, 700);
        mainMenuScene.getStylesheets().add("ressources/app.css");
        primaryStage.setScene(mainMenuScene);
        ////////////////////////////////////FIN Scene 0 : Menu

        
        
        
        ////////////////////////////////////Scene 1 : Morphisme d'images simples  
    
        ///TOP
        //On init le bouton de retour
        Button boutonRetour = creerBoutonRetour(mainMenuScene);
        boutonRetour.setOnAction(new RetourHandler(primaryStage, mainMenuScene));
        ///FIN TOP
        

        ///BOTTOM
        //Rediriger vers les informations du projet (github)
        Button boutonInfo = new Button("?");
        boutonInfo.getStyleClass().add("Bouton");
        boutonInfo.getStyleClass().add("Bouton2");
        boutonInfo.setOnAction(new InfoHandler());
        HBox bottomHBox = new HBox(10,boutonInfo);
        ///FIN BOTTOM 
        

        ///LEFT
        ImageView imageGauche = creerImageView();
        Button boutonIMGGauche = new Button("Choisir l'image de gauche");
        boutonIMGGauche.setOnAction(new ChoixIMGHandler(imageGauche));

        //add image view dans pane pour add points de contrôle (ie on clique sur la pane pas l'image view)
        StackPane leftPane = new StackPane(); 
        leftPane.setPrefSize(300, 300);
        leftPane.setMaxSize(300, 300);
        leftPane.getChildren().add(imageGauche); 
        
        VBox leftVBox = new VBox(10, leftPane, boutonIMGGauche);
        leftVBox.setAlignment(Pos.CENTER);
        ///FIN LEFT


        ///RIGHT
        ImageView imageDroite = creerImageView();
        Button boutonIMGDroite = new Button("Choisir l'image de droite");
        boutonIMGDroite.setOnAction(new ChoixIMGHandler(imageDroite));

        //add image view dans pane pour add points de contrôle (ie on clique sur la pane pas l'image view)
        StackPane rightPane = new StackPane(); 
        rightPane.getChildren().add(imageDroite);

        VBox rightVBox = new VBox(10, rightPane, boutonIMGDroite);
        rightVBox.setAlignment(Pos.CENTER);
        ///FIN RIGHT


        //traitement des pane (qui contiennent les images) pour add les points de contrôle 
        //instance de PointsControleHandler
        PointsControleHandler handler = new PointsControleHandler(leftPane, rightPane, null);
        //associer les gestionnaires d'événements aux panneaux
        leftPane.setOnMouseClicked(event -> handler.handleLeftPaneClick(event));


        ///MID
        //bouton pour réinitialiser les points de contrôle 
        Button boutonReset = new Button("Réinitialiser");
        boutonReset.setOnAction(event -> {
            System.out.println("Points de contrôle remis à 0");
            pointsControleDebut.clear();
            pointsControleFin.clear();
        });
        boutonReset.setOnAction(event -> handler.handleReset(event)); 

        //champ nb d'étapes
        TextField champEtapes = creerChampNbEntier("24"); //on fait en sorte de n'avoir que des entiers positifs
        champEtapes.setAlignment(Pos.CENTER);
        Label etapesLabel = new Label("Nombre d'étapes:");

        //champ délai 
        TextField champDelai = creerChampNbEntier("40"); //on fait en sorte de n'avoir que des entiers positifs
        champDelai.setAlignment(Pos.CENTER);
        Label delaiLabel = new Label("Délai (ms):");
        
        //bouton pour lancer le morphisme 
        Button boutonMorphing = new Button("Morphisme");
        boutonMorphing.setOnAction(new MorphingSimpleHandler(champEtapes, champDelai, imageGauche, handler));
        
        //agencement des éléments du milieu de la pane 
        VBox boutonBox = new VBox(10, boutonMorphing, boutonReset);  
        VBox champsBox1 = new VBox(3, etapesLabel,champEtapes);  
        VBox champsBox2 = new VBox(3, delaiLabel,champDelai); 
        VBox champsBox = new VBox(10, champsBox1, champsBox2); 
        VBox midBox = new VBox(40, boutonBox,champsBox);
        midBox.setAlignment(Pos.TOP_CENTER);
        boutonBox.setAlignment(Pos.CENTER);
        champsBox1.setAlignment(Pos.CENTER);
        champsBox2.setAlignment(Pos.CENTER);
        champsBox.setAlignment(Pos.CENTER);
        midBox.setPadding(new Insets(150,50,0,50));   
        HBox mid = new HBox(10, leftVBox, midBox, rightVBox); 
        mid.setAlignment(Pos.CENTER);
        ///FIN MID


        ///CREATION DU BP
        BorderPane borderpaneMethodeSimple = creerBorderPane(boutonRetour, bottomHBox,mid);
        ///FIN CREATION DU BP
  

        ///CSS :
        //TOP
        boutonRetour.getStyleClass().add("Bouton");
        boutonRetour.getStyleClass().add("Bouton2");
        //LEFT
        boutonIMGGauche.getStyleClass().add("Bouton");
        leftPane.getStyleClass().add("Pane");
        //RIGHT
        boutonIMGDroite.getStyleClass().add("Bouton");
        rightPane.getStyleClass().add("Pane");
        //BOT (cf CreerBouton)
        //MID
        boutonMorphing.getStyleClass().add("Bouton");
        boutonMorphing.getStyleClass().add("Bouton3");
        boutonReset.getStyleClass().add("Bouton");
        boutonReset.getStyleClass().add("Bouton3");
        champEtapes.getStyleClass().add("Mid_TextField");
        champDelai.getStyleClass().add("Mid_TextField");
        etapesLabel.getStyleClass().add("Mid_Label");
        delaiLabel.getStyleClass().add("Mid_Label");
        ///FIN CSS
        

        ///Génération de la Scène
        sceneSimple = new Scene(borderpaneMethodeSimple, 1020, 700);
        sceneSimple.getStylesheets().add("ressources/app.css");
        ////////////////////////////////////FIN : Scene 1 : Morphisme d'images simples

        
        
        

        ////////////////////////////////////DEBUT : Scene 2 : Morphisme d'images arrondies

        ///TOP
        //On init le bouton de retour
        Button boutonRetour2 = creerBoutonRetour(mainMenuScene);
        boutonRetour2.setOnAction(new RetourHandler(primaryStage, mainMenuScene));
        ///FIN TOP
        

        ///BOTTOM
        //Rediriger vers les informations du projet (github)
        Button boutonInfo2 = new Button("?");
        boutonInfo2.getStyleClass().add("Bouton");
        boutonInfo2.getStyleClass().add("Bouton2");
        boutonInfo2.setOnAction(new InfoHandler());
        HBox bottomHBox2 = new HBox(10,boutonInfo2);
        ///FIN BOTTOM 
        

        ///LEFT
        ImageView imageGauche2 = creerImageView();
        Button boutonIMGGauche2 = new Button("Choisir l'image de gauche");
        boutonIMGGauche2.setOnAction(new ChoixIMGHandler(imageGauche2));

        //add image view dans pane pour add points de contrôle (ie on clique sur la pane pas l'image view) 
        StackPane leftPane2 = new StackPane(); 
        leftPane2.setPrefSize(300,300);
        leftPane2.getChildren().add(imageGauche2); 
        
        VBox leftVBox2 = new VBox(10, leftPane2, boutonIMGGauche2);
        leftVBox2.setAlignment(Pos.CENTER);
        ///FIN LEFT


        ///RIGHT
        ImageView imageDroite2 = creerImageView();
        Button boutonIMGDroite2 = new Button("Choisir l'image de droite");
        boutonIMGDroite2.setOnAction(new ChoixIMGHandler(imageDroite2));

        //add image view dans pane pour add points de contrôle (ie on clique sur la pane pas l'image view)
        StackPane rightPane2 = new StackPane(); 
        rightPane2.getChildren().add(imageDroite2);

        VBox rightVBox2 = new VBox(10, rightPane2, boutonIMGDroite2);
        rightVBox2.setAlignment(Pos.CENTER);
        ///FIN RIGHT


        //traitement des pane (qui contiennent les images) pour add les points de contrôle 
        //instance de PointsControleHandler
        PointsControleIntermediairesPlacerHandler handler2 = new PointsControleIntermediairesPlacerHandler(leftPane2, rightPane2);
        //associer les gestionnaires d'événements aux panneaux
        leftPane2.setOnMouseClicked(handler2);


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
        champEtapes2.setAlignment(Pos.CENTER);
        Label etapesLabel2 = new Label("Nombre d'étapes:");
        
        //champ délai 
        TextField champDelai2 = creerChampNbEntier("40"); //On fait en sorte de n'avoir que des entiers positifs
        champDelai2.setAlignment(Pos.CENTER);
        Label delaiLabel2 = new Label("Délai (ms):");
        
        //bouton pour lancer le morphisme 
        Button boutonMorphing2 = new Button("Morphisme");
        Label loadingLabel = new Label("Chargement du morphing...");
        loadingLabel.setVisible(false);
        boutonMorphing2.setOnAction(new MorphingArrondiHandler(champEtapes2, champDelai2, imageGauche2, handler2));

        //agencement des éléments du milieu de la pane
        VBox boutonBoxArrondi = new VBox(10, boutonMorphing2, boutonReset2);  
        VBox champsBox3 = new VBox(3, etapesLabel2,champEtapes2);  
        VBox champsBox4 = new VBox(3, delaiLabel2,champDelai2); 
        VBox champsBoxArrondi = new VBox(10, champsBox3, champsBox4); 
        VBox midBox2 = new VBox(40, boutonBoxArrondi,champsBoxArrondi);
        midBox2.setAlignment(Pos.TOP_CENTER);
        boutonBoxArrondi.setAlignment(Pos.CENTER);
        champsBox3.setAlignment(Pos.CENTER);
        champsBox4.setAlignment(Pos.CENTER);
        champsBoxArrondi.setAlignment(Pos.CENTER);
        midBox2.setPadding(new Insets(150,50,0,50));        
        HBox mid2 = new HBox(10, leftVBox2, midBox2, rightVBox2);
        mid2.setAlignment(Pos.CENTER); 
        ///FIN MID


        ///CREATION DU BP
        BorderPane borderpaneMethodeArrondi = creerBorderPane(boutonRetour2, bottomHBox2,mid2);
        ///FIN CREATION DU BP



        ///CSS :
        //TOP
        boutonRetour2.getStyleClass().add("Bouton");
        boutonRetour2.getStyleClass().add("Bouton2");
        //LEFT
        boutonIMGGauche2.getStyleClass().add("Bouton");
        leftPane2.getStyleClass().add("Pane");
        //RIGHT
        boutonIMGDroite2.getStyleClass().add("Bouton");
        rightPane2.getStyleClass().add("Pane");
        //BOT (cf CreerBouton)
        //MID
        boutonMorphing2.getStyleClass().add("Bouton");
        boutonMorphing2.getStyleClass().add("Bouton3");
        boutonReset2.getStyleClass().add("Bouton");
        boutonReset2.getStyleClass().add("Bouton3");
        champEtapes2.getStyleClass().add("Mid_TextField");
        champDelai2.getStyleClass().add("Mid_TextField");
        etapesLabel2.getStyleClass().add("Mid_Label");
        delaiLabel2.getStyleClass().add("Mid_Label");
        ///FIN CSS
        

        ///Génération de la Scène
        sceneArrondie = new Scene(borderpaneMethodeArrondi, 1020, 700);
        sceneArrondie.getStylesheets().add("ressources/app.css");
        ////////////////////////////////////FIN : Scene 2 : Morphisme d'images arrondies




        ////////////////////////////////////DEBUT : Scene 3 : Morphisme d'images complexes
        ///TOP
        //On init le bouton de retour
        Button boutonRetour3 = creerBoutonRetour(mainMenuScene);
        boutonRetour3.setOnAction(new RetourHandler(primaryStage, mainMenuScene));
        ///FIN TOP
        
        ///BOTTOM
        //Rediriger vers les informations du projet (github)
        Button boutonInfo3 = new Button("?");
        boutonInfo3.getStyleClass().add("Bouton");
        boutonInfo3.getStyleClass().add("Bouton2");
        boutonInfo3.setOnAction(new InfoHandler());
        HBox bottomHBox3 = new HBox(10,boutonInfo3);
        ///FIN BOTTOM 
        

        ///LEFT
        ImageView imageGauche3 = creerImageView();
        Button boutonIMGGauche3 = new Button("Choisir l'image de gauche");
        boutonIMGGauche3.setOnAction(new ChoixIMGHandler(imageGauche3));

        //add image view dans pane pour add points de contrôle (ie on clique sur la pane pas l'image view) 
        StackPane leftPane3 = new StackPane(); 
        leftPane3.getChildren().add(imageGauche3); 
        
        VBox leftVBox3 = new VBox(10, leftPane3, boutonIMGGauche3);
        leftVBox3.setAlignment(Pos.CENTER);
        ///FIN LEFT


        ///RIGHT
        ImageView imageDroite3 = creerImageView();
        Button boutonIMGDroite3 = new Button("Choisir l'image de droite");
        boutonIMGDroite3.setOnAction(new ChoixIMGHandler(imageDroite3));

        //add image view dans pane pour add points de contrôle (ie on clique sur la pane pas l'image view)
        StackPane rightPane3 = new StackPane(); 
        rightPane3.getChildren().add(imageDroite3);

        VBox rightVBox3 = new VBox(10, rightPane3, boutonIMGDroite3);
        rightVBox3.setAlignment(Pos.CENTER);
        ///FIN RIGHT


        //traitement des pane (qui contiennent les images) pour add les points de contrôle
        //instance de PointsControleHandler
        PointsControleHandler handler3 = new PointsControleHandler(leftPane3, rightPane3, null);
        //associer les gestionnaires d'événements aux panneaux
        leftPane3.setOnMouseClicked(event -> handler3.handleLeftPaneClick(event));

        ///MID
        //bouton pour réinitialiser les points de contrôle 
        Button boutonReset3 = new Button("Réinitialiser");
        boutonReset3.setOnAction(event -> {
            System.out.println("Points de contrôle remis à 0");
            pointsControleDebut.clear();
            pointsControleFin.clear();
            handler3.setIndexPoint(0);
        });
        boutonReset3.setOnAction(event -> handler3.handleReset(event)); 
        
        //champ nb d'étapes
        TextField champEtapes3 = creerChampNbEntier("24"); //On fait en sorte de n'avoir que des entiers positifs
        champEtapes3.setAlignment(Pos.CENTER);
        Label etapesLabel3 = new Label("Nombre d'étapes:");
        
        //champ délai 
        TextField champDelai3 = creerChampNbEntier("40"); //On fait en sorte de n'avoir que des entiers positifs
        champDelai3.setAlignment(Pos.CENTER);
        Label delaiLabel3 = new Label("Délai (ms):");
        
        //bouton pour lancer le morphisme 
        Button boutonMorphing3 = new Button("Morphisme");
        Label loadingLabel3 = new Label("Chargement du morphing...");
        loadingLabel3.setVisible(false);
        boutonMorphing3.setOnAction(new MorphingComplexeHandler(champEtapes3, champDelai3, imageGauche3,imageDroite3, handler3));

        //agencement des éléments du milieu de la pane 
        VBox boutonBoxComplexe = new VBox(10, boutonMorphing3, boutonReset3);  
        VBox champsBox5 = new VBox(3, etapesLabel3,champEtapes3);  
        VBox champsBox6 = new VBox(3, delaiLabel3,champDelai3); 
        VBox champsBoxComplexe = new VBox(10, champsBox5, champsBox6); 
        VBox midBox3 = new VBox(40, boutonBoxComplexe,champsBoxComplexe);
        midBox3.setAlignment(Pos.TOP_CENTER);
        boutonBoxComplexe.setAlignment(Pos.CENTER);
        champsBox5.setAlignment(Pos.CENTER);
        champsBox6.setAlignment(Pos.CENTER);
        champsBoxComplexe.setAlignment(Pos.CENTER);
        midBox3.setPadding(new Insets(150,50,0,50));
        HBox mid3 = new HBox(10, leftVBox3, midBox3, rightVBox3); 
        mid3.setAlignment(Pos.CENTER); 
        ///FIN MID
        

        ///CREATION DU BP
        BorderPane borderpaneMethodeComplexe = creerBorderPane(boutonRetour3, bottomHBox3, mid3);
        ///FIN CREATION DU BP
  

        ///CSS :
        //TOP
        boutonRetour3.getStyleClass().add("Bouton");
        boutonRetour3.getStyleClass().add("Bouton2");
        //LEFT
        boutonIMGGauche3.getStyleClass().add("Bouton");
        leftPane3.getStyleClass().add("Pane");
        //RIGHT
        boutonIMGDroite3.getStyleClass().add("Bouton");
        rightPane3.getStyleClass().add("Pane");
        //BOT (cf CreerBouton)
        //MID
        boutonMorphing3.getStyleClass().add("Bouton");
        boutonMorphing3.getStyleClass().add("Bouton3");
        boutonReset3.getStyleClass().add("Bouton");
        boutonReset3.getStyleClass().add("Bouton3");
        champEtapes3.getStyleClass().add("Mid_TextField");
        champDelai3.getStyleClass().add("Mid_TextField");
        etapesLabel3.getStyleClass().add("Mid_Label");
        delaiLabel3.getStyleClass().add("Mid_Label");
        ///FIN CSS
        

        ///Génération de la Scène
        sceneComplexe = new Scene(borderpaneMethodeComplexe, 1020, 700);
        sceneComplexe.getStylesheets().add("ressources/app.css");
        ////////////////////////////////////FIN : Scene 3 : Morphisme d'images 


        primaryStage.setTitle("Application de Morphing");
        primaryStage.show();


    }
    
    /**
     * Fonction pour s'assurer que la valeur de l'entrée est un nombre entier positif 
     * @param initialValue
     * @return retourne un TextField 
     */
    private TextField creerChampNbEntier(String initialValue) {
        TextField champTexte = new TextField(initialValue);
        champTexte.setPrefWidth(40);

        //on fait un opérateur pour limiter les entrées à des nombres entiers positifs
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

    /**
     * Fonction pour initialiser les images 
     * @return retourne l'ImageView intialisée 
     */
    private ImageView creerImageView() {
        ImageView imageView = new ImageView();
        imageView.setImage(new Image("ressources/fond.png"));
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        imageView.getStyleClass().add("imageView");
        return imageView;
    }
    
    /**
     * Fonction pour créer le bouton de retour 
     * @param scene
     * @return retourne le bouton 
     */
    private Button creerBoutonRetour(Scene scene) {
        Image imageRetour = new Image(getClass().getResourceAsStream("ressources/return.png"));
        ImageView imageRetourView = new ImageView(imageRetour);
        imageRetourView.setFitWidth(15);
        imageRetourView.setFitHeight(15);
        Button boutonRetour = new Button();
        boutonRetour.setGraphic(imageRetourView);
        boutonRetour.getStyleClass().add("Return_Button");
        return boutonRetour;
    }
    
    /**
     * Fonction pour créer les BorderPane 
     * @param boutonRetour
     * @param bottomHBox
     * @param ouais
     * @return retourne le BorderPane
     */
    private BorderPane creerBorderPane(Button boutonRetour, HBox bottomHBox, HBox ouais) {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("root");
        borderPane.setTop(boutonRetour);
        borderPane.setBottom(bottomHBox);
        borderPane.setCenter(ouais);
        borderPane.setPadding(new javafx.geometry.Insets(20));
        return borderPane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}