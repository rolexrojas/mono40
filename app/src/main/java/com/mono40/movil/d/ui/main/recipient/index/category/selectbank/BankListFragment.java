package com.mono40.movil.d.ui.main.recipient.index.category.selectbank;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mono40.movil.R;
import com.mono40.movil.api.Api;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.d.domain.AccountRecipient;
import com.mono40.movil.d.domain.Recipient;
import com.mono40.movil.d.domain.RecipientManager;
import com.mono40.movil.d.ui.ChildFragment;
import com.mono40.movil.d.ui.main.MainContainer;
import com.mono40.movil.d.ui.main.recipient.index.category.OnSaveButtonClickedListener;
import com.mono40.movil.d.ui.main.recipient.index.category.RecipientAdditionDialogFragment;
import com.mono40.movil.dep.App;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BankListFragment extends ChildFragment<MainContainer> implements OnSaveButtonClickedListener {
    private static final String FRAGMENT_ARGS_RECIPIENT = "FRAGMENT_ARGS_RECIPIENT";

    public static BankListFragment create(Recipient recipient) {
        Bundle args = new Bundle();
        args.putParcelable(FRAGMENT_ARGS_RECIPIENT, recipient);
        BankListFragment fragment = new BankListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.recycler_view)
    RecyclerView bankListView;
    @Inject
    Api api;
    @Inject
    CompanyHelper companyHelper;
    @Inject
    public RecipientManager recipientManager;
    Disposable banksFetchDisposable;
    Recipient recipient;

    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerBankSelectComponent.builder()
                .appComponent(((App) getContext().getApplicationContext()).component())
                .build().inject(this);
        if (getArguments() != null) {
            recipient = getArguments().getParcelable(FRAGMENT_ARGS_RECIPIENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.d_fragment_recipient_candidate_list, container,
                false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bankListView.setLayoutManager(new LinearLayoutManager(getContext()));
        bankListView.setAdapter(new BanksAdapter(new ArrayList<>(), null));
        banksFetchDisposable = this.api.fetchBanks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(banks -> bankListView.setAdapter(new BanksAdapter(banks, item -> {
                    if (!item.id().equalsIgnoreCase("DKT") && !item.id().equalsIgnoreCase("CTB")) {
                        ((AccountRecipient) recipient).bank(item);
                    }
                    RecipientAdditionDialogFragment.create(recipient)
                            .show(getChildFragmentManager(), null);
                }, companyHelper)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (banksFetchDisposable != null) {
            banksFetchDisposable.dispose();
        }
    }

    @Override
    public void onSaveButtonClicked(Recipient recipient, String label) {
        recipient.setLabel(label);
        recipientManager.update(recipient);
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.popBackStack();
        }
    }
}
