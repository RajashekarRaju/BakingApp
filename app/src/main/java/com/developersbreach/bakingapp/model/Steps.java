package com.developersbreach.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Steps implements Parcelable {

    private final String mStepsId;
    private final String mStepsShortDescription;
    private final String mStepsDescription;
    private final String mVideoUrl;
    private final String mThumbnailUrl;

    public Steps(String id, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
        this.mStepsId = id;
        this.mStepsShortDescription = shortDescription;
        this.mStepsDescription = description;
        this.mVideoUrl = videoUrl;
        this.mThumbnailUrl = thumbnailUrl;
    }

    public String getStepsId() {
        return mStepsId;
    }

    public String getStepsShortDescription() {
        return mStepsShortDescription;
    }

    public String getStepsDescription() {
        return mStepsDescription;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    private Steps(Parcel in) {
        mStepsId = in.readString();
        mStepsShortDescription = in.readString();
        mStepsDescription = in.readString();
        mVideoUrl = in.readString();
        mThumbnailUrl = in.readString();
    }

    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mStepsId);
        dest.writeString(mStepsShortDescription);
        dest.writeString(mStepsDescription);
        dest.writeString(mVideoUrl);
        dest.writeString(mThumbnailUrl);
    }
}
