package com.developersbreach.bakingapp.utils;

import android.media.MediaMetadataRetriever;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FormatUtils {

    public static String getStringTimeFormat(long totalDuration) {
        return String.format(Locale.getDefault(),
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(totalDuration) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalDuration)),
                TimeUnit.MILLISECONDS.toSeconds(totalDuration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalDuration)));
    }

    public static String capitalize(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static long thumbnailRetriever(String videoUrl) {
        final long videoDuration;
        try (MediaMetadataRetriever retriever = new MediaMetadataRetriever()) {
            retriever.setDataSource(videoUrl, new HashMap<>());
            videoDuration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            retriever.release();
        }
        return videoDuration;
    }
}
