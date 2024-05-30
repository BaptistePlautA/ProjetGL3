package controleurs;

import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.FileImageOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

/**
 * Classe ConvertisseurGIF
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class ConvertisseurGIF {

    /**
     * Fonction qui récupère les images intermédiaires du morphing dans le dossier et appelle la méthode pour en faire un gif  
     * @param delai
     */
    public void convertirEnGif(int delai, String cheminDossier) {
        //chemin du dossier contenant les JPG du morphing
        //String cheminDossier = "././FormesSimples/";

        //nom du GIF de sortie
        String sortieGIF = "./resultatMorphing.gif";

        //liste pour stocker les images converties
        ArrayList<BufferedImage> listeImages = new ArrayList<>();

        try {
            //lire tous les fichiers dans le dossier
            File dossier = new File(cheminDossier);
            File[] listeFichiers = dossier.listFiles();

            //parcourir les fichiers et ajouter les images JPG à la liste
            if (listeFichiers != null) {
                for (File fichier : listeFichiers) {
                    if (fichier.isFile() && fichier.getName().toLowerCase().endsWith(".jpg")) {
                        BufferedImage img = ImageIO.read(fichier);
                        listeImages.add(img);
                    }
                }
            }

            //convertir la liste d'images en GIF
            creerGIF(sortieGIF, listeImages, delai);
            //ouvrir le GIF dans une nouvelle fenêtre
            afficherGIF(sortieGIF);

            System.out.println("Conversion terminee.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fonction qui crée le gif animé 
     * @param cheminGif
     * @param images
     * @param delai
     * @throws IOException
     */
    private void creerGIF(String cheminGif, ArrayList<BufferedImage> images, int delai) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersBySuffix("gif").next();
        FileImageOutputStream output = new FileImageOutputStream(new File(cheminGif));
        writer.setOutput(output);
        writer.prepareWriteSequence(null);

        for (BufferedImage image : images) {
            IIOMetadata metadata = writer.getDefaultImageMetadata(new ImageTypeSpecifier(image), null);
            configureRootMetadata(metadata, delai);
            writer.writeToSequence(new IIOImage(image, null, metadata), null);
        }

        writer.endWriteSequence();
        output.close();
    }

    /**
     * Fonction qui gère les métadata pour le gif 
     * @param metadata
     * @param delai
     * @throws IIOInvalidTreeException
     */
    private void configureRootMetadata(IIOMetadata metadata, int delai) throws IIOInvalidTreeException {
        String metaFormatName = metadata.getNativeMetadataFormatName();

        IIOMetadataNode root = new IIOMetadataNode(metaFormatName);

        IIOMetadataNode graphicsControlExtensionNode = new IIOMetadataNode("GraphicControlExtension");
        graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
        graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(delai / 10));
        graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");
        root.appendChild(graphicsControlExtensionNode);

        IIOMetadataNode applicationExtensionsNode = new IIOMetadataNode("ApplicationExtensions");
        IIOMetadataNode applicationExtension = new IIOMetadataNode("ApplicationExtension");

        applicationExtension.setAttribute("applicationID", "NETSCAPE");
        applicationExtension.setAttribute("authenticationCode", "2.0");

        byte[] loopContinuously = new byte[]{1, 0, 0};
        applicationExtension.setUserObject(loopContinuously);
        applicationExtensionsNode.appendChild(applicationExtension);
        root.appendChild(applicationExtensionsNode);

        metadata.mergeTree(metaFormatName, root);
    }

    /**
     * Fonction qui ouvre le gif automatiquement dès qu'il est terminé 
     * @param cheminGif
     */
    private void afficherGIF(String cheminGif) {
        JFrame fenetre = new JFrame("Votre morphing");
        fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fenetre.setSize(350, 350);

        ImageIcon iconeImage = new ImageIcon(cheminGif);
        JLabel label = new JLabel(iconeImage);
        fenetre.getContentPane().add(label, BorderLayout.CENTER);
            
        //centrer
        fenetre.setLocationRelativeTo(null);
        fenetre.setVisible(true);
    }
}