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

@GlideModule
public class AppGlide extends AppGlideModule {

    public void applyOptions(@NonNull Context context, GlideBuilder builder) {
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
        builder.setDefaultRequestOptions(new RequestOptions().frame(1000));
        builder.setDefaultRequestOptions(new RequestOptions().downsample(DownsampleStrategy.AT_MOST));
        builder.setDefaultRequestOptions(new RequestOptions().centerCrop());
        builder.setDefaultRequestOptions(new RequestOptions().encodeFormat(Bitmap.CompressFormat.JPEG));
        builder.setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_recipe_icon));
    }
}
