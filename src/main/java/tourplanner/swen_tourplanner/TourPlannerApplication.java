package tourplanner.swen_tourplanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TourPlannerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        final String applicationTitle = "Tour Planner";
        final int defaultHeight = 602;
        final int defaultWidth = 673;

        FXMLLoader fxmlLoader = new FXMLLoader(TourPlannerApplication.class.getResource("tourPlanner_mainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), defaultWidth, defaultHeight);

        stage.setTitle(applicationTitle);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}