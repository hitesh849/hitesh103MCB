package com.app.mcb.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mcb.R;
import com.app.mcb.Utility.Constants;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.ReceiverData;

import java.util.ArrayList;

/**
 * Created by Hitesh kumawat on 19-09-2016.
 */
public class ReceiverListAdapter extends PagerAdapter {

    private Context mContext;
    private TextView txtFromCityShortReceiver;
    private TextView txtFromCityLongReceiver;
    private TextView txtToCityShortReceiver;
    private TextView txtToCityLongReceiver;
    private TextView txtFlightNumReceiver;
    private TextView txtFromDateReceiver;
    private TextView txtToDateReceiver;
    private TextView txtFromTimeReceiver;
    private TextView txtToTimeReceiver;
    private TextView txtParcelIdReceiver;
    private TextView txtParcelTypeReceiver;
    private TextView txtParcelWeightReceiver;
    private TextView txtTransporterIdReceiver;
    private TextView txtParcelStatusReceiver;
    private View.OnClickListener onClickListener;
    private ArrayList<ReceiverData> receiverDataList;

    public ReceiverListAdapter(Context context, ArrayList<ReceiverData> receiverDataList, View.OnClickListener onClickListener) {
        mContext = context;
        this.onClickListener = onClickListener;
        this.receiverDataList = receiverDataList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.receiver_details_row, collection, false);
        init(layout);
        setValue(position);
        collection.addView(layout);
        return layout;
    }

    private void init(ViewGroup viewGroup) {
        txtFromCityShortReceiver = (TextView) viewGroup.findViewById(R.id.txtFromCityShortReceiver);
        txtFromCityLongReceiver = (TextView) viewGroup.findViewById(R.id.txtFromCityLongReceiver);
        txtToCityShortReceiver = (TextView) viewGroup.findViewById(R.id.txtToCityShortReceiver);
        txtToCityLongReceiver = (TextView) viewGroup.findViewById(R.id.txtToCityLongReceiver);
        txtFlightNumReceiver = (TextView) viewGroup.findViewById(R.id.txtFlightNumReceiver);
        txtFromDateReceiver = (TextView) viewGroup.findViewById(R.id.txtFromDateReceiver);
        txtToDateReceiver = (TextView) viewGroup.findViewById(R.id.txtToDateReceiver);
        txtFromTimeReceiver = (TextView) viewGroup.findViewById(R.id.txtFromTimeReceiver);
        txtToTimeReceiver = (TextView) viewGroup.findViewById(R.id.txtToTimeReceiver);
        txtParcelIdReceiver = (TextView) viewGroup.findViewById(R.id.txtParcelIdReceiver);
        txtParcelTypeReceiver = (TextView) viewGroup.findViewById(R.id.txtParcelTypeReceiver);
        txtParcelWeightReceiver = (TextView) viewGroup.findViewById(R.id.txtParcelWeightReceiver);
        txtTransporterIdReceiver = (TextView) viewGroup.findViewById(R.id.txtTransporterIdReceiver);
        txtParcelStatusReceiver = (TextView) viewGroup.findViewById(R.id.txtParcelStatusReceiver);
    }


    private void setValue(int position) {
        ReceiverData receiverData = receiverDataList.get(position);
        txtFromCityShortReceiver.setText(Util.getFirstName(receiverData.source));
        txtFromCityLongReceiver.setText("("+receiverData.source+")");
        txtToCityShortReceiver.setText(Util.getFirstName(receiverData.destination));
        txtToCityLongReceiver.setText("("+receiverData.destination+")");
        txtFlightNumReceiver.setText(receiverData.flight_no);
        txtFromDateReceiver.setText(Util.getDateFromDateTimeFormat(receiverData.arrival_time));
        txtToDateReceiver.setText(Util.getDateFromDateTimeFormat(receiverData.dep_time));
        txtFromTimeReceiver.setText(Util.getTimeFromDateTimeFormat(receiverData.arrival_time));
        txtToTimeReceiver.setText(Util.getTimeFromDateTimeFormat(receiverData.dep_time));
        txtParcelIdReceiver.setText(receiverData.ParcelID);
        txtTransporterIdReceiver.setText(receiverData.TripID);
        txtParcelStatusReceiver.setText(receiverData.statusdescription);
        if (!TextUtils.isEmpty(receiverData.weight))
            txtParcelWeightReceiver.setText(receiverData.weight + " " + "Kg");
        else
            txtParcelWeightReceiver.setText("");

        if ("E".equals(receiverData.type))
            txtParcelTypeReceiver.setText(Constants.ENVELOPE);
        else if ("B".equals(receiverData.type))
            txtParcelTypeReceiver.setText(Constants.BOX);
    }

    @Override
    public int getCount() {
        return receiverDataList.size();
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
