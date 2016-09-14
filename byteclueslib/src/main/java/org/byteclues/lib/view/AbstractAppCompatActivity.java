package org.byteclues.lib.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.byteclues.lib.custom.AppErrorView;
import org.byteclues.lib.init.Env;
import org.byteclues.lib.model.BasicModel;

import java.util.Observer;

public abstract class AbstractAppCompatActivity extends AppCompatActivity implements Observer {

    protected BasicModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            Env.currentActivity=this;
            model = getModel();
            if (model != null)
                model.addObserver(this);
            onCreatePost(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void onCreatePost(Bundle savedInstanceState);

    protected abstract BasicModel getModel();
}
