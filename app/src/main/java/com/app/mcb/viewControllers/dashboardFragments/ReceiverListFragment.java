package com.app.mcb.viewControllers.dashboardFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.Utility.Util;
import com.app.mcb.adapters.ReceiverListAdapter;
import com.app.mcb.viewControllers.sender.ParcelDetailsFragment;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

/**
 * Created by Hitesh kumawat on 19-09-2016.
 */
public class ReceiverListFragment extends AbstractFragment implements View.OnClickListener {

    private ViewPager vpReceiverList;
    private LinearLayout llCountDotsMain;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receiver_list_fragment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        vpReceiverList = (ViewPager) view.findViewById(R.id.vpReceiverList);
        llCountDotsMain = (LinearLayout) view.findViewById(R.id.llCountDotsMain);
        vpReceiverList.setAdapter(new ReceiverListAdapter(getActivity(), this));
        drawPageSelectionIndicators(0);
        ((MainActivity) getActivity()).setHeader(getResources().getString(R.string.receiver_details));
        viewPagerChangeListener();
    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable observable, Object o) {

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
        if (id == R.id.imgViewParcelListRow) {
            Util.replaceFragment(getActivity(),R.id.fmContainerSenderHomeMain,new ParcelDetailsFragment());
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
}
