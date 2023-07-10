package at.fhtw.tourplanner.bl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageExtractorTest {

    private final MessageExtractor messageExtractor;

    public MessageExtractorTest() {
        this.messageExtractor = new MessageExtractor();
    }

    @Test
    public void testNoMatch() {

        //arrange
        String input = "I am a cool error with many informations... > message error Template";

        //act
        String output = messageExtractor.extractMessageTemplate(input);

        //assert
        assertEquals("", output);
    }

    @Test
    public void testCorrectMatch() {

        //arrange
        String input = "I am a cool error with many dangerous informations who should not be shown to the user messageTemplate='this is the custom message'";

        //act
        String output = messageExtractor.extractMessageTemplate(input);

        //assert
        assertEquals("this is the custom message", output);
    }

}