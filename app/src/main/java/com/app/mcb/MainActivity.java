package com.app.mcb;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.app.mcb.Utility.Constants;
import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.dao.AddParcelData;
import com.app.mcb.dao.AddTrip;
import com.app.mcb.dao.AddTripData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.sharedPreferences.Config;
import com.app.mcb.viewControllers.LoginActivity;
import com.app.mcb.viewControllers.CommonListWithAllStateFragment;
import com.app.mcb.viewControllers.dashboardFragments.DashBoardFragment;
import com.app.mcb.viewControllers.dashboardFragments.MyProfileFragment;
import com.app.mcb.viewControllers.dashboardFragments.MyWalletFragment;
import com.app.mcb.viewControllers.dashboardFragments.ReceiverListFragment;
import com.app.mcb.viewControllers.dashboardFragments.WithDrawFragment;
import com.app.mcb.viewControllers.sender.SenderHomeFragment;
import com.app.mcb.viewControllers.transporter.TransporterHomeFragment;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

public class MainActivity extends AbstractFragmentActivity implements View.OnClickListener {
    private DrawerLayout drawer;
    public AppHeaderView appHeaderView;
    private LinearLayout llLoginMain;
    private LinearLayout llDashboardMain;
    private LinearLayout llDashBoardDrawer;
    private LinearLayout llProfileMain;
    private LinearLayout llMyParcelsMain;
    private LinearLayout llMyTripMain;
    private LinearLayout llMyRecivingMain;
    private LinearLayout llWalletMain;
    private LinearLayout llWithDrawMain;
    private LinearLayout llSettingsMain;
    private LinearLayout llTandCMain;
    private LinearLayout llPrivacyPolicyMain;
    private FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        AbstractFragment abstractFragment;
        if (Config.getLoginStatus()) {
            llDashBoardDrawer.setVisibility(View.VISIBLE);
            llLoginMain.setVisibility(View.GONE);
            abstractFragment = new DashBoardFragment();
            TripTransporterData tripTransporterData = (TripTransporterData) getIntent().getSerializableExtra("data");
            ParcelDetailsData parcelDetailsData = (ParcelDetailsData) getIntent().getSerializableExtra("KEY_PARCEL_DETAIL");
            AddParcelData addParcelData = (AddParcelData) getIntent().getSerializableExtra(Constants.ADD_PARCELS_KEY);
            AddTripData addTrip = (AddTripData) getIntent().getSerializableExtra(Constants.ADD_TRIP_KEY);
            if (tripTransporterData != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("tripData", tripTransporterData);
                abstractFragment = new SenderHomeFragment();
                abstractFragment.setArguments(bundle);
            } else if (parcelDetailsData != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("KEY_PARCEL_DETAIL", parcelDetailsData);
                abstractFragment = new TransporterHomeFragment();
                abstractFragment.setArguments(bundle);
            } else if (addParcelData != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.ADD_PARCELS_KEY, addParcelData);
                abstractFragment = new SenderHomeFragment();
                abstractFragment.setArguments(bundle);
            } else if (addTrip != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.ADD_TRIP_KEY, addTrip);
                abstractFragment = new TransporterHomeFragment();
                abstractFragment.setArguments(bundle);
            }

        } else {
            llDashBoardDrawer.setVisibility(View.GONE);
            llLoginMain.setVisibility(View.VISIBLE);
            abstractFragment = new CommonListWithAllStateFragment();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fmHomeContainer, abstractFragment, "HomeFragment").commit();

    }

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {

    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    private void init() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        appHeaderView = (AppHeaderView) findViewById(R.id.appHeaderView);
        llLoginMain = (LinearLayout) findViewById(R.id.llLoginMain);
        llDashboardMain = (LinearLayout) findViewById(R.id.llDashboardMain);
        llDashBoardDrawer = (LinearLayout) findViewById(R.id.llDashBoardDrawer);
        llProfileMain = (LinearLayout) findViewById(R.id.llProfileMain);
        llMyParcelsMain = (LinearLayout) findViewById(R.id.llMyParcelsMain);
        llMyTripMain = (LinearLayout) findViewById(R.id.llMyTripMain);
        llMyRecivingMain = (LinearLayout) findViewById(R.id.llMyRecivingMain);
        llWalletMain = (LinearLayout) findViewById(R.id.llWalletMain);
        llWithDrawMain = (LinearLayout) findViewById(R.id.llWithDrawMain);
        llSettingsMain = (LinearLayout) findViewById(R.id.llSettingsMain);
        llTandCMain = (LinearLayout) findViewById(R.id.llTandCMain);
        llPrivacyPolicyMain = (LinearLayout) findViewById(R.id.llPrivacyPolicyMain);
        appHeaderView.imgBackHeaderArrow.setImageResource(R.mipmap.hamber);
        llDashboardMain.setOnClickListener(this);
        llProfileMain.setOnClickListener(this);
        llMyParcelsMain.setOnClickListener(this);
        llMyTripMain.setOnClickListener(this);
        llMyRecivingMain.setOnClickListener(this);
        llWalletMain.setOnClickListener(this);
        llWithDrawMain.setOnClickListener(this);
        llSettingsMain.setOnClickListener(this);
        llTandCMain.setOnClickListener(this);
        llPrivacyPolicyMain.setOnClickListener(this);
        llLoginMain.setOnClickListener(this);


    }

    public void setHeader(String tittle) {
        appHeaderView.txtHeaderNamecenter.setText(tittle);
    }

    @Override
    public void onBackPressed() {

       android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStackImmediate();
        } else {
            if (drawer.isDrawerOpen(GravityCompat.START))
                drawer.closeDrawer(GravityCompat.START);
            else
                super.onBackPressed();
        }
        android.support.v4.app.Fragment senderHome = getSupportFragmentManager().findFragmentById(R.id.fmContainerSenderHomeMain);
        android.support.v4.app.Fragment transporterHome = getSupportFragmentManager().findFragmentById(R.id.fmContainerTransporterHomeMain);
        if (senderHome!=null && senderHome.isVisible()) {
            if (senderHome.getFragmentManager().getBackStackEntryCount() > 0) {
                senderHome.getFragmentManager().popBackStack();
                appHeaderView.imgBackHeaderArrow.setImageResource(R.mipmap.hamber);
                return;
            }
        } else if (transporterHome!=null && transporterHome.isVisible()) {
            if (transporterHome.getFragmentManager().getBackStackEntryCount() > 0) {
                transporterHome.getFragmentManager().popBackStack();
                appHeaderView.imgBackHeaderArrow.setImageResource(R.mipmap.hamber);
                return;
            }
        } else {
            if (drawer.isDrawerOpen(GravityCompat.START))
                drawer.closeDrawer(GravityCompat.START);
            else
                super.onBackPressed();
        }

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.llBackHeader) {
            android.support.v4.app.Fragment senderHome = getSupportFragmentManager().findFragmentById(R.id.fmContainerSenderHomeMain);
            android.support.v4.app.Fragment transporterHome = getSupportFragmentManager().findFragmentById(R.id.fmContainerTransporterHomeMain);
            if (senderHome != null && senderHome.isVisible()) {
                if (senderHome.getFragmentManager().getBackStackEntryCount() > 0) {
                    senderHome.getFragmentManager().popBackStack();
                    appHeaderView.imgBackHeaderArrow.setImageResource(R.mipmap.hamber);
                    return;
                } else
                    drawerStateChanged();
            } else if (transporterHome != null && transporterHome.isVisible()) {
                if (transporterHome.getFragmentManager().getBackStackEntryCount() > 0) {
                    transporterHome.getFragmentManager().popBackStack();
                    appHeaderView.imgBackHeaderArrow.setImageResource(R.mipmap.hamber);
                    return;
                } else
                    drawerStateChanged();
            } else {
                drawerStateChanged();
            }

        } else if (id == R.id.llDashboardMain) {
            addFragment(new DashBoardFragment());
        } else if (id == R.id.llProfileMain) {
            addFragment(new MyProfileFragment());
        } else if (id == R.id.llMyParcelsMain) {
            addFragment(new SenderHomeFragment());
        } else if (id == R.id.llMyTripMain) {
            addFragment(new TransporterHomeFragment());
        } else if (id == R.id.llMyRecivingMain) {
            addFragment(new ReceiverListFragment());
        } else if (id == R.id.llWalletMain) {
            addFragment(new MyWalletFragment());
        } else if (id == R.id.llWithDrawMain) {
            addFragment(new WithDrawFragment());
        } else if (id == R.id.llSettingsMain) {

        } else if (id == R.id.llTandCMain) {

        } else if (id == R.id.llPrivacyPolicyMain) {

        } else if (id == R.id.llLoginMain) {
            startActivityForResult(new Intent(this, LoginActivity.class), 503);
        }
    }

    public void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fmHomeContainer, fragment).commit();
        drawerStateChanged();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (503): {
                if (resultCode == Activity.RESULT_OK) {
                    TripTransporterData tripTransporterData = ((TripTransporterData) data.getSerializableExtra("data"));
                    if (Config.getLoginStatus()) {
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("data", tripTransporterData);
                        startActivity(intent);
                    }
                }
                break;
            }
        }
    }


    private void drawerStateChanged() {


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }


    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
