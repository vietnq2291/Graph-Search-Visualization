package algorithm;

import components.graph.Graph;

public abstract class Algorithm {
    private Graph graph;
    private String startVertexName;

    public Algorithm() {
    }

    public abstract void prepare();
    public abstract void execute();

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public String getStartVertexName() {
        return startVertexName;
    }

    public void setStartVertexName(String name) {
        this.startVertexName = name;
    }
}
