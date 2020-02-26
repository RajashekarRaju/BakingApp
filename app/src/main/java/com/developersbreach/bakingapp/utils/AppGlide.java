package com.developersbreach.bakingapp.utils;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.developersbreach.bakingapp.R;

/**
 * {@link AppGlideModule} extension is must for Applications that wish to use integration libraries
 * and/or Glide’s API.
 */
@GlideModule
public class AppGlide extends AppGlideModule {

    /**
     * We use this as default request options when ever any fragments or classes uses Glide module.
     * These default request options can be over written by any fragment in it's own method.
     *
     * @param context to get resources for any fragment view or resources.
     * @param builder use only one builder and can be called many times to add request options.
     */
    public void applyOptions(@NonNull Context context, GlideBuilder builder) {
        // Use PREFER_RGB_565 as default because consumes it 50% less resources than PREFER_ARGB_8888.
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
        // Get thumbnail only at first second of any video frame.
        builder.setDefaultRequestOptions(new RequestOptions().frame(1000));
        // With this request we are trying to reduce size of resource at most.
        builder.setDefaultRequestOptions(new RequestOptions().downsample(DownsampleStrategy.AT_MOST));
        // Apply centerCrop filter to fit thumbnails properly into views.
        builder.setDefaultRequestOptions(new RequestOptions().centerCrop());
        // Prefer encode format JPEG.
        builder.setDefaultRequestOptions(new RequestOptions().encodeFormat(Bitmap.CompressFormat.JPEG));
        // Default PlaceHolder drawable until actual thumbnail loads from URL.
        builder.setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_recipe_icon));
    }
}
