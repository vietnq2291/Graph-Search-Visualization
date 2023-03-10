package components.vertex;

import components.edge.Edge;
import components.status.Status;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Vertex extends StackPane {

    private String CIRCLE_FREE_STYLE = "-fx-fill:white;-fx-stroke-width:2px;-fx-stroke:black;";
    private String CIRCLE_VISITING_STYLE = "-fx-fill:green;-fx-stroke-width:2px;-fx-stroke:black;";
    private String CIRCLE_SELECTED_STYLE = "-fx-fill:orange;-fx-stroke-width:2px;-fx-stroke:black;";
    private String CIRCLE_FINISHED_STYLE = "-fx-fill:purple;-fx-stroke-width:2px;-fx-stroke:black;";

    private String name;
    private static int radius = 20;
    private Circle circle = new Circle();
    private Label label = new Label();
    private ArrayList<Edge> edges;

    public Vertex(double x, double y) {
        this.edges = new ArrayList<>();
        this.setLayoutX(x);
        this.setLayoutY(y);
        makeShape();
    }
    public Vertex(String name, double x, double y) {
        this.name = name;
        this.edges = new ArrayList<>();
        this.setLayoutX(x);
        this.setLayoutY(y);
        makeShape();
    }

    private void makeShape() {
        double paneSize = 2 * radius;

        // Set display of Vertex as Circle with name as Label
        circle = new Circle();
        circle.setRadius(radius);
        setVertexStyle(Status.FREE);
        label.setText(String.valueOf(this.name));
        label.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        this.getChildren().addAll(circle, label);

        this.setPrefSize(paneSize, paneSize);
        this.setMaxSize(paneSize, paneSize);
        this.setMinSize(paneSize, paneSize);
    }

    public void setVertexStyle(Status status) {
        switch (status) {
            case VISITING -> circle.setStyle(CIRCLE_VISITING_STYLE);
            case SELECTED -> circle.setStyle(CIRCLE_SELECTED_STYLE);
            case FREE -> circle.setStyle(CIRCLE_FREE_STYLE);
            case FINISHED -> circle.setStyle(CIRCLE_FINISHED_STYLE);
        }
    }


    public void addConnectedEdge(Edge e) {
        if (this.edges.contains(e))
            return;
        this.edges.add(e);
    }

    public void removeConnectedEdge(Edge e) {
        if (!this.edges.contains(e))
            return;
        this.edges.remove(e);
    }

    public void setName(String name) {
        this.name = name;
        label.setText(name);
    }

    public String getName() {
        return name;
    }

    public static int getRadius() {
        return radius;
    }

    public ArrayList<Edge> getConnectedEdges() {
        return edges;
    }

}
