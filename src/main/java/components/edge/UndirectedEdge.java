package components.edge;

import components.vertex.Vertex;

public class UndirectedEdge extends Edge {

    public UndirectedEdge(Vertex from, Vertex to) {
        super(from, to);
        super.setDirected(false);
    }
    public UndirectedEdge(Vertex from, Vertex to, int weight) {
        super(from, to, weight);
        super.setDirected(false);
    }

    @Override
    public String toString() {
        return super.getFrom().getName() + " ---- " + super.getTo().getName();
    }

}
