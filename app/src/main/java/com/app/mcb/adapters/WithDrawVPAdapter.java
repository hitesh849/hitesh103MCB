package com.app.mcb.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.dao.MyTripsData;
import com.app.mcb.dao.WithDrawData;

import java.util.ArrayList;

/**
 * Created by Hitesh on 16-10-2016.
 */
public class WithDrawVPAdapter extends PagerAdapter {

    private ArrayList<WithDrawData> withDrawDataList;
    private Context mContext;
    private View.OnClickListener onClickListener;
    private TextView txtBankNameWithDraw;
    private TextView txtAmountWithDraw;
    private TextView txtIFSCCodeWithDraw;
    private TextView txtStatusWithDraw;
    private TextView txtProcessingDateWithDraw;

    public WithDrawVPAdapter(Context context, ArrayList<WithDrawData> withDrawDataList) {
        mContext = context;
        this.withDrawDataList = withDrawDataList;
    }

    @Override
    public int getCount() {
        return withDrawDataList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        WithDrawData withDrawData = withDrawDataList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.withdraw_status_vp_row, collection, false);
        init(layout, withDrawDataList);
        setValue(withDrawData);
        collection.addView(layout);
        return layout;
    }

    private void setValue(WithDrawData withDrawData)
    {
        txtBankNameWithDraw.setText(withDrawData.bank_name);
        txtAmountWithDraw.setText(withDrawData.amount);
        txtIFSCCodeWithDraw.setText(withDrawData.ifsc);
        txtStatusWithDraw.setText(withDrawData.status);
        txtProcessingDateWithDraw.setText(Util.getDateFromDateTimeFormat(withDrawData.created));
    }

    private void init(ViewGroup view, ArrayList<WithDrawData> withDrawDataList) {
        txtBankNameWithDraw = (TextView) view.findViewById(R.id.txtBankNameWithDraw);
        txtAmountWithDraw = (TextView) view.findViewById(R.id.txtAmountWithDraw);
        txtIFSCCodeWithDraw = (TextView) view.findViewById(R.id.txtIFSCCodeWithDraw);
        txtStatusWithDraw = (TextView) view.findViewById(R.id.txtStatusWithDraw);
        txtProcessingDateWithDraw = (TextView) view.findViewById(R.id.txtProcessingDateWithDraw);
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
