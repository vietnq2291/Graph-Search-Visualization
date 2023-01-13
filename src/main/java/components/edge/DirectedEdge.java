package components.edge;

import components.vertex.Vertex;

public class DirectedEdge extends Edge {

    public DirectedEdge(Vertex from, Vertex to) {
        super(from, to);
        super.setDirected(true);
    }
    public DirectedEdge(Vertex from, Vertex to, int weight) {
        super(from, to, weight);
        super.setDirected(true);
    }

    @Override
    public String toString() {
        return super.getFrom().getName() + " ---→ " + super.getTo().getName();
    }

}
