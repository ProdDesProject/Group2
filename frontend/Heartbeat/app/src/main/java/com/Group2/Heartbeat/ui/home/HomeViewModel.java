package com.Group2.Heartbeat.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.Group2.Heartbeat.MainActivity;
import com.Group2.Heartbeat.NightResult;

public class HomeViewModel extends ViewModel {

    public MutableLiveData<String> mText;
    public MutableLiveData<String> mainText;
    public MutableLiveData<NightResult> nightResult;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mainText = new MutableLiveData<>();
        this.nightResult = new MutableLiveData<>();
    }
}