package application;

import components.edge.DirectedEdge;
import components.edge.Edge;
import components.graph.DirectedGraph;
import components.graph.Graph;
import components.graph.UnDirectedGraph;
import components.status.Status;
import components.vertex.Vertex;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Context context;

    @FXML
    private AnchorPane playground;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        initGraphTypes();
    }

    @FXML
    private Button showCoordinate;

    @FXML
    private ComboBox optGraphType;

    @FXML
    private Button btnNewGraph;

    @FXML
    private Button btnClearGraph;

//    @FXML
//    private Button btnSetStartVertex;

    @FXML
    private Button btnNewContext;

    @FXML
    public Context makeNewContext() {
        playground.getChildren().clear();

        this.context = new Context();
        optGraphType.setValue("Choose graph type...");
        playground.setOnMouseClicked(null);

        return context;
    }

    private void initGraphTypes() {
        optGraphType.getItems().addAll("Unweighted Directed Graph", "Unweighted Undirected Graph", "Weighted Directed Graph", "Weighted Undirected Graph");
        optGraphType.setValue("Choose graph type...");
    }

    public void selectNewGraph() {
        String graphType = optGraphType.getValue().toString();

        if (graphType.equals("Choose graph type...")) {
            return;
        }

        if (graphType.equals("Unweighted Directed Graph")) {
            context.setGraph(new DirectedGraph(false));
        } else if (graphType.equals(("Weighted Directed Graph"))) {
            context.setGraph(new DirectedGraph(true));
        } else if (graphType.equals("Unweighted Undirected Graph")) {
            context.setGraph(new UnDirectedGraph(false));
        } else if (graphType.equals("Weighted Undirected Graph")) {
            context.setGraph(new UnDirectedGraph(true));
        }

        initPlayground();
    }

    @FXML
    public void clearGraph() {
        playground.getChildren().clear();
        context.getGraph().clear();
    }

    @FXML
    public void displayVertexCoordinate() {
        System.out.println();
        for (Vertex v : context.getGraph().getVertices().values()) {
            System.out.println("Vertex v" + v.getName() + " = new Vertex(\""
                                            + v.getName() + "\", " + v.getLayoutX() + ", " + v.getLayoutY() + ");");
        }
        for (Edge e : context.getGraph().getEdges()) {
            System.out.println(
                                "DirectedEdge e" + e.getFrom().getName() + "" + e.getTo().getName() + " = new DirectedEdge(v"
                                + e.getFrom().getName() + ", v" + e.getTo().getName() +
                                        (context.getGraph().isWeighted() ? ", " + e.getWeight() : "") + ");");
        }

        System.out.println();
        for (Vertex v : context.getGraph().getVertices().values()) {
            System.out.println("this.addVertex(v" + v.getName() + ");");
        }
        for (Edge e : context.getGraph().getEdges()) {
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
                    if (context.getGraph().setVertexName(v) != -1) {
                        context.getGraph().addVertex(v);
                        makeVertexHandler(v);
                        playground.getChildren().add(v);
                    }
                }
            }
        });
    }

    public void makeEdgeHandler(Edge e) {
        if (context.getGraph().isWeighted()) {
            e.getWeightPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2) {
                        context.getGraph().setEdgeWeight(e);
                    }
                }
            });
        }

        e.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isShiftDown()) {
                    context.getGraph().removeEdge(e, playground);
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
                        Edge e = context.getGraph().makeNewEdge(firstVertex, v, context.getGraph().isWeighted());
                        if (e != null) {
                            makeEdgeHandler(e);
                            playground.getChildren().add(0, e);
                        }

                        firstVertex.setVertexStyle(Status.FREE);
                        firstVertex = null;
                    }
                } else if (event.getClickCount() == 2) {
                    // rename vertex in the context.getGraph()
                    System.out.println("double click");
                    context.getGraph().setVertexName(v);
                    context.getGraph().getVertices().put(v.getName(), v);
                } else if (event.isShiftDown()) {
                    // remove vertex from the graph
                    context.getGraph().removeVertex(v, playground);
                }
            }
        });
    }

    public void setGraph(Graph graph) {
        this.context.setGraph(graph);
    }

    public void displayGraph() {
//        vertices = new ArrayList<>();
//        vertices.addAll(graph.getVertices().values());
//        playground.getChildren().addAll(vertices);
        // add all edges from an array list
        for (Edge e : context.getGraph().getEdges()) {
            makeEdgeHandler(e);
            playground.getChildren().add(0, e);
        }
        for (Vertex v : context.getGraph().getVertices().values()) {
            makeVertexHandler(v);
            playground.getChildren().add(v);
        }
//        playground.getChildren().add(new Vertex(2));

    }
}