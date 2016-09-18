package com.app.mcb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.sharedPreferences.Config;
import com.app.mcb.viewControllers.TripListWithAllStateFragment;
import com.app.mcb.viewControllers.dashboardFragments.DashBoardFragment;
import com.app.mcb.viewControllers.dashboardFragments.MyProfileFragment;
import com.app.mcb.viewControllers.dashboardFragments.MyWalletFragment;
import com.app.mcb.viewControllers.dashboardFragments.WithDrawFragment;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

public class MainActivity extends AbstractFragmentActivity implements View.OnClickListener {
    private DrawerLayout drawer;
    private AppHeaderView appHeaderView;
    private LinearLayout llDashboardMain;
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
        AbstractFragment abstractFragment=new TripListWithAllStateFragment();
        if(Config.getLoginStatus())
        {
            abstractFragment=new DashBoardFragment();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fmHomeContainer,abstractFragment ,"HomeFragment").commit();

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
        llDashboardMain = (LinearLayout) findViewById(R.id.llDashboardMain);
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

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.llBackHeader) {
            drawerStateChanged();
        } else if (id == R.id.llDashboardMain) {
            addFragment(new DashBoardFragment());
        } else if (id == R.id.llProfileMain) {
            addFragment(new MyProfileFragment());
        } else if (id == R.id.llMyParcelsMain) {

        } else if (id == R.id.llMyTripMain) {

        } else if (id == R.id.llMyRecivingMain) {

        } else if (id == R.id.llWalletMain) {
            addFragment(new MyWalletFragment());
        } else if (id == R.id.llWithDrawMain) {
            addFragment(new WithDrawFragment());
        } else if (id == R.id.llSettingsMain) {

        } else if (id == R.id.llTandCMain) {

        } else if (id == R.id.llPrivacyPolicyMain) {

        }
    }

    public void addFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().add(R.id.fmHomeContainer, fragment).commit();
        drawerStateChanged();

    }
    private void drawerStateChanged()
    {
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
