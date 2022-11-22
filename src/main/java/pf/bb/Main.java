package pf.bb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/Login.css")).toExternalForm());
        stage.setTitle("Bicycle Builder - Login");
        stage.setWidth(300.00);
        stage.setHeight(250.00);
        stage.setResizable(false);
        stage.setScene(scene);
        Rectangle2D sb = Screen.getPrimary().getVisualBounds();
        stage.setX((sb.getWidth() - stage.getWidth()) / 2);
        stage.setY((sb.getHeight() - stage.getHeight()) / 2);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}