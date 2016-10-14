package com.pedrodavidlp.footballmanager.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedrodavidlp.footballmanager.R;
import com.pedrodavidlp.footballmanager.data.UserRepository;
import com.pedrodavidlp.footballmanager.domain.interactor.CreateUserUseCase;
import com.pedrodavidlp.footballmanager.presenter.UserPresenter;
import com.pedrodavidlp.footballmanager.view.ViewTry;
import com.pedrodavidlp.footballmanager.view.activity.JoinGroupActivity;
import com.pedrodavidlp.footballmanager.view.executor.MainThreadImp;
import com.tonilopezmr.interactorexecutor.Executor;
import com.tonilopezmr.interactorexecutor.MainThread;
import com.tonilopezmr.interactorexecutor.ThreadExecutor;

public class InsertNickFragment extends Fragment implements ViewTry {
    private TextInputEditText insertNick;
    private TextInputLayout insertNickLayout;
    private UserPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insert_nickname,container,false);
        insertNick = (TextInputEditText) view.findViewById(R.id.insertNickName);
        insertNickLayout = (TextInputLayout) view.findViewById(R.id.insertNickNameLayout);
        MainThread mainThread = new MainThreadImp();
        Executor executor = new ThreadExecutor();
        UserRepository repository = new UserRepository(getContext());
        CreateUserUseCase userUseCase = new CreateUserUseCase(mainThread,executor,repository);
        presenter = new UserPresenter(userUseCase);
        presenter.setView(this);
        presenter.init();
        return view;
    }

    @Override
    public void initUi() {
        ((JoinGroupActivity) getActivity()).setListenerToFab(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.insertNickname(insertNick.getText().toString());
                ((JoinGroupActivity) getActivity()).startAnimationFab();
            }
        });
    }

    @Override
    public void error(Exception e) {

    }

    @Override
    public void succeed() {
        ((JoinGroupActivity) getActivity()).setFragment(1);
    }

    @Override
    public void failed() {
        ((JoinGroupActivity) getActivity()).stopAnimationFab();
        insertNickLayout.setError("El nombre de usuario ya existe");
        insertNick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                insertNickLayout.setError(null);
                insertNick.removeTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
