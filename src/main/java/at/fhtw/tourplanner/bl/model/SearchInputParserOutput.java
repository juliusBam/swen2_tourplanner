package at.fhtw.tourplanner.bl.model;

import java.util.List;

public record SearchInputParserOutput(String searchInput, List<String> params) {
}
