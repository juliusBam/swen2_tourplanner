package at.fhtw.tourplanner.bl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeConverterTest {

    @ParameterizedTest
    @MethodSource("provideValues")
    void testConvertTimeStampToString(String format, int timeStamp, String expected) {
        String result = TimeConverter.convertTimeStampToString(format, timeStamp);
        assertEquals(expected, result);
    }


    static Stream<Arguments> provideValues() {
        return Stream.of(
                // recent date
                org.junit.jupiter.params.provider.Arguments.arguments("dd/MM/yyyy", 1672614000, "02/01/2023"),
                // date in the past
                org.junit.jupiter.params.provider.Arguments.arguments("dd/MM/yyyy", 922143600, "23/03/1999"),
                // date in the future
                org.junit.jupiter.params.provider.Arguments.arguments("dd/MM/yyyy", 2112130800, "06/12/2036"),
                // varying output format
                org.junit.jupiter.params.provider.Arguments.arguments("dd-MM-yyyy", 949273200, "31-01-2000"),
                org.junit.jupiter.params.provider.Arguments.arguments("MM-dd-yyyy", 949273200, "01-31-2000"),
                org.junit.jupiter.params.provider.Arguments.arguments("yyyy-MM-dd", 949273200, "2000-01-31"),
                org.junit.jupiter.params.provider.Arguments.arguments("MM-dd-yy", 949273200, "01-31-00")
        );
    }

}