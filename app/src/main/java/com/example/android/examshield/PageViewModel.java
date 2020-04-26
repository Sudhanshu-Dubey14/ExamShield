package com.example.android.examshield;

import android.content.Context;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private Context mcontext;
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            if (input == 1) {
                return mcontext.getText(R.string.instructions).toString();
            } else {
                return "Not one";
            }
        }
    });

    public void setIndex(Context context, int index) {
        mcontext = context;
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}