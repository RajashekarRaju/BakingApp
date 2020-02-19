package com.developersbreach.bakingapp.utils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class StringUtils {

    public static String getStringTimeFormat(long totalDuration) {
        return String.format(Locale.getDefault(),
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(totalDuration) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalDuration)),
                TimeUnit.MILLISECONDS.toSeconds(totalDuration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalDuration)));
    }
}
