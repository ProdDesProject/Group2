package com.Group2.Heartbeat.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.Group2.Heartbeat.MainActivity;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mainText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mainText = new MutableLiveData<>();
        mText.setValue(MainActivity.getRestMessage());
        mainText.setValue("Message");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getText2() {
        return mainText;
    }
}