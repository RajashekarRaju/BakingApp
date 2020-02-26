package com.developersbreach.bakingapp.utils;

import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Class with methods for formatting string data from JSON.
 */
public class FormatUtils {

    /**
     * @param totalDuration contains value of type long which has to format into seconds and minutes.
     * @return String from type long in format of SS:MM to show user friendly data.
     */
    public static String getStringTimeFormat(long totalDuration) {
        return String.format(Locale.getDefault(),
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(totalDuration) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalDuration)),
                TimeUnit.MILLISECONDS.toSeconds(totalDuration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalDuration)));
    }

    /**
     * @param name has value which has to be formatted to show first letter in capital.
     * @return Character string after formatting to capitalize first letter from given string name.
     */
    public static String capitalize(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    /**
     * @param videoUrl contains URL which of type video.
     * @return using {@link MediaMetadataRetriever} and key METADATA_KEY_DURATION get duration of
     * passed string URL.
     */
    public static long durationRetriever(String videoUrl) {
        final long videoDuration;
        try (MediaMetadataRetriever retriever = new MediaMetadataRetriever()) {
            retriever.setDataSource(videoUrl, new HashMap<>());
            videoDuration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            retriever.release();
        }
        return videoDuration;
    }
}
