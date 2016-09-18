package com.app.mcb.Utility;

import android.content.Context;

import org.byteclues.lib.view.AbstractFragment;
import org.byteclues.lib.view.AbstractFragmentActivity;

/**
 * Created by u on 9/15/2016.
 */
public class Util {
    public static void replaceFragment(Context context, int container, AbstractFragment abstractFragment) {
        ((AbstractFragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(container, abstractFragment).commit();
    }

    public static void addFragment(Context context, int container, AbstractFragment abstractFragment) {
        ((AbstractFragmentActivity) context).getSupportFragmentManager().beginTransaction().add(container, abstractFragment).commit();
    }
}
