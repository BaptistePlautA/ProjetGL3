package testAuto;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GIFConverter {

    public static void main(String[] args) {
        // Chemin du dossier contenant les images JPG
        String folderPath = "/home/cytech/Desktop/FormesSimples/";

        // Nom du fichier GIF de sortie
        String outputGIF = "/home/cytech/Desktop/sortie.gif";

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
            createAnimatedGif(outputGIF, imageList, 100); // Durée d'affichage de chaque image : 100 millisecondes

            System.out.println("Conversion terminée.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    private static void createAnimatedGif(String gifPath, ArrayList<BufferedImage> images, int delay) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersBySuffix("gif").next();
        FileImageOutputStream output = new FileImageOutputStream(new File(gifPath));
        writer.setOutput(output);
        writer.prepareWriteSequence(null);
        
     // Utiliser les métadonnées par défaut du format GIF
        IIOMetadata metadata = writer.getDefaultImageMetadata(new javax.imageio.ImageTypeSpecifier(images.get(0)), null);
    // Créer le nœud racine avec le bon type
        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_gif_image_1.0");

        // Ajouter le bloc d'extension d'application pour spécifier une boucle infinie
        IIOMetadataNode appExtensionsNode = new IIOMetadataNode("ApplicationExtensions");
        IIOMetadataNode appExtensionNode = new IIOMetadataNode("ApplicationExtension");
        appExtensionNode.setAttribute("applicationID", "NETSCAPE");
        appExtensionNode.setAttribute("authenticationCode", "2.0");
        byte[] loop = new byte[]{0x1, 0x0}; // Infinite loop
        appExtensionNode.setUserObject(loop);
        appExtensionsNode.appendChild(appExtensionNode);
        
        // Ajouter le bloc d'extension d'application au nœud racine
        root.appendChild(appExtensionsNode);

        // Fusionner le nœud racine avec les métadonnées
        metadata.mergeTree("javax_imageio_gif_image_1.0", root);
     

        for (BufferedImage image : images) {
            writer.writeToSequence(new javax.imageio.IIOImage(image, null, metadata), null);
        }


        writer.endWriteSequence();
        output.close();
        writer.dispose();
    }
    
 
}


