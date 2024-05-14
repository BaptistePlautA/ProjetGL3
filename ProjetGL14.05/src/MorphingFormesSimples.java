import java.util.List; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MorphingFormesSimples {
	private Forme formeDebut; 
	private Forme formeFin; 
    private Map<Character,Point> pointsControleDebut; 
    private Map<Character, Point> pointsControleFin;
	private int nbEtapes; 
	
	public MorphingFormesSimples(Forme formeDebut, Forme formeFin, int nbEtapes) {
        this.formeDebut = formeDebut; 
        this.formeFin = formeFin; 
        this.nbEtapes = nbEtapes;
        this.pointsControleDebut = new HashMap<>();
        this.pointsControleFin = new HashMap<>();
    }

    public MorphingFormesSimples(Forme formeDebut, Forme formeFin, Map<Character, Point> pointsControleDebut, Map<Character, Point> pointsControleFin, int nbEtapes) {
        this.formeDebut = formeDebut; 
        this.formeFin = formeFin; 
        this.nbEtapes = nbEtapes;
        this.pointsControleDebut = pointsControleDebut;
        this.pointsControleFin = pointsControleFin;
    }
	
	public List<Forme> genererFormesIntermediaires(){
		List<Forme> formesIntermediaires = new ArrayList<>(); 
		//vérifie s'il y a des points de contrôle ou pas 
        if (pointsControleDebut.isEmpty()) {
            //si c'est le cas : morphing classique
            return genererFormesIntermediairesOriginales();
        }

		//pour générer formes intermédiaires
        for(int i = 0; i <= nbEtapes; i++) {
            double t = (double) i / nbEtapes;  
            Point[] pointDebut = pointsControleDebut.values().toArray(new Point[0]);
            Point[] pointFin = pointsControleFin.values().toArray(new Point[0]);
            
            Point[] pointIntermediaire = new Point[pointDebut.length]; 
            //interpolation de chaque point 
            for(int j = 0; j < pointDebut.length; j++) {
                pointIntermediaire[j] = interpolation(pointDebut[j], pointFin[j], t); 
            }
            //ajoute la forme à la liste de res (ie de formes intermédiaires) 
            formesIntermediaires.add(new Forme(pointIntermediaire)); 
        }
		return formesIntermediaires; 
	}

    //morphing classique sans points de contrôle 
    private List<Forme> genererFormesIntermediairesOriginales() {
        List<Forme> formesIntermediaires = new ArrayList<>();
        
        for(int i = 0; i <= nbEtapes; i++) {
            double t = (double) i / nbEtapes;  
            Point[] pointDebut = formeDebut.getPoints();
            Point[] pointFin = formeFin.getPoints();
            
            Point[] pointIntermediaire = new Point[pointDebut.length]; 
            //interpolation de chaque point 
            for(int j = 0; j < pointDebut.length; j++) {
                pointIntermediaire[j] = interpolation(pointDebut[j], pointFin[j], t); 
            }
            //ajoute la forme à la liste de res (ie de formes intermédiaires) 
            formesIntermediaires.add(new Forme(pointIntermediaire)); 
        }
        return formesIntermediaires;
    }
	
	public Point interpolation(Point p1,Point p2, double t) {
		//applique formule interpolation entre les deux points avec le facteur d'interpolation t 
		int x = (int)((1-t)*p1.getX() + t*p2.getX()); 
		int y = (int)((1-t)*p1.getY() + t*p2.getY()); 
		
		return new Point(x,y); 
	}
}
