package application;

import components.edge.DirectedEdge;
import components.edge.Edge;
import components.graph.Graph;
import components.status.Status;
import components.vertex.Vertex;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

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

    @FXML
    private Button showCoordinate;

    @FXML
    public void displayVertexCoordinate() {
        System.out.println();
        for (Vertex v : graph.getVertices().values()) {
            System.out.println("Vertex v" + v.getName() + " = new Vertex(\""
                                            + v.getName() + "\", " + v.getLayoutX() + ", " + v.getLayoutY() + ");");
        }
        for (Edge e : graph.getEdges()) {
            System.out.println(
                                "DirectedEdge e" + e.getFrom().getName() + "" + e.getTo().getName() + " = new DirectedEdge(v"
                                + e.getFrom().getName() + ", v" + e.getTo().getName() +
                                        (graph.isWeighted() ? ", " + e.getWeight() : "") + ");");
        }

        System.out.println();
        for (Vertex v : graph.getVertices().values()) {
            System.out.println("this.addVertex(v" + v.getName() + ");");
        }
        for (Edge e : graph.getEdges()) {
            System.out.println("this.addEdge(e" + e.getFrom().getName() + "" + e.getTo().getName() + ");");
        }

        System.out.println();
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
                    if (graph.setVertexName(v) != -1) {
                        graph.addVertex(v);
                        makeVertexHandler(v);
                        playground.getChildren().add(v);
                    }
                }
            }
        });
    }

    public void makeEdgeHandler(Edge e) {
        if (graph.isWeighted()) {
            e.getWeightPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2) {
                        graph.setEdgeWeight(e);
                    }
                }
            });
        }

        e.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isShiftDown()) {
                    graph.removeEdge(e, playground);
                }
            }
        });
    }

    public void makeVertexHandler(Vertex v) {

        v.setOnMouseClicked(new EventHandler<MouseEvent>() {
            static Vertex firstVertex = null;
            @Override
            public void handle(MouseEvent event) {
                if (event.isAltDown()) {
                    // add new edge on Alt + click
                    if (firstVertex == null) {
                        firstVertex = v;
                        v.setVertexStyle(Status.SELECTED);
                    } else {
                        Edge e = graph.makeNewEdge(firstVertex, v, graph.isWeighted());
                        if (e != null) {
                            makeEdgeHandler(e);
                            playground.getChildren().add(0, e);
                        }

                        firstVertex.setVertexStyle(Status.FREE);
                        firstVertex = null;
                    }
                } else if (event.getClickCount() == 2) {
                    // rename vertex in the graph
                    System.out.println("double click");
                    graph.setVertexName(v);
                    graph.getVertices().put(v.getName(), v);
                } else if (event.isShiftDown()) {
                    // remove vertex from the graph
                    graph.removeVertex(v, playground);
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
            makeEdgeHandler(e);
            playground.getChildren().add(0, e);
        }
        for (Vertex v : graph.getVertices().values()) {
            makeVertexHandler(v);
            playground.getChildren().add(v);
        }
//        playground.getChildren().add(new Vertex(2));

    }
}