package com.app.mcb.viewControllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.mcb.R;
import com.app.mcb.adapters.TripListStateWiseAdapter;
import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.viewControllers.sender.TripDetailsActivity;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

/**
 * Created by u on 9/15/2016.
 */
public class TripListWithState extends AbstractFragmentActivity implements View.OnClickListener {

    private AppHeaderView appHeaderView;
    private RecyclerView rvTripHome;

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.trip_list_specific_state);
        init();
    }

    private void init() {
        appHeaderView = (AppHeaderView) findViewById(R.id.appHeaderView);
        appHeaderView.imgBackHeaderArrow.setImageResource(R.mipmap.back);
        rvTripHome = (RecyclerView) findViewById(R.id.rvTripHome);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvTripHome.setLayoutManager(llm);
        rvTripHome.setAdapter(new TripListStateWiseAdapter(this, this));
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
        if (id == R.id.llBackHeader) {
            onBackPressed();
        } else if (id == R.id.llHomeRowMain) {

            startActivity(new Intent(this, TripDetailsActivity.class));
        }
    }
}
