package org.byteclues.lib.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import org.byteclues.lib.R;
import org.byteclues.lib.init.Env;
import org.byteclues.lib.model.BasicModel;

import java.util.Observer;


public abstract class AbstractFragmentActivity extends FragmentActivity implements Observer {
    protected BasicModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = getModel();
        if (model != null)
            model.addObserver(this);
        Env.currentActivity = this;
        onCreatePost(savedInstanceState);
    }

    protected abstract void onCreatePost(Bundle savedInstanceState);

    protected abstract BasicModel getModel();

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Env.currentActivity = this;
        Env.APP_STATE = Env.applicationState.FOREGROUND;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.dispatchTouchEvent(ev);
    }
}
