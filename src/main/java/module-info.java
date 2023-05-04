module tourplanner.swen_tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens tourplanner.swen_tourplanner to javafx.fxml;
    exports tourplanner.swen_tourplanner;
}