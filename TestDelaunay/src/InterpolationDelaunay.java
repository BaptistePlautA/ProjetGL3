import java.util.List;
import java.util.ArrayList;
public class InterpolationDelaunay {

    public List<List<Point>> interpolerPoints(List<Point> pointsDepart, List<Point> pointsFin, int nbEtapes) {
    	//On créé une liste de liste de points interpolés pour pouvoir avoir chaque étape
    	//de notre interpolation (selon notre ratio) la triangulation de Delaunay de cette étape
        List<List<Point>> pointsInterpoles = new ArrayList<>();
        for (int i = 0; i <= nbEtapes; i++) {
            double ratio = (double) i / nbEtapes;
            List<Point> pointsEtape = new ArrayList<>();
            for (int j = 0; j < pointsDepart.size(); j++) {
                double x = interpoler(pointsDepart.get(j).getX(), pointsFin.get(j).getX(), ratio);
                double y = interpoler(pointsDepart.get(j).getY(), pointsFin.get(j).getY(), ratio);
                pointsEtape.add(new Point(x, y));
            }
            pointsInterpoles.add(pointsEtape);
        }
        return pointsInterpoles;
    }

    private double interpoler(double debut, double fin, double ratio) {
        return debut + (fin - debut) * ratio;
    }
}