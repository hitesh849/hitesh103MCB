package com.app.mcb.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.app.mcb.R;

import org.byteclues.lib.init.Env;

/**
 * Created by Hitesh kumawat on 14-09-2016.
 */
public class AppHeaderView extends RelativeLayout {

    public LinearLayout llBackHeader;
    public RelativeLayout rlNotificationHeader;
    public RelativeLayout rlLogoutHeader;

    public AppHeaderView(Context context) {
        super(context);
        init(context);
    }

    public AppHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AppHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        super.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(context).inflate(R.layout.app_header, this, true);
        this.llBackHeader = (LinearLayout) findViewById(R.id.llBackHeader);
        this.rlNotificationHeader = (RelativeLayout) findViewById(R.id.rlNotificationHeader);
        this.rlLogoutHeader = (RelativeLayout) findViewById(R.id.rlLogoutHeader);
        this.llBackHeader.setOnClickListener((OnClickListener) Env.currentActivity);

        this.rlNotificationHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        this.rlLogoutHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
