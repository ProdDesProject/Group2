package com.Group2.Heartbeat.ui.SleepHist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SleepHistViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SleepHistViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the Sleep History page");
    }

    public LiveData<String> getText() {
        return mText;
    }
}