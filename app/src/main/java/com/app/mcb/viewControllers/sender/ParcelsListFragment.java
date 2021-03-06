package com.app.mcb.viewControllers.sender;

import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.MyTripListVPAdapter;
import com.app.mcb.adapters.ParcelsListVPAdapter;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.ParcelBookingChangeStatusData;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.ParcelListData;
import com.app.mcb.filters.ParcelFilter;
import com.app.mcb.filters.ParcelFilterListener;
import com.app.mcb.model.ParcelListModel;
import com.app.mcb.retrointerface.TryAgainInterface;
import com.app.mcb.viewControllers.chat.ChatActivity;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.ArrayList;
import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh kumawat on 18-09-2016.
 */
public class ParcelsListFragment extends AbstractFragment implements View.OnClickListener, ParcelFilterListener {

    private ViewPager vpParcelList;
    private LinearLayout llCountDotsMain;
    private LinearLayout llParcelListMain;
    private RelativeLayout rlParcelListContainerMain;
    private ParcelListModel parcelListModel = new ParcelListModel();
    private String listType;
    private ArrayList<ParcelDetailsData> parcelListMain;
    private View view;


    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view==null) {
            view = inflater.inflate(R.layout.parcels_list_fragment, container, false);
            init(view);
            getParcelList();
        }
        return view;
    }

    private void init(View view) {
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.list_of_parcels));
        llParcelListMain = (LinearLayout) view.findViewById(R.id.llParcelListMain);
        vpParcelList = (ViewPager) view.findViewById(R.id.vpParcelList);
        llCountDotsMain = (LinearLayout) view.findViewById(R.id.llCountDotsMain);
        rlParcelListContainerMain = (RelativeLayout) view.findViewById(R.id.rlParcelListContainerMain);
        ParcelFilter.addFilterView(getActivity(), view, this);
        drawPageSelectionIndicators(0);
        viewPagerChangeListener();
    }

    @Override
    protected BasicModel getModel() {
        return parcelListModel;
    }


    private void addContainerView() {
        rlParcelListContainerMain.removeAllViews();
        rlParcelListContainerMain.addView(vpParcelList);
        rlParcelListContainerMain.addView(llCountDotsMain);
    }

    @Override
    public void update(Observable observable, Object data) {
        try {
            Util.dimissProDialog();

            if (data != null && data instanceof ParcelListData) {
                ParcelListData parcelListData = (ParcelListData) data;
                if (parcelListData.status.equals("success")) {
                    if (parcelListData.response != null) {
                        parcelListMain = parcelListData.response;
                        vpParcelList.setAdapter(new ParcelsListVPAdapter(getActivity(), parcelListMain, this));
                        if (vpParcelList != null && parcelListData.response.size() <= 0) {
                            rlParcelListContainerMain.addView(Util.getViewDataNotFound(getActivity(), rlParcelListContainerMain, getString(R.string.parcel_unavailable)));
                        }
                    }
                } else if (parcelListData.status.equals("Error")) {
                    rlParcelListContainerMain.addView(Util.getViewDataNotFound(getActivity(), rlParcelListContainerMain, parcelListData.errorMessage));
                }
            } else if (data != null && data instanceof ParcelBookingChangeStatusData) {
                ParcelBookingChangeStatusData parcelBookingRejectedData = ((ParcelBookingChangeStatusData) data);
                if ("success".equals(parcelBookingRejectedData.status))
                    getParcelList();
                else
                    rlParcelListContainerMain.addView(Util.getViewDataNotFound(getActivity(), rlParcelListContainerMain, parcelBookingRejectedData.errorMessage));
            } else if (data != null && data instanceof RetrofitError) {
                Util.showSnakBar(llParcelListMain, getResources().getString(R.string.pls_try_again));
                rlParcelListContainerMain.addView(Util.getViewServerNotResponding(getActivity(), rlParcelListContainerMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        Util.replaceFragment(getActivity(), R.id.fmHomeContainer, new SenderHomeFragment());
                    }
                }));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.imgViewPLR) {
            ParcelDetailsData parcelDetailsData = (ParcelDetailsData) view.getTag();
            ParcelDetailsFragment parcelDetailsFragment = new ParcelDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", parcelDetailsData);
            parcelDetailsFragment.setArguments(bundle);
            Util.addFragmentWithOnBack(getActivity(), R.id.fmContainerSenderHomeMain, parcelDetailsFragment);

        } else if (id == R.id.imgSettingsPLR) {
            final ParcelDetailsData parcelDetailsData = ((ParcelDetailsData) view.getTag());
            PopupMenu popup = new PopupMenu(getActivity(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.parcel_status, popup.getMenu());
            popup.show();

            hideAllPopUPMenuItem(popup);

            switch (parcelDetailsData.status) {
                case Constants.ParcelPaymentDue:
                    MenuItem menuItem = popup.getMenu().findItem(R.id.action_payment_due);
                    menuItem.setTitle(getString(R.string.make_payment));
                    menuItem.setVisible(true);
                    break;
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
                        case R.id.action_payment_due:
                            Intent intent = new Intent(getActivity(), ParcelPayNowActivity.class);
                            intent.putExtra("parcelId", parcelDetailsData.trans_id);
                            intent.putExtra("status", parcelDetailsData.status);
                            startActivity(intent);
                            break;
                        case R.id.action_parcel_received:
                            parcelDetailsData.status = Constants.ParcelDeliveryComplete;
                            usrUpdateParcelStatus(parcelDetailsData);
                            break;
                    }
                    return false;
                }
            });

        } else if (id == R.id.imgEditPLR) {
            ParcelDetailsData parcelDetailsData = (ParcelDetailsData) view.getTag();
            AddParcelFragment addParcelFragment = new AddParcelFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", parcelDetailsData);
            bundle.putString("listType", listType);
            addParcelFragment.setArguments(bundle);
            Util.addFragmentWithOnBack(getActivity(), R.id.fmContainerSenderHomeMain, addParcelFragment);

        } else if (id == R.id.imgCancelPLR) {
            final ParcelDetailsData parcelDetailsData = (ParcelDetailsData) view.getTag();

            if (Constants.ParcelBookedWithTR.equals(parcelDetailsData.status)) {
                Util.showAlertWithCancelDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Util.showAlertDialog(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelParcel(parcelDetailsData);
                            }
                        }, getString(R.string.do_you_want_to_delete_parcel));
                    }
                }, getString(R.string.parcel_edit_if_booked));
            } else {
                Util.showAlertDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelParcel(parcelDetailsData);
                    }
                }, getString(R.string.do_you_want_to_delete_parcel));
            }

        } else if (id == R.id.imgChatParcelList) {
          startActivity(new Intent(getActivity(), ChatActivity.class));
        }
    }

    private void hideAllPopUPMenuItem(PopupMenu popup) {
        for (int i = 0; i < popup.getMenu().size(); i++) {
            popup.getMenu().getItem(i).setVisible(false);
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

        if (mPosition >= 3) {
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
        listType = "Active";
        if (bundle != null) {
            listType = getArguments().getString("DATA");
        }
        if (listType.equals("Active")) {
            getActiveParcels();
        } else if (listType.equals("All")) {
            getAllParcels();
        }
    }

    private void usrUpdateParcelStatus(final ParcelDetailsData parcelDetailsData) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                parcelListModel.usrUpdateParcelStatus(parcelDetailsData, Constants.ParcelDeliveryComplete, "Add Comment");
            } else {
                rlParcelListContainerMain.addView(Util.getViewInternetNotFound(getActivity(), rlParcelListContainerMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        addContainerView();
                        usrUpdateParcelStatus(parcelDetailsData);
                    }
                }));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getActiveParcels() {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                parcelListModel.getActiveParcels();
            } else {
                rlParcelListContainerMain.addView(Util.getViewInternetNotFound(getActivity(), rlParcelListContainerMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        addContainerView();
                        getActiveParcels();
                    }
                }));
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
                rlParcelListContainerMain.addView(Util.getViewInternetNotFound(getActivity(), rlParcelListContainerMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        addContainerView();
                        getAllParcels();
                    }
                }));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cancelParcel(final ParcelDetailsData parcelDetailsData) {
        try {
            if (Util.isDeviceOnline()) {
                Util.showProDialog(getActivity());
                parcelListModel.cancelParcel(parcelDetailsData);
            } else {
                rlParcelListContainerMain.addView(Util.getViewInternetNotFound(getActivity(), rlParcelListContainerMain, new TryAgainInterface() {
                    @Override
                    public void callBack() {
                        addContainerView();
                        cancelParcel(parcelDetailsData);
                    }
                }));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void filterData(FilterData filterData) {
        addContainerView();
        if (TextUtils.isEmpty(filterData.parcelId) && TextUtils.isEmpty(filterData.tillDate) && TextUtils.isEmpty(filterData.parcelStatus)) {
            Util.showSnakBar(llParcelListMain, getResources().getString(R.string.filter_validation));
        } else {
            ArrayList<ParcelDetailsData> filterList = new ArrayList<ParcelDetailsData>();

            if (!TextUtils.isEmpty(filterData.tripId)) {
                ArrayList<ParcelDetailsData> filterList1 = new ArrayList<ParcelDetailsData>();
                for (ParcelDetailsData parcelList : parcelListMain) {
                    if (parcelList.id.equalsIgnoreCase(filterData.parcelId)) {
                        filterList1.add(parcelList);
                    }
                }
                filterList.clear();
                filterList.addAll(filterList1);
            } else {
                filterList.addAll(parcelListMain);
            }
            if (!TextUtils.isEmpty(filterData.tillDate)) {
                ArrayList<ParcelDetailsData> filterList1 = new ArrayList<ParcelDetailsData>();
                for (ParcelDetailsData parcelData : filterList) {
                    if (parcelData.till_date == null)
                        System.out.println("");
                    if (parcelData.till_date.equalsIgnoreCase(filterData.tillDate)) {
                        filterList1.add(parcelData);
                    }
                }
                filterList.clear();
                filterList.addAll(filterList1);
            }

            if (!TextUtils.isEmpty(filterData.parcelStatus)) {
                ArrayList<ParcelDetailsData> filterList1 = new ArrayList<ParcelDetailsData>();
                for (ParcelDetailsData parcelData : filterList) {
                    if (parcelData.status.equalsIgnoreCase(filterData.parcelStatus)) {
                        filterList1.add(parcelData);
                    }
                }
                filterList.clear();
                filterList.addAll(filterList1);
            }


            if (vpParcelList != null && filterList.size() <= 0) {
                rlParcelListContainerMain.addView(Util.getViewDataNotFound(getActivity(), rlParcelListContainerMain, getString(R.string.parcel_unavailable)));
            } else
                vpParcelList.setAdapter(new ParcelsListVPAdapter(getActivity(), filterList, this));
        }
    }
}
