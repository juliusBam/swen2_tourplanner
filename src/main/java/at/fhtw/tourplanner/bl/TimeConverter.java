package at.fhtw.tourplanner.bl;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConverter {

    public static String convertTimeStampToString(String dateFormat, int timeStamp) {
        Date date = new Date(timeStamp * 1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

}
