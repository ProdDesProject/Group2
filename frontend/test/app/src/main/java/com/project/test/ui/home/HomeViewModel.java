package com.project.test.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mainText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("this is a Home page");
        mainText = new MutableLiveData<>();
        mainText.setValue("testingiingngngng");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getText2() {
        return mainText;
    }
}