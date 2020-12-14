package com.Group2.Heartbeat.ui.SleepHist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SleepHistViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SleepHistViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("14th Dec 2020");
    }

    public LiveData<String> getText() {
        return mText;
    }
}