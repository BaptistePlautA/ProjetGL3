package controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * Classe ChoixIMGHandler
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class ChoixIMGHandler implements EventHandler<ActionEvent> {
    private ImageView imageView;

    public ChoixIMGHandler(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void handle(ActionEvent event) {
        FileChooser selecteurFichier = new FileChooser();
        selecteurFichier.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File fichierSelectionne = selecteurFichier.showOpenDialog(null);
        if (fichierSelectionne != null) {
            Image image = new Image(fichierSelectionne.toURI().toString());
            Image imageRedimensionnee = redimensionnerImage(image, 300, 300);
            sauvegarderImageEnFichier(imageRedimensionnee, "./bin/", "scaled_" + fichierSelectionne.getName());
            imageView.setImage(new Image("scaled_"+fichierSelectionne.getName()));
        }
    }
 
    /**
     * Fonction pour scale automatiquement l'image 
     * @param image
     * @param largeur
     * @param hauteur
     * @return retourne l'image 
     */
    public Image redimensionnerImage(Image image, double largeur, double hauteur) {
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(false);
        imageView.setFitWidth(largeur);
        imageView.setFitHeight(hauteur);
        return imageView.snapshot(null, null);
    }

    /**
     * Fonction qui enregistre l'image en fichier dans le bin
     * @param image
     * @param cheminDossier
     * @param nomFichier
     */
    private void sauvegarderImageEnFichier(Image image, String cheminDossier, String nomFichier) {
        File fichierSortie = new File(cheminDossier, nomFichier);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", fichierSortie);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}