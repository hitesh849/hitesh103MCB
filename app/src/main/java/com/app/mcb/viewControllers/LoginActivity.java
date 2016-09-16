package com.app.mcb.viewControllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.mcb.R;
import com.app.mcb.sharedPreferences.Config;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragmentActivity;

import java.util.Observable;

/**
 * Created by u on 9/16/2016.
 */
public class LoginActivity extends AbstractFragmentActivity implements View.OnClickListener{

    private TextView txtLogin;
    private TextView txtSignUp;
    @Override
    protected void onCreatePost(Bundle savedInstanceState) {

        setContentView(R.layout.login);
        init();
    }

    private void init() {
        txtLogin=(TextView)findViewById(R.id.txtLogin);
        txtSignUp=(TextView)findViewById(R.id.txtSignUpLogin);
        txtLogin.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
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

        int id=v.getId();
        if(id==R.id.txtLogin)
        {
            Config.setLoginStatus(true);
            Config.savePreferences();
            onBackPressed();
        }
        else if(id==R.id.txtSignUpLogin)
        {

            startActivity(new Intent(this,SignUpActivity.class));

        }
    }
}
