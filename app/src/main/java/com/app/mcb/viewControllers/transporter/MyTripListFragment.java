package com.app.mcb.viewControllers.transporter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.MyTripListVPAdapter;
import com.app.mcb.dao.CommonResponseData;
import com.app.mcb.dao.FilterData;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.ParcelBookingChangeStatusData;
import com.app.mcb.filters.CommonListener;
import com.app.mcb.filters.TripFilter;
import com.app.mcb.model.MyTripsModel;
import com.app.mcb.retrointerface.TryAgainInterface;
import com.app.mcb.sharedPreferences.Config;
import com.squareup.picasso.Picasso;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by Hitesh kumawat on 19-09-2016.
 */
public class MyTripListFragment extends AbstractFragment implements View.OnClickListener, CommonListener {

    private ViewPager vpMyList;
    private LinearLayout llCountDotsMain;
    private MyTripsModel myTripsModel = new MyTripsModel();
    private MyTripListVPAdapter myTripListVPAdapter;
    private RelativeLayout rlMyTripMain;
    private ImageView ticketImage;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();
    ArrayList<MyTripsData> tripListMain = new ArrayList<MyTripsData>();
    private View view;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.my_trip_list_fragment, container, false);
            init(view);
        }
        return view;
    }

    private void init(View view) {
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.my_trip));
        rlMyTripMain = (RelativeLayout) view.findViewById(R.id.rlMyTripMain);
        vpMyList = (ViewPager) view.findViewById(R.id.vpMyList);
        llCountDotsMain = (LinearLayout) view.findViewById(R.id.llCountDotsMain);
        TripFilter.addFilterView(getActivity(), view, this);
        viewPagerChangeListener();
        drawPageSelectionIndicators(0);
        getMyTripsList();
    }

    @Override
    protected BasicModel getModel() {
        return myTripsModel;
    }

    @Override
    public void update(Observable observable, Object o) {
        Util.dimissProDialog();
        if (o instanceof MyTripsData) {
            MyTripsData myTripsData = ((MyTripsData) o);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(myTripsData.status)) {
                tripListMain = myTripsData.response;
                myTripListVPAdapter = new MyTripListVPAdapter(getActivity(), this, tripListMain);
                vpMyList.setAdapter(myTripListVPAdapter);
                if (tripListMain.size() <= 0) {
                    rlMyTripMain.addView(Util.getViewDataNotFound(getActivity(), rlMyTripMain, getString(R.string.trip_unavailable)));
                }
            }
        } else if (o != null && o instanceof ParcelBookingChangeStatusData) {
            ParcelBookingChangeStatusData commonResponseData = ((ParcelBookingChangeStatusData) o);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(commonResponseData.status)) {
                getMyTripsList();
            } else
                rlMyTripMain.addView(Util.getViewDataNotFound(getActivity(), rlMyTripMain, commonResponseData.errorMessage));

        } else if (o instanceof CommonResponseData) {
            CommonResponseData responseData = ((CommonResponseData) o);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(responseData.status)) {
                getMyTripsList();
            }
        } else if (o instanceof RetrofitError) {
            Util.showSnakBar(rlMyTripMain, getResources().getString(R.string.pls_try_again));
            rlMyTripMain.addView(Util.getViewServerNotResponding(getActivity(), rlMyTripMain, new TryAgainInterface() {
                @Override
                public void callBack() {
                    Util.replaceFragment(getActivity(), R.id.fmContainerTransporterHomeMain, new MyTripListFragment());
                }
            }));
        }
    }

    private void getMyTripsList() {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(getActivity());
            myTripsModel.getUserTripList();
        } else {
            rlMyTripMain.addView(Util.getViewInternetNotFound(getActivity(), rlMyTripMain, new TryAgainInterface() {
                @Override
                public void callBack() {
                    addMainView();
                    getMyTripsList();
                }
            }));

        }
    }

    public void addMainView() {
        rlMyTripMain.removeAllViews();
        rlMyTripMain.addView(vpMyList);
        rlMyTripMain.addView(llCountDotsMain);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgCancelTrip) {
            final MyTripsData myTripsData = ((MyTripsData) view.getTag());


            if (Constants.TripRequestSent.equals(myTripsData.status) || Constants.TripBooked.equals(myTripsData.status)) {
                Util.showAlertWithCancelDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showAlertInputDialogBox(myTripsData);
                    }
                }, getString(R.string.trip_edit_if_booked));
            } else
                showAlertInputDialogBox(myTripsData);

        } else if (id == R.id.imgEditTrip) {
            MyTripsData myTripsData = (MyTripsData) view.getTag();
            AddTripFragment addTripFragment = new AddTripFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("tripData", myTripsData);
            addTripFragment.setArguments(bundle);
            Util.addFragmentWithOnBack(getActivity(), R.id.fmContainerTransporterHomeMain, addTripFragment);
        } else if (id == R.id.llBookedParcelsMyTripList || id == R.id.llFindParcelsMyTripList) {
            MyTripsData myTripsData = (MyTripsData) view.getTag();
            Intent intent = new Intent(getActivity(), MyTripParcelActivity.class);
            Bundle bundle = new Bundle();
            myTripsData.myClickOn = (id == R.id.llBookedParcelsMyTripList ? "Booked" : "Find");
            bundle.putSerializable("KEY_DATA", myTripsData);
            intent.putExtra("KEY_BUNDLE", bundle);
            startActivity(intent);
        } else if (id == R.id.imgViewTicketMyTripListRow) {
            MyTripsData myTripsData = ((MyTripsData) view.getTag());
            showWebViewDialog(myTripsData.image);
        }
    }

    private void cancelTrip(final HashMap<String, Object> requestData) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(getActivity());
            myTripsModel.cancelTrip(requestData);
        } else {
            rlMyTripMain.addView(Util.getViewInternetNotFound(getActivity(), rlMyTripMain, new TryAgainInterface() {
                @Override
                public void callBack() {
                    addMainView();
                    cancelTrip(requestData);
                }
            }));
        }
    }

    private void viewPagerChangeListener() {
        vpMyList.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    @Override
    public void filterData(FilterData filterData) {
        ArrayList<MyTripsData> filterList = new ArrayList<MyTripsData>();
        addMainView();
        if (!TextUtils.isEmpty(filterData.tripId)) {
            ArrayList<MyTripsData> filterList1 = new ArrayList<MyTripsData>();
            for (MyTripsData myTripsData : tripListMain) {
                if (myTripsData.id.equalsIgnoreCase(filterData.tripId)) {
                    filterList1.add(myTripsData);
                }
            }
            filterList.clear();
            filterList.addAll(filterList1);
        } else {
            filterList.addAll(tripListMain);
        }
        if (!TextUtils.isEmpty(filterData.departure_date)) {
            ArrayList<MyTripsData> filterList1 = new ArrayList<MyTripsData>();
            for (MyTripsData myTripsData : filterList) {
                if (Util.getDateFromDateTimeFormat(myTripsData.dep_time).equalsIgnoreCase(filterData.departure_date)) {
                    filterList1.add(myTripsData);
                }
            }
            filterList.clear();
            filterList.addAll(filterList1);
        }

        if (!TextUtils.isEmpty(filterData.tripStatus)) {
            ArrayList<MyTripsData> filterList1 = new ArrayList<MyTripsData>();
            for (MyTripsData myTripsData : filterList) {
                if (myTripsData.status.equalsIgnoreCase(filterData.tripStatus)) {
                    filterList1.add(myTripsData);
                }
            }
            filterList.clear();
            filterList.addAll(filterList1);
        }


        if (filterList.size() <= 0) {
            rlMyTripMain.addView(Util.getViewDataNotFound(getActivity(), rlMyTripMain, getString(R.string.trip_unavailable)));
        } else {
            vpMyList.setAdapter(new MyTripListVPAdapter(getActivity(), this, filterList));
        }
    }

    void showAlertInputDialogBox(final MyTripsData myTripsData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.reason_cancel_trip));
        final EditText input = new EditText(getActivity());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 8, 8, 8);
        input.setLayoutParams(layoutParams);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reason = input.getText().toString();
                if (!TextUtils.isEmpty(reason)) {
                    if (myTripsData != null) {
                        HashMap<String, Object> requestData = new HashMap<>();
                        requestData.put("id", myTripsData.id);
                        requestData.put("status", Constants.TripCanceled);
                        requestData.put("process_by", Config.getUserId());
                        requestData.put("reason", reason);
                        cancelTrip(requestData);
                    }
                } else {
                    Util.showOKSnakBar(rlMyTripMain, getString(R.string.valid_trip_reason));
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    void showWebViewDialoga(String url) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        WebView webView = new WebView(getActivity());
        webView.setInitialScale(1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.loadUrl("http://gallery.yopriceville.com/var/albums/Free-Clipart-Pictures/Airplanes-PNG-Clipart/Airplane_PNG_Clipart.png?m=1433777009");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        alert.setView(webView);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    void showWebViewDialog(String imageName) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        ImageView imageView = new ImageView(getActivity());
        String imageUrl = Constants.image_url + imageName;
        imageView.setImageResource(R.mipmap.ticket);
        Picasso.with(getActivity()).load(imageUrl).placeholder(getResources().getDrawable(R.mipmap.alert_circle)).into(imageView);
        alert.setView(imageView);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

}
