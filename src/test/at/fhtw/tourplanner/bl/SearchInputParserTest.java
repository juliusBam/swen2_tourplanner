package at.fhtw.tourplanner.bl;

import at.fhtw.tourplanner.bl.model.SearchInputParserOutput;
import at.fhtw.tourplanner.bl.model.TourItem;
import at.fhtw.tourplanner.bl.model.TourStats;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchInputParserTest {

    SearchInputParser searchInputParser;
    private final ObservableList<TourItem> tourDataset;

    private final TourItem tourItem1;

    private final TourItem tourItem2;

    private final TourItem tourItem3;

    public SearchInputParserTest() {
        //arrange
        searchInputParser = new SearchInputParser();
        this.tourItem1 = new TourItem(
                1L, "Tour 1 name", "Tour 1 description", "start location", "destination location",
                "bicycle", 150.54D, 3600L, "Bounding box", null
        );
        this.tourItem1.setTourStats(
                new TourStats(
                        1,
                        0.8D,
                        3000.73D,
                        6.43D,
                        4.54D
                )
        );

        this.tourItem2 = new TourItem(
                2L, "Tour 2 name", "Tour 2 description", "start location", "destination location 2",
                "car", 89.21D, 5000L, "Bounding box", null
        );
        this.tourItem2.setTourStats(
                new TourStats(
                        2,
                        1.5D,
                        6000.18D,
                        3.21D,
                        8.32D
                )
        );

        this.tourItem3 = new TourItem(
                3L, "Tour 3 name", "Tour 3 description", "start location", "destination location 3",
                "pedestrian", 200.43D, 2000L, "Bounding box", null
        );
        this.tourItem3.setTourStats(
                new TourStats(
                        3,
                        2.0D,
                        2150.32D,
                        2.50D,
                        9.80D
                )
        );

        this.tourDataset = FXCollections.observableList(List.of(tourItem1, tourItem2, tourItem3));
    }
    @Test
    public void testParamExtraction() {

        //arrange
        final String fullTextSearchString = "test search string";

        final String nameFilterParam = "@name=";
        final String nameFilterValue = "name value";
        final String descriptionFilterParam = "@description=";
        final String descriptionFilterValue = "description value";

        final String searchInput = fullTextSearchString + nameFilterParam + nameFilterValue + descriptionFilterParam + descriptionFilterValue;

        //act
        SearchInputParserOutput parsingOutput = searchInputParser.parseSearchInput(searchInput);

        //assert
        assertEquals(fullTextSearchString, parsingOutput.searchInput());
        assertTrue(parsingOutput.params().contains(nameFilterParam + nameFilterValue));
        assertTrue(parsingOutput.params().contains(descriptionFilterParam + descriptionFilterValue));
    }

    @Test
    public void testSingleFilter() {

        final String searchInput = "@description=Tour 3 description";

        final SearchInputParserOutput parserOutput = searchInputParser.parseSearchInput(searchInput);


        ObservableList<TourItem> filteredTours = searchInputParser.applyFilterParams(parserOutput.params(), this.tourDataset);

        //assert
        assertEquals(1, filteredTours.size());
        assertTrue(filteredTours.contains(this.tourItem3));

    }

    @Test
    public void testMultipleFilters() {

        //arrange
        final String searchInput = "@start=sTart loCation @distance<200";

        final SearchInputParserOutput parserOutput = this.searchInputParser.parseSearchInput(searchInput);

        //act
        ObservableList<TourItem> filteredTours = searchInputParser.applyFilterParams(parserOutput.params(), this.tourDataset);

        //assert
        assertEquals(2, filteredTours.size());
        assertTrue(filteredTours.contains(this.tourItem2));
        assertTrue(filteredTours.contains(this.tourItem1));
    }

    @Test
    public void testIllegalParamName() {

        //arrange
        final String searchInput = "@startPoint=sTart loCation";

        final SearchInputParserOutput parserOutput = this.searchInputParser.parseSearchInput(searchInput);

        //act - assert
        assertThrows(IllegalArgumentException.class, () -> searchInputParser.applyFilterParams(parserOutput.params(), this.tourDataset));

    }

    @Test
    public void testIllegalParamValue() {
        //act - assert
        final String searchInput = "@distance=b21ab";

        final SearchInputParserOutput parserOutput = this.searchInputParser.parseSearchInput(searchInput);

        //act - assert
        assertThrows(IllegalArgumentException.class, () -> searchInputParser.applyFilterParams(parserOutput.params(), this.tourDataset));
    }

}