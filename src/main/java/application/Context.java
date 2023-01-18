package application;

import algorithm.Algorithm;
import components.graph.Graph;

public class Context {

    private Algorithm algorithm;
    private Graph graph;
    private String startVertexName;

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setStartVertex(String name) {
        this.startVertexName = name;
    }

    public Graph getGraph() {
        return graph;
    }
}
