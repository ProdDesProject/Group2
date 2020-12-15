package com.Group2.Heartbeat.ui.SleepHist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Group2.Heartbeat.NightResult;

public class SleepHistViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    public MutableLiveData<Integer> latestSleepSession;
    public MutableLiveData<NightResult> nightResult;

    public SleepHistViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("14th Dec 2020");
        latestSleepSession = new MutableLiveData<>();
        nightResult = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}