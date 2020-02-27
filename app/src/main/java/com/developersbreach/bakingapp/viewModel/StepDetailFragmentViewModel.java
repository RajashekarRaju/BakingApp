package com.developersbreach.bakingapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.developersbreach.bakingapp.model.Steps;
import com.developersbreach.bakingapp.view.stepDetail.StepDetailFragment;
import com.developersbreach.bakingapp.view.stepList.StepsFragment;
import com.developersbreach.bakingapp.viewModel.factory.StepDetailFragmentViewModelFactory;

/**
 * We are extending with AndroidViewModel instead of ViewModel, because AndroidViewModel is aware of
 * using application context helps to get or access resources we need.
 * <p>
 * This class is responsible for preparing and managing the data for fragment class
 * {@link StepDetailFragment}
 * This ViewModel expose the data IngredientsList via {@link LiveData} and fragment observes the changes.
 * <p>
 * This ViewModel instance is created by factory {@link StepDetailFragmentViewModelFactory}
 * <p>
 * This helps us save fragment data and keeps UI simple.
 * This class performs background operations to fetch json data from internet.
 */
public class StepDetailFragmentViewModel extends AndroidViewModel {

    /**
     * This field is encapsulated, we used {@link MutableLiveData} because when the data is being
     * changed we will be updating StepsDetail with new values. And any externally exposed LiveData
     * can observe this changes.
     */
    private MutableLiveData<Steps> _mMutableStepDetail;

    /**
     * Get name of the recipe from selected recipes by recipe id it to StepDetails.
     */
    private final String mRecipeName;

    /**
     * Get position of the ExoPlayer Bar to start from the same position.
     */
    private long mPlayBackPosition;

    /**
     * Get ExoPlayer state to manage player from current window.
     */
    private int mCurrentWindow;

    /**
     * @return Exposed {@link LiveData} object of List of {@link Steps} externally allowing fragment
     * to observe changes. Data is observed once changes will be done internally.
     */
    public LiveData<Steps> stepDetails() {
        return _mMutableStepDetail;
    }

    /**
     * @param application provides application context for ViewModel.
     * @param step        parcel Step object with data for user selected step from {@link StepsFragment}
     * @param recipeName  get name of the recipe to show in {@link StepDetailFragment}
     */
    public StepDetailFragmentViewModel(@NonNull Application application, Steps step, String recipeName) {
        super(application);
        mRecipeName = recipeName;
        // Call this method to get new StepDetail values with step ID.
        getMutableStepDetailData(step);
    }

    /**
     * Create a new {@link MutableLiveData} after checking if is is empty, otherwise no need make
     * changes by adding new values.
     *
     * @param step has data for user selected Step with id.
     */
    private void getMutableStepDetailData(Steps step) {
        if (_mMutableStepDetail == null) {
            _mMutableStepDetail = new MutableLiveData<>();
            // Add list to internally exposed data of StepDetails by calling postValue.
            _mMutableStepDetail.postValue(step);
        }
    }

    /**
     * @return name of the recipe fro user selected Step with Step and Recipe ID's.
     */
    public String getRecipeName() {
        return mRecipeName;
    }

    /**
     * @return position of ExoPlayer Bar position.
     */
    public long getPlayBackPosition() {
        return mPlayBackPosition;
    }

    /**
     * @return gets the ExoPLayer state of the current window.
     */
    public int getCurrentWindow() {
        return mCurrentWindow;
    }

    /**
     * @param playBackPosition get the last position and save position to new position
     */
    public void setPlayBackPosition(long playBackPosition) {
        this.mPlayBackPosition = playBackPosition;
    }

    /**
     * @param currentWindow get state of current window and save window to new state.
     */
    public void setCurrentWindow(int currentWindow) {
        this.mCurrentWindow = currentWindow;
    }
}
