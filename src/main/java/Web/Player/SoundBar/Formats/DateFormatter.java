package Web.Player.SoundBar.Formats;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public class DateFormatter {

    private static final String TIME_ZONE = "Europe/Kyiv";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatTime(LocalDateTime time) {
        ZonedDateTime zonedDateTime = time.atZone(ZoneId.of(TIME_ZONE));
        return formatter.format(zonedDateTime);
    }
}