package at.fhtw.tourplanner.bl.service;

import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.dal.api.MapQuestAPI;
import at.fhtw.tourplanner.dal.api.response.RouteResponse;
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

    public MapQuestService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.mapquestapi.com/").addConverterFactory(JacksonConverterFactory.create()).build();
        api = retrofit.create(MapQuestAPI.class);
        constantsMap.put("key", key);
        constantsMap.put("unit", "k");
        constantsMap.put("size", "600,400@2x");
    }

    public RouteResponse searchRoute(TourItem tourItem) {
        RouteResponse cachedRouteResponse = getRouteFromCache(tourItem);
        if (cachedRouteResponse != null) {
            return cachedRouteResponse;
        }
        try {
            Call<RouteResponse> call = api.getRoute(constantsMap, tourItem.getFrom(), tourItem.getTo(), tourItem.getTransportType());
            Response<RouteResponse> response = call.execute();
            RouteResponse result = response.body();
            putRouteInCache(tourItem, result);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public void setRouteImage(TourItem tourItem, MapListener mapListener) {
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
                RouteResponse routeResponse = response.body();

                Call<ResponseBody> callImage = fetchRouteImageAsync(routeResponse.getRoute().getSessionId(), tourItem.getTransportType());
                callImage.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    }
                });
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable throwable) {
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
