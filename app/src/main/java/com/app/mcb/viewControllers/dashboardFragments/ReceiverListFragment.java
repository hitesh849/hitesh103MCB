package com.app.mcb.viewControllers.dashboardFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.MyTripListVPAdapter;
import com.app.mcb.adapters.ParcelsListVPAdapter;
import com.app.mcb.adapters.ReceiverListAdapter;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.ParcelBookingChangeStatusData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.ParcelListData;
import com.app.mcb.dao.ReceiverData;
import com.app.mcb.filters.CommonListener;
import com.app.mcb.filters.ParcelFilter;
import com.app.mcb.filters.ParcelFilterListener;
import com.app.mcb.filters.TransporterFilter;
import com.app.mcb.model.ReceiverModel;
import com.app.mcb.retrointerface.TryAgainInterface;
import com.app.mcb.viewControllers.sender.ParcelDetailsFragment;
import com.app.mcb.viewControllers.sender.ParcelPayNowActivity;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.ArrayList;
import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh kumawat on 19-09-2016.
 */
public class ReceiverListFragment extends AbstractFragment implements View.OnClickListener, ParcelFilterListener {

    private ViewPager vpReceiverList;
    private LinearLayout llCountDotsMain;
    private RelativeLayout rlReceiverListMain;
    private ReceiverModel receiverModel = new ReceiverModel();
    private ArrayList<ReceiverData> receiverListMain;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receiver_list_fragment, container, false);
        init(view);
        getReceiverData();
        return view;
    }

    private void init(View view) {
        ParcelFilter.addFilterView(getActivity(), view, this);
        rlReceiverListMain = (RelativeLayout) view.findViewById(R.id.rlReceiverListMain);
        vpReceiverList = (ViewPager) view.findViewById(R.id.vpReceiverList);
        llCountDotsMain = (LinearLayout) view.findViewById(R.id.llCountDotsMain);
        drawPageSelectionIndicators(0);
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.receiver_details));
        viewPagerChangeListener();
    }

    @Override
    protected BasicModel getModel() {
        return receiverModel;
    }


    private void viewPagerChangeListener() {
        vpReceiverList.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    @Override
    public void onClick(View view) {

        int id = view.getId();
        /*if (id == R.id.imgViewParcelListRow) {
            Util.replaceFragment(getActivity(), R.id.fmContainerSenderHomeMain, new ParcelDetailsFragment());
        }*/
        if (id == R.id.imgSettingReceiverSide) {

            final ReceiverData receiverData = ((ReceiverData) view.getTag());
            PopupMenu popup = new PopupMenu(getActivity(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.parcel_status, popup.getMenu());
            popup.show();
            hideAllPopUPMenuItem(popup);
            switch (receiverData.status) {
                case Constants.ParcelDelivered:
                    MenuItem menuItem1 = popup.getMenu().findItem(R.id.action_parcel_received);
                    menuItem1.setVisible(true);
                    break;

            }

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.action_parcel_received:
                            usrUpdateTripStatus(receiverData);
                            break;
                    }
                    return false;
                }
            });

        }
    }

    private void addMainView() {
        rlReceiverListMain.removeAllViews();
        rlReceiverListMain.addView(vpReceiverList);
        rlReceiverListMain.addView(llCountDotsMain);
    }

    private void usrUpdateTripStatus(final ReceiverData parcelDetailsData) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                receiverModel.usrUpdateTripStatus(parcelDetailsData, Constants.ParcelDeliveryComplete, "Add Comment");
            } else {
                rlReceiverListMain.addView(Util.getViewInternetNotFound(getActivity(), rlReceiverListMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        addMainView();
                        usrUpdateTripStatus(parcelDetailsData);
                    }
                }));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void hideAllPopUPMenuItem(PopupMenu popup) {
        for (int i = 0; i < popup.getMenu().size(); i++) {
            popup.getMenu().getItem(i).setVisible(false);
        }
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

    private void getReceiverData() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                receiverModel.getReceiverData();
            } else {
                rlReceiverListMain.addView(Util.getViewInternetNotFound(getActivity(), rlReceiverListMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        addMainView();
                        getReceiverData();
                    }
                }));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        try {
            Util.dimissProDialog();
            if (data != null && data instanceof ReceiverData) {
                ReceiverData receiverData = (ReceiverData) data;
                if ("success".equals(receiverData.status)) {
                    if (receiverData.response != null) {
                        receiverListMain = receiverData.response;
                        vpReceiverList.setAdapter(new ReceiverListAdapter(getActivity(), receiverData.response, this));

                        if (receiverData.response.size() <= 0) {
                            rlReceiverListMain.addView(Util.getViewDataNotFound(getActivity(), rlReceiverListMain, getString(R.string.parcel_unavailable)));
                        }
                    }
                } else if (receiverData.status.equals("Error")) {
                    rlReceiverListMain.addView(Util.getViewDataNotFound(getActivity(), rlReceiverListMain, receiverData.errorMessage));
                }
            } else if (data != null && data instanceof ParcelBookingChangeStatusData) {
                ParcelBookingChangeStatusData parcelBookingRejectedData = ((ParcelBookingChangeStatusData) data);
                if ("success".equals(parcelBookingRejectedData.status))
                    getReceiverData();
                else
                    rlReceiverListMain.addView(Util.getViewDataNotFound(getActivity(), rlReceiverListMain, parcelBookingRejectedData.errorMessage));
            } else if (data != null && data instanceof RetrofitError) {
                rlReceiverListMain.addView(Util.getViewServerNotResponding(getActivity(), rlReceiverListMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        addMainView();
                    }
                }));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void filterData(FilterData filterData) {
        ArrayList<ReceiverData> filterList = new ArrayList<ReceiverData>();
        addMainView();
        if (!TextUtils.isEmpty(filterData.parcelId)) {
            ArrayList<ReceiverData> filterList1 = new ArrayList<ReceiverData>();
            for (ReceiverData myTripsData : receiverListMain) {
                if (myTripsData.id.equalsIgnoreCase(filterData.tripId)) {
                    filterList1.add(myTripsData);
                }
            }
            filterList.clear();
            filterList.addAll(filterList1);
        } else {
            filterList.addAll(receiverListMain);
        }
        if (!TextUtils.isEmpty(filterData.tillDate)) {
            ArrayList<ReceiverData> filterList1 = new ArrayList<ReceiverData>();
            for (ReceiverData myTripsData : filterList) {
                if (Util.getDateFromDateTimeFormat(myTripsData.dep_time).equalsIgnoreCase(filterData.departure_date)) {
                    filterList1.add(myTripsData);
                }
            }
            filterList.clear();
            filterList.addAll(filterList1);
        }

        if (!TextUtils.isEmpty(filterData.parcelStatus)) {
            ArrayList<ReceiverData> filterList1 = new ArrayList<ReceiverData>();
            for (ReceiverData receiverData : filterList) {
                if (receiverData.status.equalsIgnoreCase(filterData.tripStatus)) {
                    filterList1.add(receiverData);
                }
            }
            filterList.clear();
            filterList.addAll(filterList1);
        }

        vpReceiverList.setAdapter(new ReceiverListAdapter(getActivity(), filterList, this));
        if (filterList.size() <= 0) {
            rlReceiverListMain.addView(Util.getViewDataNotFound(getActivity(), rlReceiverListMain, getString(R.string.parcel_unavailable)));
        }
    }
}
