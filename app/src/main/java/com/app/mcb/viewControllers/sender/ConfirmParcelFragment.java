package com.app.mcb.viewControllers.sender;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Util;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

/**
 * Created by u on 9/20/2016.
 */
public class ConfirmParcelFragment extends AbstractFragment implements View.OnClickListener {

    private LinearLayout llConfirmParcel;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_parcel, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        llConfirmParcel=(LinearLayout)view.findViewById(R.id.llConfirmParcel);
        llConfirmParcel.setOnClickListener(this);
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.confirm_parcel));
    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id==R.id.llConfirmParcel)
        {
            Util.replaceFragment(getActivity(),R.id.fmHomeContainer,new FeedBackFragment());
        }
    }
}