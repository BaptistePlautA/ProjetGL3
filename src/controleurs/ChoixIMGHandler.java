package controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

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
            saveImageToFile(scaledImage, "./bin/", "scaled_" + selectedFile.getName());
            imageView.setImage(new Image("scaled_"+selectedFile.getName()));
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
    
    private void saveImageToFile(Image image, String directoryPath, String fileName) {
        File outputFile = new File(directoryPath, fileName);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}