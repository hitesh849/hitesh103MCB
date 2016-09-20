package com.app.mcb.viewControllers.sender;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Util;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

/**
 * Created by Hitesh kumawat on 18-09-2016.
 */
public class AddParcelFragment extends AbstractFragment implements View.OnClickListener {

    private ViewPager vpParcelList;
    private LinearLayout llReceiverContainerMain;
    private LinearLayout llSearchReciver;
    private TextView txtSubmitAddParcel;
    private TextView txtNewReceiverAddParcel;
    private TextView txtExistingReceiverAddParcel;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_parcel, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        llReceiverContainerMain = (LinearLayout) view.findViewById(R.id.llReceiverContainerMain);
        addViewInRelayout(R.layout.add_parcel_receiverinfo_search);
        llSearchReciver = (LinearLayout) view.findViewById(R.id.llSearchReciver);
        llSearchReciver.setOnClickListener(this);
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.add_parcels));

    }

    private void addViewInRelayout(int layout) {
        llReceiverContainerMain.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        LinearLayout llLayout = (LinearLayout) inflater.inflate(layout, null, false);
        llReceiverContainerMain.addView(llLayout);

        txtNewReceiverAddParcel = (TextView) llLayout.findViewById(R.id.txtNewReceiverAddParcel);
        txtNewReceiverAddParcel.setOnClickListener(this);
        txtExistingReceiverAddParcel = (TextView) llLayout.findViewById(R.id.txtExistingReceiverAddParcel);
        txtExistingReceiverAddParcel.setOnClickListener(this);

        txtSubmitAddParcel = (TextView) llLayout.findViewById(R.id.txtSubmitAddParcel);
        if(txtSubmitAddParcel!=null)
        txtSubmitAddParcel.setOnClickListener(this);
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
        if (id == R.id.llSearchReciver) {
            addViewInRelayout(R.layout.add_parcel_receiverinfo_submit);

        }  else if (id == R.id.txtNewReceiverAddParcel) {
            addViewInRelayout(R.layout.add_parcel_receiverinfo_new_user);

        } else if (id == R.id.txtExistingReceiverAddParcel) {
            addViewInRelayout(R.layout.add_parcel_receiverinfo_search);
        }else if (id == R.id.txtSubmitAddParcel) {
            Util.replaceFragment(getActivity(),R.id.fmContainerSenderHomeMain,new ConfirmParcelFragment());
        }

    }
}
