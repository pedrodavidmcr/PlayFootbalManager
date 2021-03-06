package com.pedrodavidlp.footballmanager.view.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedrodavidlp.footballmanager.FootballApplication;
import com.pedrodavidlp.footballmanager.R;
import com.pedrodavidlp.footballmanager.di.player.PlayerFragmentModule;
import com.pedrodavidlp.footballmanager.domain.model.Player;
import com.pedrodavidlp.footballmanager.presenter.ListPlayersPresenter;
import com.pedrodavidlp.footballmanager.view.ViewList;
import com.pedrodavidlp.footballmanager.view.activity.MainActivity;
import com.pedrodavidlp.footballmanager.view.adapter.ListPlayersAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListPlayersFragment extends Fragment implements ViewList<Player>,ListPlayersAdapter.OnItemLongClickListener{
    private ListPlayersAdapter adapter;
    @BindView(R.id.playersRecView) RecyclerView listPlayers;
    @BindView(R.id.loadingListPlayers) AVLoadingIndicatorView loading;

    @Inject ListPlayersPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initDagger();

        View view=inflater.inflate(R.layout.fragment_list,container,false);
        ButterKnife.bind(this,view);

        presenter.setView(this);
        presenter.init();

        return view;
    }

    private void initDagger() {
        FootballApplication.get(getAppContext())
                .getPlayerComponent()
                .plus(new PlayerFragmentModule())
                .inject(this);
    }

    private Context getAppContext() {
        return  getActivity().getApplicationContext();
    }

    @Override
    public void loadList(List<Player> list) {
        adapter.setData(list);
        adapter.notifyDataSetChanged();
        listPlayers.setVisibility(View.VISIBLE);
        loading.hide();
    }

    @Override
    public void initUi() {
        adapter = new ListPlayersAdapter(this);
        listPlayers.setAdapter(adapter);
        listPlayers.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void error(Exception e) {
        Snackbar.make(getView(),"Se ha producido un error",Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loading.show();
        presenter.loadList();
    }

    @Override
    public boolean onItemLongClicked(View v) {
        ((MainActivity) getActivity()).setFabMenu();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_normal));
        }else {
            v.setBackgroundColor(getResources().getColor(R.color.blue_normal));
        }
        return false;
    }
}
