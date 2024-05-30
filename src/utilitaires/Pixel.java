package utilitaires;

/**
 * Classe Pixel
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class Pixel {
    private int r; 
    private int v; 
    private int b; 

    public Pixel (int r, int v, int b){
        this.r = r; 
        this.v = v; 
        this.b = b; 
    }

    public int getR() {
        return r;
    }
    public void setR(int r) {
        this.r = r;
    }

    public int getV() {
        return v;
    }
    public void setV(int v) {
        this.v = v;
    }

    public int getB() {
        return b;
    }
    public void setB(int b) {
        this.b = b;
    }

    
}
