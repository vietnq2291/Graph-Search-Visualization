package algorithm;

import components.edge.Edge;
import components.vertex.Vertex;

import java.util.HashMap;

public class DFS extends Algorithm {

    private HashMap<Integer, Boolean> visited;

    public DFS() {
        super();
    }

    public void prepare() {
        visited = new HashMap<>();
        for (Vertex v : this.getGraph().getVertices().values()) {
            visited.put(v.getName(), false);
        }
    }

    @Override
    public void execute() {
        int s = this.getStartVertexId();
        dfs(this.getGraph().getVertex(s));
    }

    private void dfs(Vertex vertex) {
        System.out.println("Visiting vertex " + vertex.getName());
        visited.put(vertex.getName(), true);

        for (Edge e : vertex.getConnectedEdges()) {
            Vertex nextVertex = e.getTo();
            if (visited.get(nextVertex.getName()) == false) {
                dfs(nextVertex);
            }
        }
    }

}
