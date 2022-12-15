package pf.bb;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import pf.bb.model.Article;
import pf.bb.model.Configuration;
import pf.bb.task.GetArticlesTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static final List<Article> ARTICLES = new ArrayList<>();
    public static final List<Configuration> CONFIGURATIONS = new ArrayList<>();
    public static final String API_HOST = "http://localhost:8080";


    @Override
    public void start(Stage stageMain) throws IOException {
        loadFirstClientView(stageMain);
        // Stephan - zur Trennung
        stephan();
    }

    private void stephan() {
        // query all products from REST API with Task Thread
        GetArticlesTask articlesTask = new GetArticlesTask();

        //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
        articlesTask.setOnRunning((successEvent) -> {
            System.out.println("loading articles...");
        });
        articlesTask.setOnSucceeded((WorkerStateEvent e) -> {
            System.out.println("articles loaded.");
            ARTICLES.addAll(articlesTask.getValue());
        });
        //Tasks in eigenem Thread ausführen
        new Thread(articlesTask).start();


//        // query all configurations from REST API with Task Thread
//        GetConfigurationsTask configurationsTask = new GetConfigurationsTask();
//
//        //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
//        configurationsTask.setOnRunning((successEvent) -> {
//            System.out.println("loading  configurations...");
//        });
//        configurationsTask.setOnSucceeded((WorkerStateEvent e) -> {
//            System.out.println("configurations loaded.");
//            CONFIGURATIONS.addAll(configurationsTask.getValue());
//        });
//        //Tasks in eigenem Thread ausführen
//        new Thread(configurationsTask).start();


    }


    private void loadFirstClientView(Stage stageMain) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/Login.fxml"));
        AnchorPane apane = fxmlLoader.load();
        Scene scene = new Scene(apane);
        stageMain.setTitle("Bicycle Builder - Login");
        stageMain.setResizable(false);
        stageMain.setScene(scene);
        stageMain.show();
        centerLoginScene(stageMain);
        fadeLoginScene(apane);
    }

    private void centerLoginScene(Stage stage) {
        Rectangle2D sb = Screen.getPrimary().getVisualBounds();
        stage.setX((sb.getWidth() - stage.getWidth()) / 2);
        stage.setY((sb.getHeight() - stage.getHeight()) / 2);
    }

    // AR: aktuell nur zum Testen
    private void fadeLoginScene(AnchorPane pane) {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), pane);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    public static void main(String[] args) {
        launch();
    }
}