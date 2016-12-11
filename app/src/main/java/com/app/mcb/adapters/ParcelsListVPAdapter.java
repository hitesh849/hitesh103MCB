package com.app.mcb.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.ParcelDetailsData;
import com.app.mcb.dao.ParcelListData;
import com.app.mcb.dao.TripTransporterData;
import com.app.mcb.sharedPreferences.Config;

import java.util.ArrayList;

/**
 * Created by Hitesh kumawat on 18-09-2016.
 */
public class ParcelsListVPAdapter extends PagerAdapter {

    private Context mContext;
    private View.OnClickListener onClickListener;
    private ImageView imgViewParcelListRow;
    private ImageView imgSettingsParcelList;
    private TextView txtFromCityShortPl;
    private TextView txtFromCityLongPl;
    private TextView txtToCityShortPl;
    private TextView txtToCityLongPl;
    private TextView txtFromDatePl;
    private TextView txtToDatePl;
    private TextView txtFromTimePl;
    private TextView txtToTimePl;
    private TextView txtParcelIdPL;
    private TextView txtPrcelTypePL;
    private TextView txtPrcelWeightPL;
    private TextView txtTransporterIdPL;
    private TextView txtReceiverIdPL;
    private TextView txtParcelAmount;
    private TextView txtBookedPL;
    private ImageView imgViewPLR;
    private ImageView imgEditPLR;
    private ImageView imgCancelPLR;
    private ImageView imgSettingsPLR;
    private ImageView imgChatParcelList;
    private LinearLayout llParcelActionMain;
    private ArrayList<ParcelDetailsData> parcelList;

