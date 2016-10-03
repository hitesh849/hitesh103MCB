package com.app.mcb.viewControllers;

import android.support.multidex.MultiDexApplication;

import com.app.mcb.database.DatabaseMgr;
import com.app.mcb.sharedPreferences.Config;

import org.byteclues.lib.init.Env;

/**
 * Created by u on 9/16/2016.
 */
public class MCBAPP extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Env.appContext = this;
        Config.init(this);
        DatabaseMgr.getInstance(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_BACKGROUND || level == TRIM_MEMORY_UI_HIDDEN) {
            Env.APP_STATE = Env.applicationState.BACKGROUND;

        }
    }
}
