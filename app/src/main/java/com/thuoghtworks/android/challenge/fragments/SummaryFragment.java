package com.thuoghtworks.android.challenge.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thuoghtworks.android.challenge.BattleApplication;
import com.thuoghtworks.android.challenge.R;
import com.thuoghtworks.android.challenge.adapter.KingsAdapter;
import com.thuoghtworks.android.challenge.db.Battle;
import com.thuoghtworks.android.challenge.db.BattleDao;
import com.thuoghtworks.android.challenge.db.King;
import com.thuoghtworks.android.challenge.db.KingDao;
import com.thuoghtworks.android.challenge.http.ObjectFactory;
import com.thuoghtworks.android.challenge.widget.AppProgressDialog;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SummaryFragment extends Fragment {


    private Context mContext;

    public SummaryFragment() {
        // Required empty public constructor
    }

    public static SummaryFragment newInstance(String param1, String param2) {
        SummaryFragment fragment = new SummaryFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summary, container, false);
        mContext = getActivity();
        initScreen(rootView);
        return rootView;
    }

    private void initScreen(View rootView) {
        KingDao kingDao = BattleApplication.getDaoSession().getKingDao();
        List<King> kings = kingDao.loadAll();
        KingsAdapter kingsAdapter = new KingsAdapter(mContext, kings);
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.kings_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(kingsAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
