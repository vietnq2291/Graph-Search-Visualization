package components.graph;

import components.edge.DirectedEdge;
import components.edge.Edge;
import components.vertex.Vertex;
import javafx.scene.layout.AnchorPane;

import java.util.Iterator;

public class DirectedGraph extends Graph {

    public DirectedGraph() {
        super(true, false);
    }
    public DirectedGraph(boolean isWeighted) {
        super(true, isWeighted);
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
    public Edge makeNewEdge(Vertex from, Vertex to, boolean isWeighted) {
        if (from == to) {
            return null;
        }

        for (Edge e : from.getConnectedEdges()) {
            if (e.getTo() == to) {
                return null;
            }
        }

        Edge edge = new DirectedEdge(from, to);
        if (isWeighted) {
            int flag = this.setEdgeWeight(edge);
            if (flag == -1) {
                return null;
            }
        }
        addEdge(edge);
        return edge;
    }

    @Override
    public void removeEdge(Edge e, AnchorPane playground) {
        this.getEdges().remove(e);
        e.getFrom().removeConnectedEdge(e);
        playground.getChildren().remove(e);
    }

    @Override
    public void removeVertex(Vertex v, AnchorPane playground) {
        for (Iterator<Edge> it = this.getEdges().iterator(); it.hasNext(); ) {
            Edge e = it.next();
            if (e.getFrom() == v || e.getTo() == v) {
                it.remove();
                e.getFrom().removeConnectedEdge(e);
                removeEdge(e, playground);
            }
        }
        this.getVertices().remove(v.getName());
        playground.getChildren().remove(v);
    }

    public void makeDemoGraph() {
        this.clear();

//        this.setWeighted(true);
        this.setWeighted(false);

        Vertex vA = new Vertex("A", 96.0, 240.0);
        Vertex vB = new Vertex("B", 341.0, 242.0);
        Vertex vC = new Vertex("C", 572.0, 241.0);
        Vertex vD = new Vertex("D", 98.0, 427.0);
        Vertex vE = new Vertex("E", 570.0, 434.0);
        Vertex vF = new Vertex("F", 338.0, 433.0);
        Vertex vG = new Vertex("G", 765.0, 235.0);
        Vertex vH = new Vertex("H", 406.0, 27.0);
        DirectedEdge eAB = new DirectedEdge(vA, vB);
        DirectedEdge eAD = new DirectedEdge(vA, vD);
        DirectedEdge eBC = new DirectedEdge(vB, vC);
        DirectedEdge eBF = new DirectedEdge(vB, vF);
        DirectedEdge eCE = new DirectedEdge(vC, vE);
        DirectedEdge eCG = new DirectedEdge(vC, vG);
        DirectedEdge eCH = new DirectedEdge(vC, vH);
        DirectedEdge eGE = new DirectedEdge(vG, vE);
        DirectedEdge eGH = new DirectedEdge(vG, vH);
        DirectedEdge eEB = new DirectedEdge(vE, vB);
        DirectedEdge eEF = new DirectedEdge(vE, vF);
        DirectedEdge eFA = new DirectedEdge(vF, vA);
        DirectedEdge eDF = new DirectedEdge(vD, vF);
        DirectedEdge eHA = new DirectedEdge(vH, vA);

        this.addVertex(vA);
        this.addVertex(vB);
        this.addVertex(vC);
        this.addVertex(vD);
        this.addVertex(vE);
        this.addVertex(vF);
        this.addVertex(vG);
        this.addVertex(vH);
        this.addEdge(eAB);
        this.addEdge(eAD);
        this.addEdge(eBC);
        this.addEdge(eBF);
        this.addEdge(eCE);
        this.addEdge(eCG);
        this.addEdge(eCH);
        this.addEdge(eGE);
        this.addEdge(eGH);
        this.addEdge(eEB);
        this.addEdge(eEF);
        this.addEdge(eFA);
        this.addEdge(eDF);
        this.addEdge(eHA);





//        Vertex v0 = new Vertex("0", 100, 200);
//        Vertex v1 = new Vertex("1", 200, 100);
//        Vertex v2 = new Vertex("2", 300, 200);
//        Vertex v3 = new Vertex("3", 200, 300);
//
//        DirectedEdge e01 = new DirectedEdge(v0, v1, 2);
//        DirectedEdge e02 = new DirectedEdge(v0, v2, 5);
//        DirectedEdge e13 = new DirectedEdge(v1, v3, 1);
//        DirectedEdge e12 = new DirectedEdge(v1, v2,4);
//        DirectedEdge e20 = new DirectedEdge(v2, v0, 0);
//        DirectedEdge e33 = new DirectedEdge(v3, v3, -2);
//
//        this.addVertex(v0);
//        this.addVertex(v1);
//        this.addVertex(v2);
//        this.addVertex(v3);
//        this.addEdge(e01);
//        this.addEdge(e02);
//        this.addEdge(e13);
//        this.addEdge(e12);
//        this.addEdge(e20);
//        this.addEdge(e33);
    }
}