    public ParcelsListVPAdapter(Context context, ArrayList<ParcelDetailsData> parcelList, View.OnClickListener onClickListener) {
        mContext = context;
        this.parcelList = parcelList;
        this.onClickListener = onClickListener;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.parcels_list_row, collection, false);
        init(layout);
        setValues(position);
        collection.addView(layout);
        return layout;
    }

    private void init(ViewGroup viewGroup) {
        imgSettingsPLR = (ImageView) viewGroup.findViewById(R.id.imgSettingsPLR);
        imgChatParcelList = (ImageView) viewGroup.findViewById(R.id.imgChatParcelList);
        txtFromCityShortPl = (TextView) viewGroup.findViewById(R.id.txtFromCityShortPl);
        txtFromCityLongPl = (TextView) viewGroup.findViewById(R.id.txtFromCityLongPl);
        txtToCityShortPl = (TextView) viewGroup.findViewById(R.id.txtToCityShortPl);
        txtToCityLongPl = (TextView) viewGroup.findViewById(R.id.txtToCityLongPl);
        txtFromDatePl = (TextView) viewGroup.findViewById(R.id.txtFromDatePl);
        txtToDatePl = (TextView) viewGroup.findViewById(R.id.txtToDatePl);
        txtFromTimePl = (TextView) viewGroup.findViewById(R.id.txtFromTimePl);
        txtToTimePl = (TextView) viewGroup.findViewById(R.id.txtToTimePl);
        txtParcelIdPL = (TextView) viewGroup.findViewById(R.id.txtParcelIdPL);
        txtPrcelTypePL = (TextView) viewGroup.findViewById(R.id.txtPrcelTypePL);
        txtParcelAmount = (TextView) viewGroup.findViewById(R.id.txtParcelAmount);
        txtPrcelWeightPL = (TextView) viewGroup.findViewById(R.id.txtPrcelWeightPL);
        txtTransporterIdPL = (TextView) viewGroup.findViewById(R.id.txtTransporterIdPL);
        txtReceiverIdPL = (TextView) viewGroup.findViewById(R.id.txtReceiverIdPL);
        txtBookedPL = (TextView) viewGroup.findViewById(R.id.txtBookedPL);
        imgViewPLR = (ImageView) viewGroup.findViewById(R.id.imgViewPLR);
        imgEditPLR = (ImageView) viewGroup.findViewById(R.id.imgEditPLR);
        imgCancelPLR = (ImageView) viewGroup.findViewById(R.id.imgCancelPLR);
        llParcelActionMain = (LinearLayout) viewGroup.findViewById(R.id.llParcelActionMain);
        imgViewPLR.setOnClickListener(onClickListener);
        imgSettingsPLR.setOnClickListener(onClickListener);
        imgCancelPLR.setOnClickListener(onClickListener);
        imgEditPLR.setOnClickListener(onClickListener);
        imgChatParcelList.setOnClickListener(onClickListener);
    }

    private void setValues(int position) {
        ParcelDetailsData parcelListData = parcelList.get(position);
        txtFromCityShortPl.setText(Util.getFirstName(parcelListData.source));
        txtFromCityLongPl.setText(parcelListData.source);
        txtToCityShortPl.setText(Util.getFirstName(parcelListData.destination));
        txtToCityLongPl.setText(parcelListData.destination);
        txtParcelIdPL.setText(parcelListData.ParcelID);
        txtFromDatePl.setText(Util.getDateFromDateTimeFormat(parcelListData.created));
        txtFromTimePl.setText(Util.getTimeFromDateTimeFormat(parcelListData.created));
        txtToDatePl.setText(Util.getDDMMYYYYFormat(parcelListData.till_date, "yyyy-MM-dd"));
        txtParcelAmount.setText(parcelListData.payment+" "+"\u20B9");
        imgViewPLR.setTag(parcelListData);
        imgEditPLR.setTag(parcelListData);
        imgCancelPLR.setTag(parcelListData);
        imgChatParcelList.setTag(parcelListData);
        imgSettingsPLR.setTag(parcelListData);
        if ("E".equals(parcelListData.type))
            txtPrcelTypePL.setText(Constants.ENVELOPE);
        else if ("B".equals(parcelListData.type))
            txtPrcelTypePL.setText(Constants.BOX);
        else if ("P".equals(parcelListData.type))
            txtPrcelTypePL.setText(Constants.PACKET);

        if (!TextUtils.isEmpty(parcelListData.weight))
            txtPrcelWeightPL.setText(parcelListData.weight + " " + "KG");
        else
            txtPrcelWeightPL.setText("");


        if ((!TextUtils.isEmpty(parcelListData.trans_id)) && (!parcelListData.trans_id.equals("0")))
            txtTransporterIdPL.setText(Constants.BEGIN_WITH_TRANSPORTER_ID + parcelListData.trans_id);
        else
            txtTransporterIdPL.setText("-");

        if (!TextUtils.isEmpty(parcelListData.recv_id))
            txtReceiverIdPL.setText(Constants.BEGIN_WITH_USER_ID + parcelListData.recv_id);
        else
            txtReceiverIdPL.setText("-");

        if (!TextUtils.isEmpty(parcelListData.statusdescription))
            txtBookedPL.setText(parcelListData.statusdescription);
        else
            txtBookedPL.setText("");

        setViewVisible();
        switch (parcelListData.status) {
            case Constants.ParcelIdCreated:
                imgChatParcelList.setVisibility(View.GONE);
                imgSettingsPLR.setVisibility(View.GONE);
                break;
            case Constants.ParcelPaymentDue:
                imgChatParcelList.setVisibility(View.GONE);
                break;
            case Constants.ParcelDeliveryComplete:
                imgSettingsPLR.setVisibility(View.GONE);
                imgCancelPLR.setVisibility(View.GONE);
                imgEditPLR.setVisibility(View.GONE);
                break;
            case Constants.ParcelCancelled:
                llParcelActionMain.setVisibility(View.GONE);
                break;
            case Constants.ParcelBookedWithTR:
                imgSettingsPLR.setVisibility(View.GONE);
                break;
            case Constants.ParcelCollected:
                imgSettingsPLR.setVisibility(View.GONE);
                imgCancelPLR.setVisibility(View.GONE);
                imgEditPLR.setVisibility(View.GONE);
                break;
            case Constants.ParcelDelivered:
                imgCancelPLR.setVisibility(View.GONE);
                imgEditPLR.setVisibility(View.GONE);
                break;
        }
    }

    void setViewVisible() {
        imgChatParcelList.setVisibility(View.VISIBLE);
        imgSettingsPLR.setVisibility(View.VISIBLE);
        llParcelActionMain.setVisibility(View.VISIBLE);
    }

    @Override
    public int getCount() {
        return parcelList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

}

