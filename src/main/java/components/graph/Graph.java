package components.graph;

import components.edge.Edge;
import components.vertex.Vertex;
import javafx.event.EventHandler;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.*;

public abstract class Graph {

    private HashMap<String, Vertex> vertices;
    private List<Edge> edges;
    private boolean isDirected;
    private boolean isWeighted;

    public Graph(boolean isDirected, boolean isWeighted) {
        this.isDirected = isDirected;
        this.isWeighted = isWeighted;
        this.vertices = new HashMap<String, Vertex>();
        this.edges = new ArrayList<>();
    }

    public int addVertex(Vertex v) {
        if (vertices.containsKey(v.getName())){
            return -1;
        }

        vertices.put(v.getName(), v);
        return 1;
    }

    public int setVertexName(Vertex v) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Set vertex name");
        dialog.setHeaderText("Provide new value");
        Optional<String> result = dialog.showAndWait();

        while (result.isPresent() && result.get() != "") {
            String newValue = result.get();

            if (vertices.keySet().contains(newValue)) {
                dialog.setHeaderText("Value is already in use. Provide a new value");
                result = dialog.showAndWait();
            } else {
                vertices.remove(v.getName());
                v.setName(newValue);
                return 1;
            }
        }
        return -1;
    }

    public abstract Edge makeNewEdge(Vertex from, Vertex to, boolean isWeighted);

    public abstract void removeEdge(Edge e, AnchorPane playground);

    public abstract void removeVertex(Vertex v, AnchorPane playground);

    public int setEdgeWeight(Edge e) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Set edge weight");
        dialog.setHeaderText("Provide new value");
        Optional<String> result = dialog.showAndWait();

        while (result.isPresent() && result.get() != "") {
            String newValue = result.get();

            try {
                int weight = Integer.parseInt(newValue);
                e.setWeight(weight);
                return 1;
            } catch (NumberFormatException ex) {
                dialog.setHeaderText("Value is not a number. Provide a new value");
                result = dialog.showAndWait();
            }
        }
        return -1;
    }

    public void addEdge(Edge e) {
        if (edges.contains(e))
            return;
        edges.add(e);
    }

    public HashMap<String, Vertex> getVertices() {
        return vertices;
    }

    public Vertex getVertex(String name) {
        return vertices.get(name);
    }
    public List<Edge> getEdges() {
        return edges;
    }

    public void clear() {
        vertices.clear();
        edges.clear();
    }

    public abstract void makeDemoGraph();

    public void setWeighted(boolean weighted) {
        isWeighted = weighted;
    }

    public boolean isWeighted() {
        return isWeighted;
    }


//    public void fromAdjacencyMatrix(Integer[][] adjacencyMatrix) {
//        int matrixSize = adjacencyMatrix.length;
//        Vertex fromVertex;
//        Vertex toVertex;
//        Edge edge;
//
//        this.vertices.clear();
//        this.edges.clear();
//
//        for (int from = 0; from < matrixSize; from++) {
//            fromVertex = makeVertex(from);
//            for (int to = 0; to < matrixSize; to++) {
//                toVertex = makeVertex(to);
//                if (adjacencyMatrix[from][to] == 0)
//                    continue;
//                fromVertex.addNeighbor(toVertex);
//
//                if ((!isDirected) && (from > to))
//                    continue;
//                if (isDirected) {
//                    edge = new DirectedEdge(fromVertex, toVertex);
//                } else {
//                    edge = new UndirectedEdge(fromVertex, toVertex);
//                }
//                if (isWeighted) {
//                    edge.setWeight(adjacencyMatrix[from][to]);
//                }
//                edges.add(edge);
//            }
//        }
//    }


}
