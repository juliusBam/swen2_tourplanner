package at.fhtw.tourplanner.bl.service;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.dal.api.MapQuestAPI;
import at.fhtw.tourplanner.dal.api.response.Route;
import at.fhtw.tourplanner.dal.api.response.RouteResponse;
import javafx.application.Platform;
import javafx.scene.image.Image;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MapQuestService {
    private final MapQuestAPI api;
    private final String key = "Vg5MvjVijd5lRe6xqUXDdJR1SKcuce0h";
    private final Map<String, Image> imageCache = new HashMap<>();
    private final Map<String, RouteResponse> routeCache = new HashMap<>();
    private final Map<String, String> constantsMap = new HashMap<>();
    private Call<RouteResponse> setImageCall;

    public interface MapListener {
        void updateMap(Image routeImage);
    }

    public interface ErrorListener {
        void onError(String title, String errorMsg);
    }

    public interface RouteListener {
        void setRoute(RouteResponse routeResponse);
    }

    public MapQuestService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.mapquestapi.com/").addConverterFactory(JacksonConverterFactory.create()).build();
        api = retrofit.create(MapQuestAPI.class);
        constantsMap.put("key", key);
        constantsMap.put("unit", "k");
        constantsMap.put("size", "600,400@2x");
    }

    public void searchRoute(TourItem tourItem, RouteListener routeListener, ErrorListener errorListener) {
        RouteResponse cachedRouteResponse = getRouteFromCache(tourItem);
        if (cachedRouteResponse != null) {
            routeListener.setRoute(cachedRouteResponse);
            //return cachedRouteResponse;
        } else {

            Call<RouteResponse> apiCall = api.getRoute(constantsMap, tourItem.getFrom(), tourItem.getTo(), tourItem.getTransportType());
            apiCall.enqueue(new Callback<RouteResponse>() {
                @Override
                public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {

                    if (response.body() != null && response.body().getInfo().getStatusCode() == 0) {
                        Platform.runLater(() -> {

                            routeListener.setRoute(response.body());

                        });
                    } else {

                        Platform.runLater(() -> {
                            //update application thread
                            try {
                                if (response.errorBody() != null) {
                                    errorListener.onError("Error fetching the route", response.errorBody().string());
                                } else {
                                    errorListener.onError("Error fetching the route", response.message());
                                }

                            } catch (IOException | NullPointerException e) {
                                errorListener.onError("Unknown error", e.getMessage());
                            }

                        });

                    }

                }

                @Override
                public void onFailure(Call<RouteResponse> call, Throwable throwable) {
                    Platform.runLater(() -> {
                        //update application thread
                        errorListener.onError("Error fetching route", throwable.getMessage());

                    });
                }
            });
        }

    }

    private Call<RouteResponse> searchRouteAsync(String from, String to, String transportType) {
        return api.getRoute(constantsMap, from, to, transportType);
    }

    private Call<ResponseBody> fetchRouteImageAsync(String session, String transportType) {
        String color = "255,0,0";
        if (transportType.equals("bicycle")) {
            color = "0,255,0";
        } else if (transportType.equals("pedestrian")) {
            color = "0,0,255";
        }
        return api.getImage(constantsMap, session, color);
    }

    public void setRouteImage(TourItem tourItem, MapListener mapListener, ErrorListener errorListener) {
        System.out.println("Setting image for tour " + tourItem.getName());
        Image cachedImage = getImageFromCache(tourItem);
        if (cachedImage != null) {
            mapListener.updateMap(cachedImage);
            return;
        }
        System.out.println("Image not found in cache, requesting");

        setImageCall = searchRouteAsync(tourItem.getFrom(), tourItem.getTo(), tourItem.getTransportType());
        setImageCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {

                if (response.body() == null) {
                    Platform.runLater(() -> {
                        //update application thread
                        try {
                            errorListener.onError("Error loading route information for tour: ", response.errorBody().string());
                        } catch (IOException | NullPointerException e) {
                            errorListener.onError("Unknown error", e.getMessage());
                        }

                    });
                    return;
                }

                RouteResponse routeResponse = response.body();

                Call<ResponseBody> callImage = fetchRouteImageAsync(routeResponse.getRoute().getSessionId(), tourItem.getTransportType());
                callImage.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response != null && response.body() != null) {
                            ResponseBody responseBody = response.body();

                            byte[] bytes;
                            try {
                                bytes = responseBody.bytes();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            InputStream inputStream = new ByteArrayInputStream(bytes);
                            Image image = new Image(inputStream);
                            putImageInCache(tourItem, image);
                            mapListener.updateMap(image);
                        } else {

                            Platform.runLater(() -> {
                                //update application thread
                                try {
                                    errorListener.onError("Error loading route information for tour: ", response.errorBody().string());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                            });

                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        if(throwable.getMessage().contains("Canceled"))
                        {
                            return;
                        }
                        Platform.runLater(() -> {
                            //update application thread
                            errorListener.onError("Error loading route information for tour: ", throwable.getMessage());

                        });
                    }
                });
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable throwable) {
                if(throwable.getMessage().contains("Canceled"))
                {
                    return;
                }
                Platform.runLater(() -> {
                    //update application thread
                    errorListener.onError("Error loading route information for tour: ", throwable.getMessage());

                });
            }
        });
    }

    public void cancelSetRouteImage() {
        if (setImageCall != null) {
            setImageCall.cancel();
        }
    }

    private Image getImageFromCache(TourItem tourItem) {
        return imageCache.get(tourItem.getFrom() + tourItem.getTo() + tourItem.getTransportType());
    }

    private void putImageInCache(TourItem tourItem, Image mapImage) {
        imageCache.put(tourItem.getFrom() + tourItem.getTo() + tourItem.getTransportType(), mapImage);
    }

    private RouteResponse getRouteFromCache(TourItem tourItem) {
        return routeCache.get(tourItem.getFrom() + tourItem.getTo() + tourItem.getTransportType());
    }

    private void putRouteInCache(TourItem tourItem, RouteResponse routeResponse) {
        routeCache.put(tourItem.getFrom() + tourItem.getTo() + tourItem.getTransportType(), routeResponse);
    }

    public String getSessionId(TourItem tourItem) {
        Call<RouteResponse> callRoute = searchRouteAsync(tourItem.getFrom(), tourItem.getTo(), tourItem.getTransportType());
        try {
            return callRoute.execute().body().getRoute().getSessionId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
