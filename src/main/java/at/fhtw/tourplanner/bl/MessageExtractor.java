package at.fhtw.tourplanner.bl;

public class MessageExtractor {

    private final String msgTemplateString = "messageTemplate";

    public String extractMessageTemplate(String errorMsg) {

        int indexOfMessageTemplate = errorMsg.indexOf(this.msgTemplateString);

        StringBuilder content = new StringBuilder();

        if (indexOfMessageTemplate != -1) {

            // +1 because there is an equal sign
            int startingIndex = indexOfMessageTemplate + this.msgTemplateString.length() + 1;

            int numberOfQuotationMarks = 0;

            for (int i = startingIndex; i < errorMsg.length(); i++) {

                if (errorMsg.charAt(i) == '\'') {

                    ++numberOfQuotationMarks;

                    if (numberOfQuotationMarks == 2) {
                        break;
                    }
                    continue;
                }

                content.append(errorMsg.charAt(i));

            }

        }

        return content.toString();
    }

}
