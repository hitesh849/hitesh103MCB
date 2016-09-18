package com.app.mcb.viewControllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.app.mcb.R;
import com.app.mcb.adapters.TripDetailsVPAdapter;
import com.app.mcb.viewControllers.LoginActivity;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

/**
 * Created by Hitesh on 9/16/2016.
 */
public class TripDetailsActivity extends AbstractFragmentActivity implements View.OnClickListener {

    private ViewPager vpTripDetails;


    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.trip_details);
        init();
    }

    private void init() {
        vpTripDetails = (ViewPager) findViewById(R.id.vpTripDetails);
        vpTripDetails.setAdapter(new TripDetailsVPAdapter(this, this));
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

        int id = v.getId();
        if (id == R.id.llBookNoewTripDetailsRow) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
