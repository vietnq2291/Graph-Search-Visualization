package algorithm;

import components.graph.Graph;

public abstract class Algorithm {
    private Graph graph;
    private int startVertexId;

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

    public int getStartVertexId() {
        return startVertexId;
    }

    public void setStartVertexId(int startVertexId) {
        this.startVertexId = startVertexId;
    }
}
