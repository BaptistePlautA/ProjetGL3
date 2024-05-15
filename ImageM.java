public class ImageM {
    private Pixel[][] tab; 

    public ImageM(Pixel[][] tab, int hauteur, int largeur){
        this.tab = new Pixel[hauteur][largeur]; 
    }

    public int getHauteur(){
        return tab.length; 
    }

    public int getLargeur(){
        return tab[0].length; 
    }
}
