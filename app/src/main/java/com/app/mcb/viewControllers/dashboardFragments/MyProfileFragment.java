package com.app.mcb.viewControllers.dashboardFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.mcb.R;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

/**
 * Created by u on 9/15/2016.
 */
public class MyProfileFragment extends AbstractFragment implements View.OnClickListener
{


    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_profile, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void onClick(View v) {

    }
}
