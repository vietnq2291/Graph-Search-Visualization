package application;

import algorithm.DFS;
import components.edge.DirectedEdge;
import components.edge.Edge;
import components.graph.DirectedGraph;
import components.graph.Graph;
import components.vertex.Vertex;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Controller controller = fxmlLoader.getController();
        stage.setTitle("Graph Visualization");
        stage.setScene(scene);
        stage.show();

        // Demo
        DirectedGraph dg = new DirectedGraph();
        dg.makeDemoGraph();

        DFS dfs = new DFS();
        dfs.setGraph(dg);
        dfs.prepare();
        dfs.setStartVertexId(2);
        dfs.execute();

        controller.setGraph(dg);
        controller.displayGraph();
    }

    public static void main(String[] args) {
        launch();
    }
}