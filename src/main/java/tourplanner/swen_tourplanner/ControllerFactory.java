package tourplanner.swen_tourplanner;

import tourplanner.swen_tourplanner.viewModels.*;
import tourplanner.swen_tourplanner.views.*;

public class ControllerFactory {

    private final BottomPaneViewModel bottomPaneViewModel;

    private final CenterPaneViewModel centerPaneViewModel;

    private final LeftPaneViewModel leftPaneViewModel;

    private final TopMenuViewModel topMenuViewModel;

    private final TourPlannerApplicationViewModel tourPlannerApplicationViewModel;

    public ControllerFactory() {
        this.bottomPaneViewModel = new BottomPaneViewModel();
        this.centerPaneViewModel = new CenterPaneViewModel();
        this.leftPaneViewModel = new LeftPaneViewModel();
        this.topMenuViewModel = new TopMenuViewModel();
        this.tourPlannerApplicationViewModel = new TourPlannerApplicationViewModel(
                this.bottomPaneViewModel, this.centerPaneViewModel, this.leftPaneViewModel,
                this.topMenuViewModel
        );
    }

    public Object create(Class<?> controllerClass) {

        if (controllerClass == TourPlannerApplicationController.class) {
            return new TourPlannerApplicationController(this.tourPlannerApplicationViewModel);
        } else if (controllerClass == BottomPaneController.class) {
            return new BottomPaneController(bottomPaneViewModel);
        } else if (controllerClass == CenterPaneController.class) {
            return new CenterPaneController(centerPaneViewModel);
        } else if (controllerClass == LeftPaneController.class) {
            return new LeftPaneController(leftPaneViewModel);
        } else if (controllerClass == TopMenuController.class) {
            return new TopMenuController(topMenuViewModel);
        }
        throw new IllegalArgumentException("Unknown controller class: " + controllerClass);
    }


    //
    // Singleton-Pattern with early-binding
    //
    private static ControllerFactory instance = new ControllerFactory();

    public static ControllerFactory getInstance() {
        return instance;
    }
}
