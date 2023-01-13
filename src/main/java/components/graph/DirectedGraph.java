package components.graph;

import components.edge.DirectedEdge;
import components.edge.Edge;
import components.vertex.Vertex;

public class DirectedGraph extends Graph {

    public DirectedGraph() {
        super(true, true);
    }

    public void addEdge(Edge edge) {
        if (edge.isDirected()) {
            super.addEdge(edge);
            edge.getFrom().addConnectedEdge(edge);
        } else {
            throw new IllegalArgumentException("Edge is not directed");
        }
    }

    @Override
    public Edge makeNewEdge(Vertex from, Vertex to) {
        if (from == to) {
            return null;
        }

        for (Edge e : from.getConnectedEdges()) {
            if (e.getTo() == to) {
                return null;
            }
        }

        Edge edge = new DirectedEdge(from, to);
        addEdge(edge);
        return edge;
    }

    public void makeDemoGraph() {
        this.clear();

        Vertex v0 = new Vertex(0, 100, 200);
        Vertex v1 = new Vertex(1, 200, 100);
        Vertex v2 = new Vertex(2, 300, 200);
        Vertex v3 = new Vertex(3, 200, 300);

        DirectedEdge e01 = new DirectedEdge(v0, v1);
        DirectedEdge e02 = new DirectedEdge(v0, v2);
        DirectedEdge e13 = new DirectedEdge(v1, v3);
        DirectedEdge e12 = new DirectedEdge(v1, v2);
        DirectedEdge e20 = new DirectedEdge(v2, v0);
        DirectedEdge e33 = new DirectedEdge(v3, v3);

        this.addVertex(v0);
        this.addVertex(v1);
        this.addVertex(v2);
        this.addVertex(v3);
        this.addEdge(e01);
        this.addEdge(e02);
        this.addEdge(e13);
        this.addEdge(e12);
        this.addEdge(e20);
        this.addEdge(e33);
    }
}
