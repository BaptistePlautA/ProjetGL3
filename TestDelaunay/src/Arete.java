public class Arete {
    Point p0;
    Point p1;

    public Arete(Point p0, Point p1) {
        this.p0 = p0;
        this.p1 = p1;
    }
    
    //Note à moi même: bien écrire le equals
    //sous peine d'avoir des résultats complètement FAUX :))
    boolean equals(Arete e) {
        return (this.p0.equals(e.p0) && this.p1.equals(e.p1)) || (this.p0.equals(e.p1) && this.p1.equals(e.p0));
    }
}