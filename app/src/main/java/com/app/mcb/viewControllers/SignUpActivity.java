package com.app.mcb.viewControllers;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.mcb.R;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

/**
 * Created by u on 9/16/2016.
 */
public class SignUpActivity extends AbstractFragmentActivity implements View.OnClickListener {

    private TextView txtRagisterSignUp;

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {

        setContentView(R.layout.sign_up);
        init();
    }

    private void init() {
        txtRagisterSignUp = (TextView) findViewById(R.id.txtRagisterSignUp);
        txtRagisterSignUp.setOnClickListener(this);
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
        if (id == R.id.txtRagisterSignUp) {
            onBackPressed();
        }
        else if(id==R.id.llBackHeader)
        {
            onBackPressed();
        }
    }
}
