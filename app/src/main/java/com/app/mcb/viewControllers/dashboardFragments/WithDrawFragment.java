package com.app.mcb.viewControllers.dashboardFragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.mcb.MainActivity;
import com.app.mcb.R;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

/**
 * Created by u on 9/15/2016.
 */
public class WithDrawFragment extends AbstractFragment implements View.OnClickListener {

    private RelativeLayout rlWithDrawMain;
    private TextView txtWithDrawStatus;
    private TextView txtNewUserWithDraw;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.withdraw, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        rlWithDrawMain = (RelativeLayout) view.findViewById(R.id.rlWithDrawMain);
        txtWithDrawStatus = (TextView) view.findViewById(R.id.txtWithDrawStatus);
        txtNewUserWithDraw = (TextView) view.findViewById(R.id.txtNewUserWithDraw);
        txtWithDrawStatus.setOnClickListener(this);
        txtNewUserWithDraw.setOnClickListener(this);
        addViewInRelayout(R.layout.withdraw_status);
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.withdraw));


    }

    private void addViewInRelayout(int layout) {
        rlWithDrawMain.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(layout, null, false);
        rlWithDrawMain.addView(relativeLayout);
    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.txtWithDrawStatus) {
            addViewInRelayout(R.layout.withdraw_status);
            unSelectTextView();
            selectLeftTextView();
        } else if (id == R.id.txtNewUserWithDraw) {
            addViewInRelayout(R.layout.create_new_withdraw);
            unSelectTextView();
            selectRightTextView();
        }

    }

    private void unSelectTextView() {
        txtWithDrawStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        txtNewUserWithDraw.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            txtNewUserWithDraw.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rect_right_corners_pink_border_grey_bg));
            txtWithDrawStatus.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rect_left_corners_pink_border_grey_bg));
        }
    }

    private void selectLeftTextView() {
        txtWithDrawStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            txtWithDrawStatus.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rect_left_corners_pink_bg));
        }
    }

    private void selectRightTextView() {
        txtNewUserWithDraw.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            txtNewUserWithDraw.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rect_right_corners_pink_bg));
        }
    }
}
