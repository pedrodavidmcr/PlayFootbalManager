package com.pedrodavidlp.footballmanager.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.animation.AnimationUtils;

import com.pedrodavidlp.footballmanager.FootballApplication;
import com.pedrodavidlp.footballmanager.R;
import com.pedrodavidlp.footballmanager.di.launcher.LauncherActivityModule;
import com.pedrodavidlp.footballmanager.domain.interactor.SelectStateUseCase;
import com.pedrodavidlp.footballmanager.presenter.LauncherPresenter;
import com.pedrodavidlp.footballmanager.view.ViewMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LauncherActivity extends AppCompatActivity implements ViewMode {
    @BindView(R.id.image_loading) AppCompatImageView imageView;
    private final String TAG = getClass().getSimpleName();

    @Inject LauncherPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        ButterKnife.bind(this);
        initDagger();

        presenter.setView(this);
        presenter.init();
    }

    private void initDagger() {
        FootballApplication.get(getApplicationContext())
                .getLauncherComponent()
                .plus(new LauncherActivityModule())
                .inject(this);
    }

    @Override
    public void initUi(int mode) {
        Intent intent=null;
        Log.d(TAG, "initUi: "+mode);
        switch (mode){
            case SelectStateUseCase.NO_CONNECTION:
                Snackbar.make(getCurrentFocus().getRootView(),"No hay conexion a internet",Snackbar.LENGTH_INDEFINITE).show();
                return;
            case SelectStateUseCase.NOT_LOGGED:
                intent = new Intent(getApplicationContext(),LoginActivity.class);
                break;
            case SelectStateUseCase.NO_NICKNAME:
                intent = new Intent(getApplicationContext(),JoinGroupActivity.class);
                intent.putExtra("fragment",0);
                break;
            case SelectStateUseCase.NO_GROUP:
                intent = new Intent(getApplicationContext(),JoinGroupActivity.class);
                intent.putExtra("fragment",1);
                break;
            case SelectStateUseCase.ADMIN_USER:
                intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("admin",true);
                break;
            case SelectStateUseCase.NORMAL_USER:
                intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("admin",false);
                break;
        }
        startActivity(intent);
        finish();

    }

    @Override
    public void initUi() {
        imageView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate));
    }

    @Override
    public void error(Exception e) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FootballApplication.get(getApplicationContext())
                .releaseLauncherComponent();
    }
}