package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.bl.service.MapQuestService;
import at.fhtw.tourplanner.bl.service.TourItemService;
import at.fhtw.tourplanner.bl.service.TourLogService;
import at.fhtw.tourplanner.dal.api.TourPlannerAPI;
import at.fhtw.tourplanner.dal.repository.TourItemRepository;
import at.fhtw.tourplanner.viewModel.*;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ControllerFactory {

    private static final ControllerFactory instance = new ControllerFactory();
    private final BottomPaneViewModel bottomPaneViewModel;
    private final CenterPaneViewModel centerPaneViewModel;
    private final LeftPaneViewModel leftPaneViewModel;
    private final TopMenuViewModel topMenuViewModel;

    private final TourLogsTabViewModel tourLogsTabViewModel;

    private final TourPlannerApplicationViewModel tourPlannerApplicationViewModel;

    public ControllerFactory() {
        TourPlannerAPI tourPlannerAPI =
                new Retrofit
                        .Builder()
                        .baseUrl("http://localhost:8080/")
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build()
                        .create(TourPlannerAPI.class);

        TourItemRepository tourItemRepository = new TourItemRepository(tourPlannerAPI);
        TourItemService tourItemService = new TourItemService(tourItemRepository);
        MapQuestService mapQuestService = new MapQuestService();

        this.bottomPaneViewModel = new BottomPaneViewModel();
        this.centerPaneViewModel = new CenterPaneViewModel(tourItemService, mapQuestService);
        this.leftPaneViewModel = new LeftPaneViewModel(tourItemService, mapQuestService);
        this.topMenuViewModel = new TopMenuViewModel();
        this.tourLogsTabViewModel = new TourLogsTabViewModel(new TourLogService());
        this.tourPlannerApplicationViewModel = new TourPlannerApplicationViewModel(this.bottomPaneViewModel, this.centerPaneViewModel, this.leftPaneViewModel,
                                                                                    this.topMenuViewModel, this.tourLogsTabViewModel);
    }

    public static ControllerFactory getInstance() {
        return instance;
    }

    public TourPlannerController create(Class<?> controllerClass) {

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

        } else if (controllerClass == TourLogsTabController.class) {

            return new TourLogsTabController(this.tourLogsTabViewModel);

        }

        throw new IllegalArgumentException("Unknown controller class: " + controllerClass);
    }
}
