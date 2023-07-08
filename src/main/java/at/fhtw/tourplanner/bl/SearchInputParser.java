package at.fhtw.tourplanner.bl;

import at.fhtw.tourplanner.bl.model.SearchInputParserOutput;
import at.fhtw.tourplanner.bl.model.TourItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchInputParser {

    public interface StringFilter {
        boolean filterList(TourItem tourItem, String value);
    }

    public interface NumericFilter {
        boolean filterList(String paramName, TourItem tourItem, String value, char searchSymbol) throws IllegalArgumentException;
    }

    private final Map<String, StringFilter> tourStringFields = new HashMap<String, StringFilter>();

    private final Map<String, NumericFilter> tourNumericFields = new HashMap<String, NumericFilter>();

    //region string filter functions
    public boolean filterName(TourItem tourItem, String value) {
        return tourItem.getName().equalsIgnoreCase(value);
    }

    public boolean filterDescription(TourItem tourItem, String value) {
        return tourItem.getDescription().equalsIgnoreCase(value);
    }

    public boolean filterStart(TourItem tourItem, String value) {
        return tourItem.getFrom().equalsIgnoreCase(value);
    }

    public boolean filterDestination(TourItem tourItem, String value) {
        return tourItem.getTo().equalsIgnoreCase(value);
    }

    public boolean filterTransport(TourItem tourItem, String value) {
        return tourItem.getTransportType().equalsIgnoreCase(value);
    }
    //endregion

    //region numeric filter functions
    public boolean filterDistance(String paramName, TourItem tourItem, String paramValue, char filterSymbol) throws IllegalArgumentException {

        Double paramValueDouble;

        try {
            paramValueDouble = Double.valueOf(paramValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The parameter value for '" + paramName + "' is invalid");
        }

        Double roundedTourDistance = Double.valueOf(Math.round(tourItem.getTourDistanceKilometers() * 100));
        roundedTourDistance = roundedTourDistance/100;


        return compareNumberValues(paramValueDouble, roundedTourDistance, filterSymbol);
    }

    //filterTime
    public boolean filterTime(String paramName, TourItem tourItem, String paramValue, char filterSymbol) throws IllegalArgumentException {
        //tourItem.getEstimatedTimeSeconds();
        Long paramValueLong;

        try {
            paramValueLong = Long.valueOf(paramValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The parameter value for '" + paramName + "' is invalid");
        }

        return compareNumberValues(paramValueLong, tourItem.getEstimatedTimeSeconds(), filterSymbol);

    }

    public boolean filterPopularity(String paramName, TourItem tourItem, String paramValue, char filterSymbol) throws IllegalArgumentException {

        Integer paramValueInt;

        try {
            paramValueInt = Integer.valueOf(paramValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The parameter value for '" + paramName + "' is invalid");
        }

        return compareNumberValues(paramValueInt, tourItem.getTourStats().getPopularity(), filterSymbol);

    }

    public boolean filterChildFriendliness(String paramName, TourItem tourItem, String paramValue, char filterSymbol) throws IllegalArgumentException {

        Double paramValueDouble;

        try {
            paramValueDouble = Double.valueOf(paramValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The parameter value for '" + paramName + "' is invalid");
        }

        Double roundedChildFriendliness = Double.valueOf(Math.round(tourItem.getTourStats().getChildFriendliness() * 100));
        roundedChildFriendliness = roundedChildFriendliness/100;

        return compareNumberValues(paramValueDouble, roundedChildFriendliness, filterSymbol);

    }

    public boolean filterAverageTime(String paramName, TourItem tourItem, String paramValue, char filterSymbol) throws IllegalArgumentException {

        Double paramValueDouble;

        try {
            paramValueDouble = Double.valueOf(paramValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The parameter value for '" + paramName + "' is invalid");
        }

        Double roundedAvgTime = Double.valueOf(Math.round(tourItem.getTourStats().getAverageTime() * 100));
        roundedAvgTime = roundedAvgTime/100;

        return compareNumberValues(paramValueDouble, roundedAvgTime, filterSymbol);

    }

    public boolean filterAverageRating(String paramName, TourItem tourItem, String paramValue, char filterSymbol) throws IllegalArgumentException {

        Double paramValueDouble;

        try {
            paramValueDouble = Double.valueOf(paramValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The parameter value for '" + paramName + "' is invalid");
        }

        Double roundedAvgRating = Double.valueOf(Math.round(tourItem.getTourStats().getAverageRating() * 100));
        roundedAvgRating = roundedAvgRating/100;

        return compareNumberValues(paramValueDouble, roundedAvgRating, filterSymbol);

    }

    public boolean filterAverageDifficulty(String paramName, TourItem tourItem, String paramValue, char filterSymbol) throws IllegalArgumentException {

        Double paramValueDouble;

        try {
            paramValueDouble = Double.valueOf(paramValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The parameter value for '" + paramName + "' is invalid");
        }

        Double roundedAvgDifficulty = Double.valueOf(Math.round(tourItem.getTourStats().getAverageDifficulty() * 100));
        roundedAvgDifficulty = roundedAvgDifficulty/100;

        return compareNumberValues(paramValueDouble, roundedAvgDifficulty, filterSymbol);
    }

    private boolean compareNumberValues(Double searchValue, Double storedValue, char filterSymbol) {

        if (filterSymbol == '=') {
            return searchValue.equals(storedValue);
        }

        if (filterSymbol == '>') {
            return storedValue > searchValue;
        }

        if (filterSymbol == '<') {
            return storedValue < searchValue;
        }

        return true;

    }

    private boolean compareNumberValues(Long searchValue, Long storedValue, char filterSymbol) {

        if (filterSymbol == '=') {
            return searchValue.equals(storedValue);
        }

        if (filterSymbol == '>') {
            return storedValue > searchValue;
        }

        if (filterSymbol == '<') {
            return storedValue < searchValue;
        }

        return true;

    }

    private boolean compareNumberValues(Integer searchValue, Integer storedValue, char filterSymbol) {

        if (filterSymbol == '=') {
            return searchValue.equals(storedValue);
        }

        if (filterSymbol == '>') {
            return storedValue > searchValue;
        }

        if (filterSymbol == '<') {
            return storedValue < searchValue;
        }

        return true;

    }
    //endregion

    public SearchInputParser() {

        //populate string fields map
        this.tourStringFields.put("name", this::filterName);
        this.tourStringFields.put("description", this::filterDescription);
        this.tourStringFields.put("start", this::filterStart);
        this.tourStringFields.put("destination", this::filterDestination);
        this.tourStringFields.put("transport", this::filterTransport);

        //populate numeric fields map
        this.tourNumericFields.put("distance", this::filterDistance);
        this.tourNumericFields.put("time", this::filterTime);
        this.tourNumericFields.put("popularity", this::filterPopularity);
        this.tourNumericFields.put("childF", this::filterChildFriendliness);
        this.tourNumericFields.put("avgTime", this::filterAverageTime);
        this.tourNumericFields.put("avgRating", this::filterAverageRating);
        this.tourNumericFields.put("avgDifficulty", this::filterAverageDifficulty);
    }

    public SearchInputParserOutput parseSearchInput(String rawInput) {

        String fullTextInput = this.extractSearchText(rawInput);

        int indexOfFirstParam = rawInput.indexOf('@');

        if (indexOfFirstParam == -1) {
            return new SearchInputParserOutput(fullTextInput, null);
        } else {

            String paramSubString = rawInput.substring(indexOfFirstParam);

            List<String> searchParams = this.extractParams(paramSubString);

            return new SearchInputParserOutput(fullTextInput, searchParams);
        }

    }

    private List<String> extractParams(String paramSubString) {

        List<String> paramsList = new ArrayList<>();

        // find all starts of the params
        List<Integer> paramsStartIndexes = new ArrayList<>();

        for (int i = 0; i < paramSubString.length(); i++) {

            if (paramSubString.charAt(i) == '@') {
                paramsStartIndexes.add(i);
            }

        }

        //iterate over the start indexes of the params
        for (int i = 0; i < paramsStartIndexes.size(); i++) {

            if (i == paramsStartIndexes.size() - 1) {

                paramsList.add(paramSubString.substring(paramsStartIndexes.get(i)).trim());

            } else {

                paramsList.add(paramSubString.substring(paramsStartIndexes.get(i), paramsStartIndexes.get(i + 1)).trim());

            }

        }

        return paramsList;

    }

    private String extractSearchText(String searchInput) {

        StringBuilder sb = new StringBuilder();

        int indexOfFirstParam = searchInput.indexOf('@');

        //if no additional search params are defined
        if (indexOfFirstParam == -1) {

            return searchInput.trim();

        } else {

            for (int i = 0; i < indexOfFirstParam; i++) {

                sb.append(searchInput.charAt(i));

            }

            return sb.toString().trim();

        }

    }

    public ObservableList<TourItem> applyFilterParams(List<String> params, ObservableList<TourItem> fullTextResult) throws IllegalArgumentException {

        for (String parameter : params) {

            int indexOfEquals = parameter.indexOf('=');

            if (indexOfEquals != -1) {

                String paramName = parameter.substring(1, indexOfEquals);

                String paramValue = parameter.substring(indexOfEquals + 1).trim();

                fullTextResult = this.applyFilter(paramName, paramValue, fullTextResult, '=');

            } else {

                int indexOfLess = parameter.indexOf('<');

                if (indexOfLess != -1) {

                    String paramName = parameter.substring(1, indexOfLess);

                    String paramValue = parameter.substring(indexOfLess + 1).trim();

                    fullTextResult = this.applyFilter(paramName, paramValue, fullTextResult, '<');

                } else {

                    int indexOfMore = parameter.indexOf('>');

                    if (indexOfMore != -1) {


                        String paramName = parameter.substring(1, indexOfMore);

                        String paramValue = parameter.substring(indexOfMore + 1).trim();

                        fullTextResult = this.applyFilter(paramName, paramValue, fullTextResult, '>');

                    } else {
                        throw new IllegalArgumentException("Parameter malformed, '=' or '<' or '>' needed");
                    }

                }

            }

        }

        return fullTextResult;

    }

    private ObservableList<TourItem> applyFilter(String paramName, String paramValue, ObservableList<TourItem> fullTextResult, char symbol) throws IllegalArgumentException {
        StringFilter stringFilter = this.tourStringFields.get(paramName);

        boolean noStringFilter = false;

        if (paramValue.isBlank()) {
            throw new IllegalArgumentException("Parameter value for parameter '" + paramName + "' cannot be blank");
        }

        if (stringFilter != null) {

            return this.filterListString(paramValue, stringFilter, fullTextResult);

        }

        boolean noNumericFilter = false;

        NumericFilter numericFilter = this.tourNumericFields.get(paramName);

        if (numericFilter != null) {

            return this.filterListNumeric(paramName, symbol, paramValue, numericFilter, fullTextResult);

        }

        if (numericFilter == null && stringFilter == null) {
            throw new IllegalArgumentException("Parameter: '" + paramName + "' is invalid");
        }

        return fullTextResult;

    }

    private ObservableList<TourItem> filterListString(String paramValue, StringFilter stringFilter, ObservableList<TourItem> fullTextResult) throws IllegalArgumentException {

        return FXCollections.observableList(fullTextResult.stream().filter(tourItem -> stringFilter.filterList(tourItem, paramValue)).toList());

    }

    private ObservableList<TourItem> filterListNumeric(String paramName, char symbol, String paramValue, NumericFilter numericFilter, ObservableList<TourItem> fullTextResult) throws IllegalArgumentException {

        return FXCollections.observableList(fullTextResult.stream().filter(tourItem -> numericFilter.filterList(paramName, tourItem, paramValue, symbol)).toList());

    }
}
