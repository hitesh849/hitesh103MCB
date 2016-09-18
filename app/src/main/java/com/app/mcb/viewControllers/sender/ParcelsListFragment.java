package com.app.mcb.viewControllers.sender;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.ParcelsListVPAdapter;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

/**
 * Created by Hitesh kumawat on 18-09-2016.
 */
public class ParcelsListFragment extends AbstractFragment implements View.OnClickListener {

    private ViewPager vpParcelList;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parcels_list_fragment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        vpParcelList = (ViewPager) view.findViewById(R.id.vpParcelList);
        vpParcelList.setAdapter(new ParcelsListVPAdapter(getActivity(), this));
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
        if (id == R.id.imgViewParcelListRow) {
            Util.replaceFragment(getActivity(),R.id.fmContainerSenderHomeMain,new ParcelDetailsFragment());
        }
    }
}
