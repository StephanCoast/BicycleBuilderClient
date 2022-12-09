package pf.bb;

import javafx.application.Application;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pf.bb.controller.LoginController;
import pf.bb.model.Article;
import pf.bb.model.User;
import pf.bb.task.GetArticlesTask;
import pf.bb.task.GetUserDetailsTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main extends Application {


    public static final String APP_TITLE = "Bicycle Builder";
    public static final String API_HOST = "http://localhost:8080";
    public static final List<Article> ARTICLES = new ArrayList<>();

    @Override
    public void start(Stage stage) throws IOException {

        Node main = loadFXML("fxml/Main.fxml");
        Scene scene = new Scene((Parent) main, 1024, 768);
        stage.setScene(scene);
        stage.setMinWidth(1280);
        stage.setMinHeight(720);
        stage.setTitle(APP_TITLE);

//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/Login.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/Login.css")).toExternalForm());
//        stage.setTitle("Bicycle Builder - Login");
//        stage.setWidth(300.00);
//        stage.setHeight(250.00);
//        stage.setResizable(false);
//        stage.setScene(scene);
//        Rectangle2D sb = Screen.getPrimary().getVisualBounds();
//        stage.setX((sb.getWidth() - stage.getWidth()) / 2);
//        stage.setY((sb.getHeight() - stage.getHeight()) / 2);

        stage.show();


        // query all products from REST API with Task Thread
        GetArticlesTask articlesTask = new GetArticlesTask();

        //Erst Task definieren incl. WorkerStateEvent als Flag, um zu wissen, wann fertig
        articlesTask.setOnRunning((successEvent) -> {
            System.out.println("loading articles...");
        });
        articlesTask.setOnSucceeded((WorkerStateEvent e) -> {
            System.out.println("articles loaded.");
            ARTICLES.addAll(articlesTask.getValue());
            // TODO MAYBE query images from Server
//            // query product images from REST API
//            for (Product product : PRODUCTS) {
//                GetProductImageTask imageTask = new GetProductImageTask(product);
//                imageTask.setOnSucceeded((WorkerStateEvent e2) -> {
//                    product.image = imageTask.getValue();
//                });
//                new Thread(imageTask).start();
//            }

        });
        //Tasks in eigenem Thread ausführen
        new Thread(articlesTask).start();
    }


    public static void queryActiveUserBookings(Runnable runnable) {
        User user = LoginController.activeUser;
        GetUserDetailsTask getUserTask = new GetUserDetailsTask(user);
        getUserTask.setOnSucceeded((WorkerStateEvent e2) -> {
            user.id = getUserTask.getValue().id;

            // TODO remove - We don't need user specific configuration view because all consultants need to see all configurations
//            user.configurations.clear();
//            user.configurations.addAll(bookingsTask.getValue().configurations);

            if (runnable != null) {
                new Thread(runnable).start();
            }
        });
        new Thread(getUserTask).start();
    }

    public static Node loadFXML(String fxmlFilename) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlFilename));
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        launch();
    }
}