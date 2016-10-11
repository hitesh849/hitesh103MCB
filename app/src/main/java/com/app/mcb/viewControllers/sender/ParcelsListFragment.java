package com.app.mcb.viewControllers.sender;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.ParcelsListVPAdapter;
import com.app.mcb.dao.ParcelListData;
import com.app.mcb.dao.UserInfoData;
import com.app.mcb.model.ParcelListModel;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh kumawat on 18-09-2016.
 */
public class ParcelsListFragment extends AbstractFragment implements View.OnClickListener {

    private ViewPager vpParcelList;
    private LinearLayout llCountDotsMain;
    private LinearLayout llParcelListMain;
    private ParcelListModel parcelListModel = new ParcelListModel();


    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parcels_list_fragment, container, false);
        init(view);
        getParcelList();
        return view;
    }


    private void init(View view) {
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.list_of_parcels));
        llParcelListMain = (LinearLayout) view.findViewById(R.id.llParcelListMain);
        vpParcelList = (ViewPager) view.findViewById(R.id.vpParcelList);
        llCountDotsMain = (LinearLayout) view.findViewById(R.id.llCountDotsMain);
        drawPageSelectionIndicators(0);
        viewPagerChangeListener();
    }

    @Override
    protected BasicModel getModel() {
        return parcelListModel;
    }

    @Override
    public void update(Observable observable, Object data) {
        try {
            Util.dimissProDialog();
            if (data != null && data instanceof ParcelListData) {
                ParcelListData parcelListData = (ParcelListData) data;
                if (parcelListData.status.equals("success")) {
                    if (parcelListData.response != null) {
                        vpParcelList.setAdapter(new ParcelsListVPAdapter(getActivity(), parcelListData.response, this));
                    }
                } else if (parcelListData.status.equals("Error")) {
                    Util.showOKSnakBar(llCountDotsMain, parcelListData.errorMessage);
                }
            } else if (data != null && data instanceof RetrofitError) {
                Util.showOKSnakBar(llParcelListMain, getResources().getString(R.string.pls_try_again));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.imgViewPLR) {
            Util.replaceFragment(getActivity(), R.id.fmContainerSenderHomeMain, new ParcelDetailsFragment());
        } else if (id == R.id.imgSettingsPLR) {
            ((ImageView) view).setBackgroundResource(R.mipmap.action_setting_hover);
            PopupMenu popup = new PopupMenu(getActivity(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.main, popup.getMenu());
            popup.show();
        }
    }

    private void viewPagerChangeListener() {
        vpParcelList.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                drawPageSelectionIndicators(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void drawPageSelectionIndicators(int mPosition) {
        if (llCountDotsMain != null) {
            llCountDotsMain.removeAllViews();
        }

        ImageView[] dots = new ImageView[3];

        if (mPosition > 2) {
            mPosition = (mPosition % 3);
        }
        for (int i = 0; i < 3; i++) {


            dots[i] = new ImageView(getActivity());
            if (i == mPosition)
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.item_selected));
            else
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.vp_item_unselected));


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(5, 0, 5, 0);
            llCountDotsMain.addView(dots[i], params);
        }
    }

    private void getParcelList() {
        Bundle bundle = getArguments();
        String listType = "Active";
        if (bundle != null) {
            listType = getArguments().getString("DATA");
        }
        if (listType.equals("Active")) {
            getActiveParcels();
        } else if (listType.equals("All")) {
            getAllParcels();
        }
    }

    private void getActiveParcels() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                parcelListModel.getActiveParcels();
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getAllParcels() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                parcelListModel.getAllParcels();
            } else {
                Util.showAlertDialog(null, getResources().getString(R.string.noInternetMsg));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
