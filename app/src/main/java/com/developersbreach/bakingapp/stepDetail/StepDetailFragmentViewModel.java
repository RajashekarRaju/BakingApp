package com.developersbreach.bakingapp.stepDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.developersbreach.bakingapp.model.Steps;

public class StepDetailFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Steps> mMutableStepDetail;
    private String mRecipeName;
    private long mPlayBackPosition;
    private int mCurrentWindow;

    StepDetailFragmentViewModel(@NonNull Application application, Steps step, String recipeName) {
        super(application);
        mRecipeName = recipeName;
        mMutableStepDetail = new MutableLiveData<>();
        mMutableStepDetail.postValue(step);
    }

    MutableLiveData<Steps> getStepDetailData() {
        return mMutableStepDetail;
    }

    String getRecipeName() {
        return mRecipeName;
    }

    long getPlayBackPosition() {
        return mPlayBackPosition;
    }

    int getCurrentWindow() {
        return mCurrentWindow;
    }

    void setPlayBackPosition(long playBackPosition) {
        this.mPlayBackPosition = playBackPosition;
    }

    void setCurrentWindow(int mCurrentWindow) {
        this.mCurrentWindow = mCurrentWindow;
    }
}
