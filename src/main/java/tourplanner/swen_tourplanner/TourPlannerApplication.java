package tourplanner.swen_tourplanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class TourPlannerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        final String applicationTitle = "Tour Planner";
        final int defaultHeight = 602;
        final int defaultWidth = 673;

        Parent root = FXMLDependencyInjection.load("tourPlanner_mainWindow.fxml", Locale.ENGLISH);
        Scene scene = new Scene(root, defaultWidth, defaultHeight);

        stage.setTitle(applicationTitle);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}