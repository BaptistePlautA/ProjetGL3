package controleurs;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import utilitaires.Point;

public class PointControleArrondisHandler implements EventHandler<ActionEvent> {
    private StackPane leftPane;
    private StackPane rightPane;
    private Point pointEnCoursDeDeplacement;

    private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private int[] alphabetIndex = {0};

    private static Map<Character, Point> pointsControleDebut = new HashMap<>();
    private static Map<Character, Point> pointsControleFin = new HashMap<>();
    private static Map<Character, Point> pointsControleArrondis = new HashMap<>();

    private Circle pointControleDebut1;
    private Circle pointControleDebut2;
    private Circle pointControleArrondis1;
    private Circle pointControleArrondis2;
    private Line line;

    private Circle pointControleDebut1Right;
    private Circle pointControleDebut2Right;
    private Circle pointControleArrondis1Right;
    private Circle pointControleArrondis2Right;
    private Line lineRight;

    public PointControleArrondisHandler(StackPane leftPane, StackPane rightPane) {
        this.leftPane = leftPane;
        this.rightPane = rightPane;
    }

    @Override
    public void handle(ActionEvent event) {}

    public void handleLeftPaneClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        if (pointsControleDebut.size() < 2) {
            char label = alphabet.charAt(alphabetIndex[0]);
            alphabetIndex[0]++;

            Point pointDebut = new Point(x, y);
            pointsControleDebut.put(label, pointDebut);

            Circle pointControleDebut = new Circle(4, Color.RED);
            pointControleDebut.setTranslateX(x - (leftPane.getWidth() / 2));
            pointControleDebut.setTranslateY(y - (leftPane.getHeight() / 2));
            leftPane.getChildren().add(pointControleDebut);

            if (pointsControleDebut.size() == 1) {
                pointControleDebut1 = pointControleDebut;
            } else {
                pointControleDebut2 = pointControleDebut;
                afficherPointsBleusEtLigne(label);
            }

            afficherPointsPaneRight();
        }
    }

    private void afficherPointsPaneRight() {
        if (pointsControleDebut.size() == 1) {
            Point point = pointsControleDebut.get(alphabet.charAt(alphabetIndex[0] - 1));
            Circle pointControleDebutRight = new Circle(4, Color.RED);
            pointControleDebutRight.setTranslateX(point.getX() - (rightPane.getWidth() / 2));
            pointControleDebutRight.setTranslateY(point.getY() - (rightPane.getHeight() / 2));
            rightPane.getChildren().add(pointControleDebutRight);

            if (pointsControleDebut.size() == 1) {
                pointControleDebut1Right = pointControleDebutRight;
            }
        } else if (pointsControleDebut.size() == 2) {
            Point point1 = pointsControleDebut.get(alphabet.charAt(alphabetIndex[0] - 2));
            Point point2 = pointsControleDebut.get(alphabet.charAt(alphabetIndex[0] - 1));

            pointControleDebut1Right.setTranslateX(point1.getX() - (rightPane.getWidth() / 2));
            pointControleDebut1Right.setTranslateY(point1.getY() - (rightPane.getHeight() / 2));

            Circle pointControleDebutRight = new Circle(4, Color.RED);
            pointControleDebutRight.setTranslateX(point2.getX() - (rightPane.getWidth() / 2));
            pointControleDebutRight.setTranslateY(point2.getY() - (rightPane.getHeight() / 2));
            rightPane.getChildren().add(pointControleDebutRight);
            pointControleDebut2Right = pointControleDebutRight;

            // Calculate equidistant points for blue points
            double midX = (point1.getX() + point2.getX()) / 2;
            double midY = (point1.getY() + point2.getY()) / 2;
            double dx = (point2.getX() - point1.getX()) / 2;
            double dy = (point2.getY() - point1.getY()) / 2;
            double blueX1 = midX + dy;
            double blueY1 = midY - dx;
            double blueX2 = midX - dy;
            double blueY2 = midY + dx;

            Point pointArrondis1 = new Point(blueX1, blueY1);
            Point pointArrondis2 = new Point(blueX2, blueY2);
            pointsControleArrondis.put(alphabet.charAt(alphabetIndex[0] - 2), pointArrondis1);
            pointsControleArrondis.put(alphabet.charAt(alphabetIndex[0] - 1), pointArrondis2);

            Circle pointControleArrondis1Right = new Circle(4, Color.BLUE);
            Circle pointControleArrondis2Right = new Circle(4, Color.BLUE);
            pointControleArrondis1Right.setTranslateX(blueX1 - (rightPane.getWidth() / 2));
            pointControleArrondis1Right.setTranslateY(blueY1 - (rightPane.getHeight() / 2));
            pointControleArrondis2Right.setTranslateX(blueX2 - (rightPane.getWidth() / 2));
            pointControleArrondis2Right.setTranslateY(blueY2 - (rightPane.getHeight() / 2));

            Line lineRight = new Line(blueX1 - (rightPane.getWidth() / 2), blueY1 - (rightPane.getHeight() / 2),
                    blueX2 - (rightPane.getWidth() / 2), blueY2 - (rightPane.getHeight() / 2));
            lineRight.setStroke(Color.BLACK);

            rightPane.getChildren().addAll(pointControleArrondis1Right, pointControleArrondis2Right, lineRight);
            this.pointControleArrondis1Right = pointControleArrondis1Right;
            this.pointControleArrondis2Right = pointControleArrondis2Right;
            this.lineRight = lineRight;
        }
    }

    private void afficherPointsBleusEtLigne(char label) {
        if (pointsControleDebut.size() != 2) return;

        Point point1 = pointsControleDebut.get(alphabet.charAt(alphabetIndex[0] - 2));
        Point point2 = pointsControleDebut.get(alphabet.charAt(alphabetIndex[0] - 1));

        // Calculate equidistant points for blue points
        double midX = (point1.getX() + point2.getX()) / 2;
        double midY = (point1.getY() + point2.getY()) / 2;
        double dx = (point2.getX() - point1.getX()) / 2;
        double dy = (point2.getY() - point1.getY()) / 2;
        double blueX1 = midX + dy;
        double blueY1 = midY - dx;
        double blueX2 = midX - dy;
        double blueY2 = midY + dx;

        Point pointArrondis1 = new Point(blueX1, blueY1);
        Point pointArrondis2 = new Point(blueX2, blueY2);
        pointsControleArrondis.put(alphabet.charAt(alphabetIndex[0] - 2), pointArrondis1);
        pointsControleArrondis.put(alphabet.charAt(alphabetIndex[0] - 1), pointArrondis2);

        pointControleArrondis1 = new Circle(4, Color.BLUE);
        pointControleArrondis2 = new Circle(4, Color.BLUE);
        pointControleArrondis1.setTranslateX(blueX1 - (leftPane.getWidth() / 2));
        pointControleArrondis1.setTranslateY(blueY1 - (leftPane.getHeight() / 2));
        pointControleArrondis2.setTranslateX(blueX2 - (leftPane.getWidth() / 2));
        pointControleArrondis2.setTranslateY(blueY2 - (leftPane.getHeight() / 2));

        line = new Line(blueX1 - (leftPane.getWidth() / 2), blueY1 - (leftPane.getHeight() / 2),
                blueX2 - (leftPane.getWidth() / 2), blueY2 - (leftPane.getHeight() / 2));
        line.setStroke(Color.BLACK);

        leftPane.getChildren().addAll(pointControleArrondis1, pointControleArrondis2, line);

        PointControleArrondisHandler handler = this;

        pointControleArrondis1.setOnMousePressed(event -> handler.handlePanePress(event, pointArrondis1));
        pointControleArrondis1.setOnMouseDragged(event -> handler.handlePaneDrag(event, pointArrondis1, pointControleArrondis1));
        pointControleArrondis1.setOnMouseReleased(event -> handler.handleMouseRelease(event));

        pointControleArrondis2.setOnMousePressed(event -> handler.handlePanePress(event, pointArrondis2));
        pointControleArrondis2.setOnMouseDragged(event -> handler.handlePaneDrag(event, pointArrondis2, pointControleArrondis2));
        pointControleArrondis2.setOnMouseReleased(event -> handler.handleMouseRelease(event));
    }

    public void handlePanePress(MouseEvent event, Point point) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        pointEnCoursDeDeplacement = new Point(mouseX, mouseY);
    }

    public void handlePaneDrag(MouseEvent event, Point point, Circle cerclePointControle) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        double deltaX = mouseX - pointEnCoursDeDeplacement.getX();
        double deltaY = mouseY - pointEnCoursDeDeplacement.getY();

        double coordonneeX = cerclePointControle.getTranslateX() + deltaX;
        double coordonneeY = cerclePointControle.getTranslateY() + deltaY;

        // Check if point is within the pane bounds
        if (coordonneeX < -leftPane.getWidth() / 2) {
            cerclePointControle.setTranslateX(-leftPane.getWidth() / 2);
        } else if (coordonneeX > leftPane.getWidth() / 2) {
            cerclePointControle.setTranslateX(leftPane.getWidth() / 2);
        } else {
            cerclePointControle.setTranslateX(coordonneeX);
        }

        if (coordonneeY < -leftPane.getHeight() / 2) {
            cerclePointControle.setTranslateY(-leftPane.getHeight() / 2);
        } else if (coordonneeY > leftPane.getHeight() / 2) {
            cerclePointControle.setTranslateY(leftPane.getHeight() / 2);
        } else {
            cerclePointControle.setTranslateY(coordonneeY);
        }

        point.setX(point.getX() + deltaX);
        point.setY(point.getY() + deltaY);

        updateLine();
    }

    private void updateLine() {
        if (line != null) {
            line.setStartX(pointControleArrondis1.getTranslateX());
            line.setStartY(pointControleArrondis1.getTranslateY());
            line.setEndX(pointControleArrondis2.getTranslateX());
            line.setEndY(pointControleArrondis2.getTranslateY());
        }
    }

    public void handleMouseRelease(MouseEvent event) {
        pointEnCoursDeDeplacement = null;
    }

    public void handleReset(ActionEvent event) {
        pointsControleDebut.clear();
        pointsControleFin.clear();
        pointsControleArrondis.clear();
        leftPane.getChildren().clear();
        rightPane.getChildren().clear();
        alphabetIndex[0] = 0;
    }
}
