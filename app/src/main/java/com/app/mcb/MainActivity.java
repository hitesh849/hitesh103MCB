package com.app.mcb;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.app.mcb.custom.AppHeaderView;
import com.app.mcb.viewControllers.DashBoardFragment;
import com.app.mcb.viewControllers.HomeFragment;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractAppCompatActivity;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

public class MainActivity extends AbstractFragmentActivity implements View.OnClickListener {
    private DrawerLayout drawer;
    private AppHeaderView appHeaderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fmHomeContainer, new DashBoardFragment()).commit();

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

        int id=view.getId();
        if(id==R.id.llBackHeader)
        {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        }
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
