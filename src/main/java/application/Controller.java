package application;

import components.edge.Edge;
import components.graph.Graph;
import components.status.Status;
import components.vertex.Vertex;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Graph graph;

    @FXML
    private AnchorPane playground;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        initPlayground();
    }

    private void initPlayground() {

        // add new vertex on Ctrl + click
        playground.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX() - Vertex.getRadius();
                double y = event.getY() - Vertex.getRadius();

                if (10 < x && x < 850 && 10 < y && y < 535 && event.isControlDown()) {
                    Vertex v = new Vertex(x, y);
                    if (graph.setVertexName(v) != null) {
                        graph.addVertex(v);
                        makeVertexHandler(v);
                        playground.getChildren().add(v);
                    }
                }
            }
        });
    }

    public void makeVertexHandler(Vertex v) {

        // rename vertex
        v.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    graph.setVertexName(v);
                }
            }
        });

        // add new edge on Alt + click
        v.setOnMouseClicked(new EventHandler<MouseEvent>() {
            static Vertex firstVertex = null;
            @Override
            public void handle(MouseEvent event) {
                if (event.isAltDown()) {
                    if (firstVertex == null) {
                        firstVertex = v;
                        v.setVertexStyle(Status.SELECTED);
                    } else {
                        Edge e = graph.makeNewEdge(firstVertex, v);
                        if (e != null) {
                            playground.getChildren().add(0, e.getEdgePane());
                        }

                        firstVertex.setVertexStyle(Status.FREE);
                        firstVertex = null;
                    }
                }
            }
        });
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void displayGraph() {
//        vertices = new ArrayList<>();
//        vertices.addAll(graph.getVertices().values());
//        playground.getChildren().addAll(vertices);
        // add all edges from an array list
        for (Edge e : graph.getEdges()) {
            playground.getChildren().add(0, e.getEdgePane());
        }
        for (Vertex v : graph.getVertices().values()) {
            makeVertexHandler(v);
            playground.getChildren().add(v);
        }
//        playground.getChildren().add(new Vertex(2));

    }
}