package components.edge;

import components.vertex.Vertex;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

public abstract class Edge {

    private String LINE_FREE_STYLE = "-fx-fill:white;-fx-stroke-width:2px;-fx-stroke:black;";
    private String LINE_VISITING_STYLE = "-fx-fill:green;-fx-stroke-width:2px;-fx-stroke:black;";
    private String LINE_SELECTED_STYLE = "-fx-fill:orange;-fx-stroke-width:2px;-fx-stroke:black;";
    private String LINE_FINISHED_STYLE = "-fx-fill:purple;-fx-stroke-width:2px;-fx-stroke:black;";

    private Text weightText;
    private Pane pane;
    private Line line;
    private StackPane weightPane;

    private Vertex from;
    private Vertex to;
    private int weight;
    private boolean isWeighted;
    private boolean isDirected;

    public Edge(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
        this.weight = 0;
        this.isWeighted = false;
        makeShape();
    }
    public Edge(Vertex from, Vertex to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.isWeighted = true;
        makeShape();
    }

    private void makeShape() {
        pane = new Pane();
        line = createDirectedLine(from, to, pane);
    }

    private Line createDirectedLine(StackPane startDot, StackPane endDot, Pane parent) {
        Line virtualCenterLine = new Line();
        virtualCenterLine.setOpacity(0);
        virtualCenterLine.startXProperty().bind(startDot.layoutXProperty().add(startDot.translateXProperty()).add(startDot.widthProperty().divide(2)));
        virtualCenterLine.startYProperty().bind(startDot.layoutYProperty().add(startDot.translateYProperty()).add(startDot.heightProperty().divide(2)));
        virtualCenterLine.endXProperty().bind(endDot.layoutXProperty().add(endDot.translateXProperty()).add(endDot.widthProperty().divide(2)));
        virtualCenterLine.endYProperty().bind(endDot.layoutYProperty().add(endDot.translateYProperty()).add(endDot.heightProperty().divide(2)));

        Line directedLine = new Line();
        directedLine.setStyle(LINE_FREE_STYLE);
        directedLine.startXProperty().bind(virtualCenterLine.startXProperty());
        directedLine.startYProperty().bind(virtualCenterLine.startYProperty());
        directedLine.endXProperty().bind(virtualCenterLine.endXProperty());
        directedLine.endYProperty().bind(virtualCenterLine.endYProperty());


//        final ChangeListener<Number> listener = (obs, old, newVal) -> {
//            Rotate r = new Rotate();
//            r.setPivotX(virtualCenterLine.getStartX());
//            r.setPivotY(virtualCenterLine.getStartY());
//            Point2D point = r.transform(new Point2D(virtualCenterLine.getStartX(), virtualCenterLine.getStartY()));
//            directedLine.setStartX(point.getX());
//            directedLine.setStartY(point.getY());
//
//            Rotate r2 = new Rotate();
//            r2.setPivotX(virtualCenterLine.getEndX());
//            r2.setPivotY(virtualCenterLine.getEndY());
//            Point2D point2 = r2.transform(new Point2D(virtualCenterLine.getEndX(), virtualCenterLine.getEndY()));
//            directedLine.setEndX(point2.getX());
//            directedLine.setEndY(point2.getY());
//        };

//        virtualCenterLine.startXProperty().addListener(listener);
//        virtualCenterLine.startYProperty().addListener(listener);
//        virtualCenterLine.endXProperty().addListener(listener);
//        virtualCenterLine.endYProperty().addListener(listener);

        StackPane mainArrow = createArrow(true, directedLine, startDot, endDot);
        createWeightPane(directedLine);
        parent.getChildren().addAll(  directedLine, mainArrow, weightPane);
        return directedLine;
    }

