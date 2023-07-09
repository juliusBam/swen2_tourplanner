package at.fhtw.tourplanner.view;

import at.fhtw.tourplanner.bl.service.*;
import at.fhtw.tourplanner.dal.api.MapQuestAPI;
import at.fhtw.tourplanner.dal.api.TourPlannerAPI;
import at.fhtw.tourplanner.dal.repository.ReportRepository;
import at.fhtw.tourplanner.dal.repository.TourItemRepository;
import at.fhtw.tourplanner.dal.repository.TourLogRepository;
import at.fhtw.tourplanner.viewModel.DetailsViewModel;
import at.fhtw.tourplanner.viewModel.OverviewViewModel;
import at.fhtw.tourplanner.viewModel.TourLogsTabViewModel;
import at.fhtw.tourplanner.viewModel.TourPlannerApplicationViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ControllerFactory {

    private static final ControllerFactory instance = new ControllerFactory();
    private final DetailsViewModel detailsViewModel;
    private final OverviewViewModel overviewViewModel;

    private final TourLogsTabViewModel tourLogsTabViewModel;

    private final TourPlannerApplicationViewModel tourPlannerApplicationViewModel;

    public ControllerFactory() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("src/main/resources/at/fhtw/tourplanner/config/config.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        TourPlannerAPI tourPlannerAPI =
                new Retrofit
                        .Builder()
                        .baseUrl(properties.getProperty("tourPlannerServer.url"))
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build()
                        .create(TourPlannerAPI.class);

        TourItemRepository tourItemRepository = new TourItemRepository(tourPlannerAPI);
        ReportRepository reportRepository = new ReportRepository(tourPlannerAPI);

        TourItemService tourItemService = new TourItemService(tourItemRepository);

        TourLogRepository tourLogRepository = new TourLogRepository(tourPlannerAPI);
        TourLogService tourLogService = new TourLogService(tourLogRepository);

        MapQuestAPI mapQuestAPI = new Retrofit
                .Builder()
                .baseUrl(properties.getProperty("mapquest.url"))
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(MapQuestAPI.class);
        MapQuestService mapQuestService = new MapQuestService(mapQuestAPI, properties);
        ReportService reportService = new ReportService(reportRepository);
        ImportExportService importExportService = new ImportExportService(new ObjectMapper());

        this.detailsViewModel = new DetailsViewModel(tourItemService, mapQuestService);
        this.overviewViewModel = new OverviewViewModel(tourItemService, mapQuestService, reportService, importExportService, tourLogService);
        this.tourLogsTabViewModel = new TourLogsTabViewModel(tourLogService);
        this.tourPlannerApplicationViewModel = new TourPlannerApplicationViewModel(this.detailsViewModel, this.overviewViewModel,
                this.tourLogsTabViewModel);
    }

    public static ControllerFactory getInstance() {
        return instance;
    }

    public TourPlannerController create(Class<?> controllerClass) {

        //injects the correct viewmodel into the controller

        if (controllerClass == TourPlannerApplicationController.class) {

            return new TourPlannerApplicationController(this.tourPlannerApplicationViewModel);

        } else if (controllerClass == DetailsController.class) {

            return new DetailsController(this.detailsViewModel);

        } else if (controllerClass == OverviewController.class) {

            return new OverviewController(this.overviewViewModel);

        } else if (controllerClass == TourLogsTabController.class) {

            return new TourLogsTabController(this.tourLogsTabViewModel);

        }

        throw new IllegalArgumentException("Unknown controller class: " + controllerClass);
    }
}
