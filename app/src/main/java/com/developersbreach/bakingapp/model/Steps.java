package com.developersbreach.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.developersbreach.bakingapp.R;
import com.developersbreach.bakingapp.bindingAdapter.StepsListBindingAdapter;
import com.developersbreach.bakingapp.network.JsonUtils;


/**
 * Data class for getting Steps, which returns objects of step properties.
 * We pass each property into ArrayList of Steps from class {@link JsonUtils#fetchStepsJsonData(String, int)}
 * and these properties will be accessed by binding objects as static fields.
 * <p>
 * The list of properties are set to RecyclerView in class {@link StepsListBindingAdapter}
 */
public class Steps implements Parcelable {

    // Step property of type int with unique id for step.
    private final int mStepsId;
    // Step property of type string has short description for step recipe.
    private final String mStepsShortDescription;
    // Step property of type string has description for step recipe.
    private final String mStepsDescription;
    // Step property of type URL string of video used to fetch thumbnail for each step in recipe if
    // the thumbnail url is empty.
    private final String mVideoUrl;
    // Step property of type URL string for getting thumbnail for each step recipe in recipe.
    private final String mThumbnailUrl;

    // Class constructor for making data class into reusable objects of steps.
    public Steps(int id, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
        this.mStepsId = id;
        this.mStepsShortDescription = shortDescription;
        this.mStepsDescription = description;
        this.mVideoUrl = videoUrl;
        this.mThumbnailUrl = thumbnailUrl;
    }

    /**
     * @return stepId type int for passing as an argument to load specific step detail of selected recipe.
     */
    public int getStepsId() {
        return mStepsId;
    }

    /**
     * @return short description for step, string value for binding view in {@link R.layout#item_step}
     */
    public String getStepsShortDescription() {
        return mStepsShortDescription;
    }

    /**
     * @return description for step, string value for binding view in {@link R.layout#item_step}
     */
    public String getStepsDescription() {
        return mStepsDescription;
    }

    /**
     * @return videoURL for step, string value for binding view of type thumbnail in {@link R.layout#item_step}
     */
    public String getVideoUrl() {
        return mVideoUrl;
    }

    /**
     * @return thumbnail URL for step, string value for binding view in {@link R.layout#item_step}
     */
    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    private Steps(Parcel in) {
        mStepsId = in.readInt();
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
        dest.writeInt(mStepsId);
        dest.writeString(mStepsShortDescription);
        dest.writeString(mStepsDescription);
        dest.writeString(mVideoUrl);
        dest.writeString(mThumbnailUrl);
    }
}
