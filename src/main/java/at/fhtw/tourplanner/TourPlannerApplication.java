package at.fhtw.tourplanner;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class TourPlannerApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {

        final String applicationTitle = "Tour Planner";
        final int defaultHeight = 500;
        final int defaultWidth = 865;

        Parent root = FXMLDependencyInjection.load("TourPlanner_mainWindow.fxml", Locale.ENGLISH);
        Scene scene = new Scene(root, defaultWidth, defaultHeight);

        stage.setTitle(applicationTitle);

        stage.setScene(scene);
        stage.show();
    }
}