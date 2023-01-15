package components.edge;

import components.status.Status;
import components.vertex.Vertex;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public abstract class Edge extends Pane {

    private String LINE_FREE_STYLE = "-fx-fill:black;-fx-stroke-width:3px;-fx-stroke:black;";
    private String LINE_VISITING_STYLE = "-fx-fill:green;-fx-stroke-width:3px;-fx-stroke:green;";
    private String LINE_SELECTED_STYLE = "-fx-fill:orange;-fx-stroke-width:3px;-fx-stroke:orange;";
    private String LINE_FINISHED_STYLE = "-fx-fill:purple;-fx-stroke-width:3px;-fx-stroke:#4d4949;";

    private Line line;
    private Text weightText;
    private StackPane weightPane;

    private Vertex from;
    private Vertex to;
    private int weight;
    private boolean isWeighted;
    private boolean isDirected;

    public Edge(Vertex from, Vertex to, boolean isDirected) {
        this.from = from;
        this.to = to;
        this.weight = 0;
        this.isWeighted = false;
        this.isDirected = isDirected;
        makeShape();
    }
    public Edge(Vertex from, Vertex to, boolean isDirected, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.isWeighted = true;
        this.isDirected = isDirected;
        makeShape();
    }

    private void makeShape() {
        if (isDirected) {
            line = createDirectedLine(from, to);
        } else {
            line = createLine(from, to);
        }

        if (isWeighted) {
            createWeightPane(line);
            this.getChildren().add(weightPane);
        }
    }

    private Line createDirectedLine(StackPane startDot, StackPane endDot) {
        Line line = createLine(startDot, endDot);

        StackPane arrow = createArrow(true, line, startDot, endDot);
        this.getChildren().add(arrow);
        return line;
    }

    private Line createLine(StackPane startDot, StackPane endDot) {
        Line line = new Line();
        line.setStyle(LINE_FREE_STYLE);
        line.startXProperty().bind(startDot.layoutXProperty().add(startDot.translateXProperty()).add(startDot.widthProperty().divide(2)));
        line.startYProperty().bind(startDot.layoutYProperty().add(startDot.translateYProperty()).add(startDot.heightProperty().divide(2)));
        line.endXProperty().bind(endDot.layoutXProperty().add(endDot.translateXProperty()).add(endDot.widthProperty().divide(2)));
        line.endYProperty().bind(endDot.layoutYProperty().add(endDot.translateYProperty()).add(endDot.heightProperty().divide(2)));
        this.getChildren().add(line);

        return line;
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

    public void setEdgeStyle(Status status) {
        switch (status) {
            case VISITING -> line.setStyle(LINE_VISITING_STYLE);
            case SELECTED -> line.setStyle(LINE_SELECTED_STYLE);
            case FREE -> this.setStyle(LINE_FREE_STYLE);
            case FINISHED -> line.setStyle(LINE_FINISHED_STYLE);
        }
        System.out.println("Edge " + from.getName() + " -> " + to.getName() + " is " + status);
    }

    public void setWeight(int weight) {
        this.weight = weight;
        this.isWeighted = true;

        if (weightPane != null) {
            weightText.setText(String.valueOf(weight));
        } else {
            createWeightPane(line);
            this.getChildren().add(weightPane);
        }
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

    public int getWeight() {
        return weight;
    }

    public StackPane getWeightPane() {
        return weightPane;
    }

    public void setDirected(boolean isDirected) {
        this.isDirected = isDirected;
    }

    public boolean isDirected() {
        return isDirected;
    }
}
