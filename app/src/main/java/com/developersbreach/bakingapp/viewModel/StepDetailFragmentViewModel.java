package com.developersbreach.bakingapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.developersbreach.bakingapp.model.Steps;

public class StepDetailFragmentViewModel extends AndroidViewModel {

    private final MutableLiveData<Steps> _mMutableStepDetail;
    private final String _mRecipeName;
    private long _mPlayBackPosition;
    private int _mCurrentWindow;

    public MutableLiveData<Steps> stepDetails() {
        return _mMutableStepDetail;
    }

    public StepDetailFragmentViewModel(@NonNull Application application, Steps step, String recipeName) {
        super(application);
        _mRecipeName = recipeName;
        _mMutableStepDetail = new MutableLiveData<>();
        _mMutableStepDetail.postValue(step);
    }

    public String getRecipeName() {
        return _mRecipeName;
    }

    public long getPlayBackPosition() {
        return _mPlayBackPosition;
    }

    public int getCurrentWindow() {
        return _mCurrentWindow;
    }

    public void setPlayBackPosition(long playBackPosition) {
        this._mPlayBackPosition = playBackPosition;
    }

    public void setCurrentWindow(int mCurrentWindow) {
        this._mCurrentWindow = mCurrentWindow;
    }
}
