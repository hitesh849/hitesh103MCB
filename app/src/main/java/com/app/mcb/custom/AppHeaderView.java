package com.app.mcb.custom;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.sharedPreferences.Config;

import org.byteclues.lib.init.Env;
import org.byteclues.lib.view.AbstractFragment;
import org.byteclues.lib.view.AbstractFragmentActivity;

/**
 * Created by Hitesh kumawat on 14-09-2016.
 */
public class AppHeaderView extends RelativeLayout {

    public LinearLayout llBackHeader;
    public RelativeLayout rlNotificationHeader;
    public RelativeLayout rlLogoutHeader;
    public ImageView imgBackHeaderArrow;
    public TextView txtHeaderNamecenter;


    public AppHeaderView(Context context) {
        super(context);
        init(context);
        if (Config.getLoginStatus())
            rlLogoutHeader.setVisibility(VISIBLE);
        else
            rlLogoutHeader.setVisibility(GONE);
    }

    public AppHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        if (Config.getLoginStatus())
            rlLogoutHeader.setVisibility(VISIBLE);
        else
            rlLogoutHeader.setVisibility(GONE);
    }

    public AppHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        if (Config.getLoginStatus())
            rlLogoutHeader.setVisibility(VISIBLE);
        else
            rlLogoutHeader.setVisibility(GONE);
    }

    private void init(final Context context) {
        super.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(context).inflate(R.layout.app_header, this, true);
        this.llBackHeader = (LinearLayout) findViewById(R.id.llBackHeader);
        this.rlNotificationHeader = (RelativeLayout) findViewById(R.id.rlNotificationHeader);
        this.rlLogoutHeader = (RelativeLayout) findViewById(R.id.rlLogoutHeader);
        this.imgBackHeaderArrow = (ImageView) findViewById(R.id.imgBackHeaderArrow);
        this.txtHeaderNamecenter = (TextView) findViewById(R.id.txtHeaderNamecenter);
        this.llBackHeader.setOnClickListener((OnClickListener) Env.currentActivity);


        if (!(Env.currentActivity instanceof MainActivity)) {
            this.llBackHeader.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AbstractFragmentActivity) Env.currentActivity).onBackPressed();
                }
            });
        }

        this.rlNotificationHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        this.rlLogoutHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.clearPreferences();
                Env.currentActivity.startActivity(new Intent(Env.currentActivity, MainActivity.class));
                ((AbstractFragmentActivity) Env.currentActivity).finish();
                rlLogoutHeader.setVisibility(GONE);

            }
        });
    }
}
