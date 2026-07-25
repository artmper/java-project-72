package hexlet.code.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DTFormatter {
    public static String format(Instant dt) {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ZonedDateTime.ofInstant(dt, ZoneId.systemDefault()).format(formatter);
    }
}
