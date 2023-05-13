module tourplanner.swen_tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;
            
                            
    opens tourplanner.swen_tourplanner to javafx.fxml;
    exports tourplanner.swen_tourplanner;
    exports tourplanner.swen_tourplanner.views;
    opens tourplanner.swen_tourplanner.views to javafx.fxml;
    exports tourplanner.swen_tourplanner.viewModels;
    opens tourplanner.swen_tourplanner.viewModels to javafx.fxml;
}
