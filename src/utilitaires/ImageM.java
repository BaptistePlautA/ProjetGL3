package utilitaires;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Classe ImageM
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class ImageM {
	
	private Pixel[][] tab; 
    
    /**
     * Constructeur de la classe ImageM  
     * @param imagePath  
     */
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

    public void setTab(Pixel[][] tab) {
        this.tab = tab;
    }

    /**
     * Procédure qui enregistre une image 
     * @param outputPath  
     */
    public void sauvegarderImage(String outputPath) {
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
}