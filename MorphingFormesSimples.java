import java.util.List; 
import java.util.ArrayList; 

public class MorphingFormesSimples {
	private Forme formeDebut; 
	private Forme formeFin; 
	private int nbEtapes; 
	
	public MorphingFormesSimples(Forme formeDebut, Forme formeFin, int nbEtapes){
		this.formeDebut = formeDebut; 
		this.formeFin = formeFin; 
		this.nbEtapes = nbEtapes; 
	}
	
	public List<Forme> genererFormesIntermediaires(){
		List<Forme> formesIntermediaires = new ArrayList<>(); //pour stocker les formes intermédiaires
		
		//pour les générer 
		for(int i=0; i<=nbEtapes; i++) {
			double t = (double) i / nbEtapes;  
			Point[] pointDebut = formeDebut.getPoints(); 
			Point[] pointFin = formeFin.getPoints(); 
			
			Point[] pointIntermediaire = new Point[pointDebut.length]; 
			//interpolation de chaque point 
			for(int j=0; j<pointDebut.length;j++) {
				pointIntermediaire[j] = interpolation(pointDebut[j], pointFin[j],t); 
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
