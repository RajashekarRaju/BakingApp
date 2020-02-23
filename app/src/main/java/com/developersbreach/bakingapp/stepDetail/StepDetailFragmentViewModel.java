package com.developersbreach.bakingapp.stepDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.developersbreach.bakingapp.model.Steps;

public class StepDetailFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Steps> _mMutableStepDetail;
    private String _mRecipeName;
    private long _mPlayBackPosition;
    private int _mCurrentWindow;

    MutableLiveData<Steps> stepDetails() {
        return _mMutableStepDetail;
    }

    StepDetailFragmentViewModel(@NonNull Application application, Steps step, String recipeName) {
        super(application);
        _mRecipeName = recipeName;
        _mMutableStepDetail = new MutableLiveData<>();
        _mMutableStepDetail.postValue(step);
    }

    String getRecipeName() {
        return _mRecipeName;
    }

    long getPlayBackPosition() {
        return _mPlayBackPosition;
    }

    int getCurrentWindow() {
        return _mCurrentWindow;
    }

    void setPlayBackPosition(long playBackPosition) {
        this._mPlayBackPosition = playBackPosition;
    }

    void setCurrentWindow(int mCurrentWindow) {
        this._mCurrentWindow = mCurrentWindow;
    }
}
