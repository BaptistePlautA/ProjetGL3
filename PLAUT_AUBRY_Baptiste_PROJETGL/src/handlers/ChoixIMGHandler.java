package handlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import java.io.File;

public class ChoixIMGHandler implements EventHandler<ActionEvent> {
    private ImageView imageView;

    public ChoixIMGHandler(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void handle(ActionEvent event) {
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
    
    //Scale automatiquement l'image
    private Image scaleImage(Image image, double largeur, double hauteur) {
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(largeur);
        imageView.setFitHeight(hauteur);
        return imageView.snapshot(null, null);
    }
}