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

    public ITourPlannerController create(Class<?> controllerClass) {

        //injects the correct viewmodel into the controller

        if (controllerClass == TourPlannerApplicationController.class) {

            return new TourPlannerApplicationController(this.tourPlannerApplicationViewModel);

        } else if (controllerClass == BottomPaneController.class) {

            return new BottomPaneController(this.bottomPaneViewModel);

        } else if (controllerClass == CenterPaneController.class) {

            return new CenterPaneController(this.centerPaneViewModel);

        } else if (controllerClass == LeftPaneController.class) {

            return new LeftPaneController(this.leftPaneViewModel);

        } else if (controllerClass == TopMenuController.class) {

            return new TopMenuController(this.topMenuViewModel);

        }

        throw new IllegalArgumentException("Unknown controller class: " + controllerClass);
    }

    private static final ControllerFactory instance = new ControllerFactory();

    public static ControllerFactory getInstance() {
        return instance;
    }
}
