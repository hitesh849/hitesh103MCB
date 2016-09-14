package org.byteclues.lib.view;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

import org.byteclues.lib.R;
import org.byteclues.lib.init.Env;
import org.byteclues.lib.model.BasicModel;

import java.util.Observer;

/**
 * Created by admin on 2/15/2016.
 */
public abstract class AbstractActionBarActivity extends ActionBarActivity implements Observer
{
    protected BasicModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = getModel();
        if (model != null)
            model.addObserver(this);
        Env.currentActivity=this;

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
        Env.currentActivity=this;
    }
}
