package com.app.mcb.viewControllers.dashboardFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.mcb.R;
import com.app.mcb.adapters.MyWalletAdapter;
import com.app.mcb.adapters.TripListCommonAdapter;
import com.app.mcb.viewControllers.TripListWithState;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

/**
 * Created by u on 9/15/2016.
 */
public class MyWalletFragment extends AbstractFragment implements View.OnClickListener {

    private RecyclerView rvTripListMyWallet;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            View rootView = inflater.inflate(R.layout.my_wallet, container, false);
            init(rootView);
            return rootView;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void init(View rootView) {
        rvTripListMyWallet = (RecyclerView) rootView.findViewById(R.id.rvTripListMyWallet);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvTripListMyWallet.setLayoutManager(llm);
        rvTripListMyWallet.setAdapter(new MyWalletAdapter(getActivity()));
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

    }
}
