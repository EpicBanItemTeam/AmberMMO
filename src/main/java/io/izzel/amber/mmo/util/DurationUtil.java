package io.izzel.amber.mmo.util;

import org.spongepowered.api.util.Coerce;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationUtil {

    private static final Pattern DURATION = Pattern.compile("^(?:(\\d+)w)?(?:(\\d+)d)?(?:(\\d+)h)?(?:(\\d+)m)?(?:(\\d+(\\.\\d+)?)s)?$");

    public static long parseToMillis(String duration) throws Exception {
        Matcher matcher = DURATION.matcher(duration);
        if (matcher.find()) {
            int w = Coerce.toInteger(matcher.group(1));
            int d = Coerce.toInteger(matcher.group(2));
            int h = Coerce.toInteger(matcher.group(3));
            int m = Coerce.toInteger(matcher.group(4));
            double s = Coerce.toDouble(matcher.group(5));
            return Duration.ofDays(w * 7 + d).plusHours(h).plusMinutes(m).plusMillis((long) (s * 1000)).toMillis();
        }
        throw new Exception();
    }
}
