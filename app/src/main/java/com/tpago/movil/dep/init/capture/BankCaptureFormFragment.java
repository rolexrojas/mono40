package com.tpago.movil.dep.init.capture;

import android.content.Context;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.R;
import com.tpago.movil.api.Api;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.company.bank.BankStore;
import com.tpago.movil.dep.content.StringResolver;
import com.tpago.movil.dep.init.InitActivityBase;
import com.tpago.movil.dep.init.PhoneNumberInitFragment;
import com.tpago.movil.util.ObjectHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BankCaptureFormFragment
        extends CaptureFormFragment<BankCaptureFormPresenter>
        implements BankCaptureFormPresenter.View {

    static BankCaptureFormFragment create() {
        return new BankCaptureFormFragment();
    }

    final private int NO_BANK_SELECTED = -1;

    private BankCaptureFormPresenter presenter;
    private Unbinder unbinder;
    private int selectedBank = NO_BANK_SELECTED;
    private Adapter adapter = new Adapter();
    private List<Bank> banks;

    @Inject
    AlertManager alertManager;
    @Inject
    StringResolver stringResolver;
    @Inject
    CaptureData captureData;
    @Inject
    BankStore bankStore;
    @Inject
    CompanyHelper companyHelper;
    @Inject
    Api api;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.continue_capture)
    TextView continueTextView;


    @Override
    protected BankCaptureFormPresenter getPresenter() {
        if (ObjectHelper.isNull(presenter)) {
            presenter = new BankCaptureFormPresenter(this, stringResolver, captureData, api, bankStore, alertManager, this);
        }
        return presenter;
    }

    @Override
    protected Fragment getNextScreen() {
        return PhoneNumberInitFragment.create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Injects all the annotated dependencies.
        getCaptureComponent()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_non_affiliated_info_capture, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        continueTextView.setVisibility(View.INVISIBLE);
        unbinder = ButterKnife.bind(this, view);
        swipeRefreshLayout.setEnabled(false);
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        final Context context = getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
        ));
        final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(context)
                .drawable(R.drawable.divider_line_horizontal)
                .marginResId(R.dimen.space_horizontal_20)
                .showLastDivider()
                .build();
        recyclerView.addItemDecoration(divider);
        getPresenter().fetchBanks();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.banks = new ArrayList<>();
        this.banks.addAll(
                this.bankStore.getAll()
                        .defaultIfEmpty(new ArrayList<>())
                        .blockingGet()
        );
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.unbinder.unbind();
    }

    @OnClick(R.id.continue_capture)
    void onContinueClick() {
        if (selectedBank != NO_BANK_SELECTED) {
            getPresenter().onContinue();
        }
    }

    @Override
    public void moveToNextScreen() {
        super.moveToNextScreen();
        getActivity().finish();
        getContext().startActivity(
                InitActivityBase.getLaunchIntent(getContext())
        );
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView textView;
        private ImageView indicator;

        private int id;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.image_view_background);
            textView = itemView.findViewById(R.id.text_view);
            indicator = itemView.findViewById(R.id.indicator);
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            selectedBank = id;
            Bank bank = banks.get(selectedBank);
            getPresenter().selectedBank(bank);
            adapter.notifyDataSetChanged();

            continueTextView.setVisibility(View.VISIBLE);
        }
    }

    class Adapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.list_item_bank_capture, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Bank bank = banks.get(position);
            Picasso.get()
                    .load(companyHelper.getLogoUri(bank, Company.LogoStyle.COLORED_24))
                    .noFade()
                    .into(holder.imageView);
            holder.textView.setText(bank.name());

            holder.indicator.setVisibility(position == selectedBank ? View.VISIBLE : View.INVISIBLE);
            holder.setId(position);
        }

        @Override
        public int getItemCount() {
            return banks.size();
        }
    }
}
