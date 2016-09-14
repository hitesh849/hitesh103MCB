package org.byteclues.lib.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.byteclues.lib.R;

/**
 * Created by admin on 5/30/2016.
 */
public class AppErrorView extends RelativeLayout
{

    public LinearLayout llMdActivityNotFound;
    public LinearLayout llNoInternetConnection;
    public LinearLayout llserverNotRes;
    public Button btnRetry;
    public AppErrorView(Context context) {
        super(context);
        init(context);
    }

    public AppErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AppErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        super.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(context).inflate(R.layout.app_error, this, true);
        this.llMdActivityNotFound = (LinearLayout) findViewById(R.id.llMdActivityNotFound);
        this.llNoInternetConnection = (LinearLayout) findViewById(R.id.llNoInternetConnection);
        this.llserverNotRes = (LinearLayout) findViewById(R.id.llserverNotRes);
        this.btnRetry = (Button) findViewById(R.id.btnRetry);
    }

    private void hideAllView()
    {
        llMdActivityNotFound.setVisibility(GONE);
        llNoInternetConnection.setVisibility(GONE);
        llserverNotRes.setVisibility(GONE);
    }
    public void showNoDataMsg()
    {
        hideAllView();
        llMdActivityNotFound.setVisibility(VISIBLE);
    }

    public void showNoInternetConnection()
    {
        hideAllView();
        llNoInternetConnection.setVisibility(VISIBLE);
    }
    public void showServerNotResponding()
    {
        hideAllView();
        llserverNotRes.setVisibility(VISIBLE);
    }
    public void showErrorView(int i) {
        hideAllView();
        this.setVisibility(View.VISIBLE);
        if (i == 1)
           showNoInternetConnection();
        else if (i == 2)
          showNoDataMsg();
        else if (i == 3)
           showServerNotResponding();

    }
}
