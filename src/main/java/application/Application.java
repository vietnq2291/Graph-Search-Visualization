package application;

import algorithm.DFS;
import components.graph.DirectedGraph;
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


        Context context = controller.makeNewContext();



        // Demo
//        DirectedGraph dg = new DirectedGraph();
//        dg.makeDemoGraph();

//        DFS dfs = new DFS();
//        dfs.setGraph(dg);
//        dfs.prepare();
//        dfs.setStartVertexName("2");
//        dfs.execute();

//        controller.setGraph(dg);
//        controller.displayGraph();
    }

    public static void main(String[] args) {
        launch();
    }
}