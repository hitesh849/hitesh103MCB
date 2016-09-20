package com.app.mcb.viewControllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.mcb.MainActivity;
import com.app.mcb.R;
import com.app.mcb.adapters.TripListCommonAdapter;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

/**
 * Created by Hitesh kumawat on 14-09-2016.
 */
public class TripListWithAllStateFragment extends AbstractFragment implements View.OnClickListener {

    private RecyclerView rvTripHome;
    private LinearLayout llBecomeTransporter;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            View rootView = inflater.inflate(R.layout.home_fragment, container, false);
            init(rootView);
            return rootView;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void init(View rootView) {
        rvTripHome = (RecyclerView) rootView.findViewById(R.id.rvTripHome);
        llBecomeTransporter = (LinearLayout) rootView.findViewById(R.id.llBecomeTransporter);
        llBecomeTransporter.setOnClickListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvTripHome.setLayoutManager(llm);
        rvTripHome.setAdapter(new TripListCommonAdapter(getActivity(), this, true));
        ((MainActivity) getActivity()).setHeader("Welcome");
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
        if (id == R.id.txtViewAllStateRow) {
            //rvTripHome.setAdapter(new TripListCommonAdapter(getActivity(), this, false));
            Intent intent = new Intent(getActivity(), TripListWithStateActivity.class);
            startActivity(intent);
        } else if (id == R.id.llBecomeTransporter) {
            Intent intent = new Intent(getActivity(), SignUpActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.llHomeRowMain) {

            startActivity(new Intent(getActivity(), TripDetailsActivity.class));
        }
    }
}
