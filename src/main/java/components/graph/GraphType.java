package components.graph;

public enum GraphType {
    WEIGHTED_DIRECTED("Weighted Directed Graph"),
    WEIGHTED_UNDIRECTED("Weighted Undirected Graph"),
    UNWEIGHTED_DIRECTED("Unweighted Directed Graph"),
    UNWEIGHTED_UNDIRECTED("Unweighted Undirected Graph");

    private String name;

    GraphType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