    private StackPane createArrow(boolean toLineEnd, Line line, StackPane startDot, StackPane endDot) {

        double size = 15; // Arrow size
        StackPane arrow = new StackPane();
        arrow.setStyle("-fx-background-color:#333333;-fx-border-width:1px;-fx-border-color:black;-fx-shape: \"M0,-4L4,0L0,4Z\"");//
        arrow.setPrefSize(size, size);
        arrow.setMaxSize(size, size);
        arrow.setMinSize(size, size);

        // Determining the arrow visibility unless there is enough space between dots.
        DoubleBinding xDiff = line.endXProperty().subtract(line.startXProperty());
        DoubleBinding yDiff = line.endYProperty().subtract(line.startYProperty());
        BooleanBinding visible = (xDiff.lessThanOrEqualTo(size).and(xDiff.greaterThanOrEqualTo(-size)).and(yDiff.greaterThanOrEqualTo(-size)).and(yDiff.lessThanOrEqualTo(size))).not();
        arrow.visibleProperty().bind(visible);

        // Determining the x point on the line which is at a certain distance.
        DoubleBinding tX = Bindings.createDoubleBinding(() -> {
            double xDiffSqu = (line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX());
            double yDiffSqu = (line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY());
            double lineLength = Math.sqrt(xDiffSqu + yDiffSqu);
            double dt;
            if (toLineEnd) {
                // When determining the point towards end, the required distance is total length minus (radius + arrow half width)
                dt = lineLength - (endDot.getWidth() / 2) - (arrow.getWidth() / 2);
            } else {
                // When determining the point towards start, the required distance is just (radius + arrow half width)
                dt = (startDot.getWidth() / 2) + (arrow.getWidth() / 2);
            }

            double t = dt / lineLength;
            double dx = ((1 - t) * line.getStartX()) + (t * line.getEndX());
            return dx;
        }, line.startXProperty(), line.endXProperty(), line.startYProperty(), line.endYProperty());

        // Determining the y point on the line which is at a certain distance.
        DoubleBinding tY = Bindings.createDoubleBinding(() -> {
            double xDiffSqu = (line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX());
            double yDiffSqu = (line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY());
            double lineLength = Math.sqrt(xDiffSqu + yDiffSqu);
            double dt;
            if (toLineEnd) {
                dt = lineLength - (endDot.getHeight() / 2) - (arrow.getHeight() / 2);
            } else {
                dt = (startDot.getHeight() / 2) + (arrow.getHeight() / 2);
            }
            double t = dt / lineLength;
            double dy = ((1 - t) * line.getStartY()) + (t * line.getEndY());
            return dy;
//                        System.out.println("Exist, " + firstVertex.getName() + " -> " + v.getName());
        }, line.startXProperty(), line.endXProperty(), line.startYProperty(), line.endYProperty());

        arrow.layoutXProperty().bind(tX.subtract(arrow.widthProperty().divide(2)));
        arrow.layoutYProperty().bind(tY.subtract(arrow.heightProperty().divide(2)));

        DoubleBinding endArrowAngle = Bindings.createDoubleBinding(() -> {
            double stX = toLineEnd ? line.getStartX() : line.getEndX();
            double stY = toLineEnd ? line.getStartY() : line.getEndY();
            double enX = toLineEnd ? line.getEndX() : line.getStartX();
            double enY = toLineEnd ? line.getEndY() : line.getStartY();
            double angle = Math.toDegrees(Math.atan2(enY - stY, enX - stX));
            if (angle < 0) {
                angle += 360;
            }
            return angle;
        }, line.startXProperty(), line.endXProperty(), line.startYProperty(), line.endYProperty());
        arrow.rotateProperty().bind(endArrowAngle);

        return arrow;
    }

    private void createWeightPane(Line line) {
        weightPane = new StackPane();
        double size = 20;
        weightPane.setStyle("-fx-background-color:grey;-fx-border-width:1px;-fx-border-color:black;");
        weightPane.setPrefSize(size, size);
        weightPane.setMaxSize(size, size);
        weightPane.setMinSize(size, size);

        weightText = new Text(String.valueOf(weight));
        weightPane.getChildren().add(weightText);

        DoubleBinding wgtSqrHalfWidth = weightPane.widthProperty().divide(2);
        DoubleBinding wgtSqrHalfHeight = weightPane.heightProperty().divide(2);
        DoubleBinding lineXHalfLength = line.endXProperty().subtract(line.startXProperty()).divide(2);
        DoubleBinding lineYHalfLength = line.endYProperty().subtract(line.startYProperty()).divide(2);

        weightPane.layoutXProperty().bind(line.startXProperty().add(lineXHalfLength.subtract(wgtSqrHalfWidth)));
        weightPane.layoutYProperty().bind(line.startYProperty().add(lineYHalfLength.subtract(wgtSqrHalfHeight)));
    }

    public Pane getEdgePane() {
        return this.pane;
    }

    public void setWeight(int weight) {
        this.weight = weight;
        this.isWeighted = true;
    }

    public void setWeighted(boolean weighted) {
        isWeighted = weighted;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }

    public void setDirected(boolean isDirected) {
        this.isDirected = isDirected;
    }

    public boolean isDirected() {
        return isDirected;
    }
}
