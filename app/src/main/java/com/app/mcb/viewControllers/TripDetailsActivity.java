package com.app.mcb.viewControllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.mcb.R;
import com.app.mcb.adapters.TripDetailsVPAdapter;
import com.app.mcb.custom.AppHeaderView;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

/**
 * Created by Hitesh on 9/16/2016.
 */
public class TripDetailsActivity extends AbstractFragmentActivity implements View.OnClickListener {

    private ViewPager vpTripDetails;
    private LinearLayout llCountDotsMain;
    private AppHeaderView appHeaderView;


    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.trip_details);
        init();
        drawPageSelectionIndicators(0);
    }

    private void init() {
        vpTripDetails = (ViewPager) findViewById(R.id.vpTripDetails);
        appHeaderView = (AppHeaderView) findViewById(R.id.appHeaderView);
        vpTripDetails.setAdapter(new TripDetailsVPAdapter(this, this));
        appHeaderView.txtHeaderNamecenter.setText(getResources().getString(R.string.trip_details));
        viewPagerChangeListener();

    }

    private void viewPagerChangeListener() {
        vpTripDetails.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {



                drawPageSelectionIndicators(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
        if (id == R.id.llBookNoewTripDetailsRow || id == R.id.llBookNoewTripDetailsRow0) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void drawPageSelectionIndicators(int mPosition) {
        if (llCountDotsMain != null) {
            llCountDotsMain.removeAllViews();
        }
        llCountDotsMain = (LinearLayout) findViewById(R.id.llCountDotsMain);
        ImageView[] dots = new ImageView[3];

        if(mPosition>2)
        {
            mPosition=(mPosition%3);
        }
        for (int i = 0; i < 3; i++) {


            dots[i] = new ImageView(this);
            if (i == mPosition)
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.item_selected));
            else
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.vp_item_unselected));


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(5, 0, 5, 0);
            llCountDotsMain.addView(dots[i], params);
        }
    }
}
