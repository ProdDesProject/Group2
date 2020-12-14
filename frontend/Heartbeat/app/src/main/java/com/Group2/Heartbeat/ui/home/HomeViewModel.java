package com.Group2.Heartbeat.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.Group2.Heartbeat.MainActivity;
import com.Group2.Heartbeat.NightResult;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mainText;
    public MutableLiveData<NightResult> nightResult;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mainText = new MutableLiveData<>();
        mText.setValue(MainActivity.getRestMessage());
        mainText.setValue("Message");
        this.nightResult = new MutableLiveData<>();
    }

    public void setNightResult(MutableLiveData<NightResult> nightResult) {
        this.nightResult = nightResult;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getText2() {
        return mainText;
    }

//    public LiveData<NightResult> getNightResult(){return nightResult;};
}