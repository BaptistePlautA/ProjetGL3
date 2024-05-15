package handlers;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ConvertisseurGIF {

    public void convertirEnGif(int delai) {
        // Chemin du dossier contenant les JPG du morphing
        String folderPath = "././FormesSimples/";

        // Nom du GIF de sortie
        String outputGIF = "./resultatMorphing.gif";

        // Liste pour stocker les images converties
        ArrayList<BufferedImage> imageList = new ArrayList<>();

        try {
            // Lire tous les fichiers dans le dossier
            File folder = new File(folderPath);
            File[] listOfFiles = folder.listFiles();

            // Parcourir les fichiers et ajouter les images JPG à la liste
            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".jpg")) {
                        BufferedImage img = ImageIO.read(file);
                        imageList.add(img);
                    }
                }
            }

            // Convertir la liste d'images en GIF
            createAnimatedGif(outputGIF, imageList, delai);

            // Ouvrir le GIF dans une nouvelle fenêtre
            afficherGif(outputGIF);

            System.out.println("Conversion terminee.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createAnimatedGif(String gifPath, ArrayList<BufferedImage> images, int delay) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersBySuffix("gif").next();
        FileImageOutputStream output = new FileImageOutputStream(new File(gifPath));
        writer.setOutput(output);
        writer.prepareWriteSequence(null);

        for (BufferedImage image : images) {
            IIOMetadata metadata = writer.getDefaultImageMetadata(new ImageTypeSpecifier(image), null);
            configureRootMetadata(metadata, delay);
            writer.writeToSequence(new IIOImage(image, null, metadata), null);
        }

        writer.endWriteSequence();
        output.close();
    }

    private void configureRootMetadata(IIOMetadata metadata, int delay) throws IIOInvalidTreeException {
        String metaFormatName = metadata.getNativeMetadataFormatName();

        IIOMetadataNode root = new IIOMetadataNode(metaFormatName);

        IIOMetadataNode graphicsControlExtensionNode = new IIOMetadataNode("GraphicControlExtension");
        graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
        graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(delay / 10));
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

    private void afficherGif(String gifPath) {
        JFrame frame = new JFrame("Votre morphing");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 300);

        ImageIcon imageIcon = new ImageIcon(gifPath);
        JLabel label = new JLabel(imageIcon);
        frame.getContentPane().add(label, BorderLayout.CENTER);
        
        //centrer
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
