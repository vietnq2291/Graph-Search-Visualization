package components.vertex;

import components.edge.Edge;
import components.status.Status;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

public class Vertex extends StackPane {

    private String CIRCLE_FREE_STYLE = "-fx-fill:white;-fx-stroke-width:2px;-fx-stroke:black;";
    private String CIRCLE_VISITING_STYLE = "-fx-fill:green;-fx-stroke-width:2px;-fx-stroke:black;";
    private String CIRCLE_SELECTED_STYLE = "-fx-fill:orange;-fx-stroke-width:2px;-fx-stroke:black;";
    private String CIRCLE_FINISHED_STYLE = "-fx-fill:purple;-fx-stroke-width:2px;-fx-stroke:black;";

    private int name;
    private double x, y;
    private static int radius = 20;
    private Circle circle = new Circle();
    private Label label = new Label();
    private ArrayList<Edge> edges;


//    public Vertex(int name) {
//        this.name = name;
//        this.edges = new ArrayList<>();
//    }
    public Vertex(double x, double y) {
        this.edges = new ArrayList<>();
        this.setLayoutX(x);
        this.setLayoutY(y);
        makeShape();
        makeHandler();
    }
    public Vertex(int name, double x, double y) {
        this.name = name;
        this.edges = new ArrayList<>();
        this.setLayoutX(x);
        this.setLayoutY(y);
        makeShape();
        makeHandler();
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

    private void makeHandler() {
        this.setOnMousePressed(e -> {
            x = e.getSceneX();
            y = e.getSceneY();
        });

        this.setOnMouseDragged(e -> {
            this.setTranslateX(e.getSceneX() - x);
            this.setTranslateY(e.getSceneY() - y);
        });

        this.setOnMouseReleased(e -> {
            // Updating the new layout positions
            this.setLayoutX(this.getLayoutX() + this.getTranslateX());
            this.setLayoutY(this.getLayoutY() + this.getTranslateY());

            // Resetting the translate positions
            this.setTranslateX(0);
            this.setTranslateY(0);
        });
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

    public void setName(int name) {
        this.name = name;
        label.setText(String.valueOf(this.name));
    }



    public int getName() {
        return name;
    }

    public static int getRadius() {
        return radius;
    }

    public ArrayList<Edge> getConnectedEdges() {
        return edges;
    }
}
