package utilitaires;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageM {
	
	private Pixel[][] tab; 
    
    public ImageM(String imagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (image != null) {
            int height = image.getHeight();
            int width = image.getWidth();
            tab = new Pixel[height][width];

            // Parcours de l'image pour récupérer les pixels
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = image.getRGB(x, y);
                    int red = (pixel >> 16) & 0xFF;
                    int green = (pixel >> 8) & 0xFF;
                    int blue = pixel & 0xFF;
                    tab[y][x] = new Pixel(red, green, blue);
                }
            }
        } else {
            System.out.println("Erreur lors du chargement de l'image.");
        }
    }

    public ImageM(Pixel[][] pixels) {
        this.tab = pixels;
    }

    public int getHauteur(){
        return tab.length; 
    }

    public int getLargeur(){
        return tab[0].length; 
    }

    public Pixel[][] getTab(){
        return tab; 
    }
    


    public void saveImage(String outputPath) {
        int width = tab[0].length;
        int height = tab.length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Pixel pixel = tab[x][y];
                int rgb = (pixel.getR() << 16) | (pixel.getV() << 8) | pixel.getB();
                image.setRGB(x, y, rgb);
            }
        }

        File output = new File(outputPath);
        try {
            ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
/* 
    public static void main(String[] args) {
        // Chemin vers l'image à charger
        String imagePath = "/home/cytech/Desktop/FormesSimples/croix.jpg";

        // Création de l'objet ImageM en chargeant l'image
        ImageM image = new ImageM(imagePath);

        // Affichage de la hauteur et de la largeur de l'image
        System.out.println("Hauteur de l'image : " + image.getHauteur());
        System.out.println("Largeur de l'image : " + image.getLargeur());

        // Accès aux pixels de l'image
        Pixel[][] pixelArray = image.tab; // Accès direct au tableau de pixels

        // Exemple d'utilisation : affichage des valeurs des pixels de l'image
        for (int y = 0; y < image.getHauteur(); y++) {
            for (int x = 0; x < image.getLargeur(); x++) {
                Pixel pixel = pixelArray[y][x];
                System.out.println("Pixel : (" + x + "," + y + "): R=" + pixel.getR() + ", G=" + pixel.getV() + ", B=" + pixel.getB());
            }
        }

        // Test conversion tab -> image
        ImageM imageTest = new ImageM(pixelArray);
        imageTest.saveImage("/home/cytech/Desktop/test.jpg");
    }
*/
}
