import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ImageMorphingApp extends Application {

    private Scene sceneArrondie;
    private Scene mainMenuScene;
    private Scene sceneSimple;

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

        mainMenuScene = new Scene(mainMenu, 1020, 500);
        mainMenuScene.getStylesheets().add("app.css");
        primaryStage.setScene(mainMenuScene);
        ////////////////////////////////////FIN Scene 0 : Menu

        
        
        
        ////////////////////////////////////Scene 1 : Morphisme d'images prédéfinies
        //On init les 2 images par défaut
        ImageView leftImageView = createImageView();
        ImageView rightImageView = createImageView();

        //On init le bouton de retour
        Button returnButton1 = createReturnButton(mainMenuScene);
        returnButton1.setOnAction(e -> primaryStage.setScene(mainMenuScene));


        ////////////////////////Listes de séléction
        List<String> formes = new ArrayList<>();
        formes.add("Carre");
        formes.add("Croix");
        formes.add("Losange");
        formes.add("Parallelogramme");
        formes.add("Fleche Pentagone");
        formes.add("Trapeze");
        formes.add("Triangle");
        formes.add("Triangle Rectangle");

        //Liste de gauche
        ComboBox<String> formesComboBox = new ComboBox<>();
        formesComboBox.getItems().addAll(formes);
        formesComboBox.setPromptText("Selectionner une forme");

        formesComboBox.setOnAction(e -> {
            String selectedForme = formesComboBox.getValue();
            System.out.println("Forme selectionnee : " + selectedForme);
            updateImageView(selectedForme, leftImageView);
        });
        //Fin liste de gauche


        //Liste de Droite
        ComboBox<String> formesComboBoxR = new ComboBox<>();
        formesComboBoxR.getItems().addAll(formes);
        formesComboBoxR.setPromptText("Selectionner une forme");

        formesComboBoxR.setOnAction(e -> {
            String selectedForme = formesComboBoxR.getValue();
            System.out.println("Forme selectionne a droite : " + selectedForme);
            updateImageView(selectedForme, rightImageView);
        });
        //Fin liste de droite
        ////////////////////////Fin liste de séléction


        // Bouton pour afficher les informations du projet
        Button infoButton2 = new Button("?");
        infoButton2.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.google.com"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        Button midButton2 = new Button("Morphisme");

        formesComboBox.getStyleClass().add("LR_Button");
        formesComboBox.getStyleClass().add("combo-box");
        formesComboBoxR.getStyleClass().add("LR_Button");
        formesComboBoxR.getStyleClass().add("combo-box");
        infoButton2.getStyleClass().add("Info_Button");
        midButton2.getStyleClass().add("Mid_Button");

        VBox leftVBox2 = new VBox(10, leftImageView, formesComboBox);
        leftVBox2.setAlignment(Pos.CENTER);

        VBox rightVBox2 = new VBox(10, rightImageView, formesComboBoxR);
        rightVBox2.setAlignment(Pos.CENTER);


        BorderPane bp2 = new BorderPane();
        bp2.getStyleClass().add("root");
        bp2.setBottom(infoButton2);
        bp2.setLeft(leftVBox2);
        bp2.setCenter(midButton2);
        bp2.setRight(rightVBox2);
        bp2.setTop(returnButton1);
        bp2.setPadding(new javafx.geometry.Insets(20));
        sceneSimple = new Scene(bp2, 1020, 500);
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

        // Bouton pour afficher les informations
        Button infoButton = new Button("?");
        infoButton.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.google.com"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        Button midButton = new Button("Morphisme");

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
        sceneArrondie = new Scene(bp1, 1020, 500); // Initialiser la variable sceneArrondie
        sceneArrondie.getStylesheets().add("app.css");
        ////////////////////////////////////FIN : Scene 2 : Morphisme de forme Arrondie


        // Configuration de la fenêtre principale
        primaryStage.setTitle("Application de Morphing");
        primaryStage.show();

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
    
    //Update l'image avec une image de la liste (Scène 1)
    private void updateImageView(String selectedForme, ImageView imageView) {
        Image image = null;
        switch (selectedForme) {
            case "Carre":
                image = new Image("carre.png");
                break;
            case "Croix":
                image = new Image("croix.png");
                break;
            case "Losange":
                image = new Image("losange.png");
                break;
            case "Parallelogramme":
                image = new Image("parallelogramme.png");
                break;
            case "Fleche Pentagone":
                image = new Image("fleche_pentagone.png");
                break;
            case "Trapeze":
                image = new Image("trapeze.png");
                break;
            case "Triangle":
                image = new Image("triangle.png");
                break;
            case "Triangle Rectangle":
                image = new Image("triangle_rectangle.png");
                break;
        }
        System.out.println("Image modifiee !");
        Image scaledImage = scaleImage(image, 300, 300); // Redimensionner l'image
        imageView.setImage(scaledImage);
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

    public static void main(String[] args) {
        launch(args);
    }
}
