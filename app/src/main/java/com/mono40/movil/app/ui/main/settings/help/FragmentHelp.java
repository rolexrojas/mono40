package com.mono40.movil.app.ui.main.settings.help;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.mono40.movil.R;
import com.mono40.movil.app.ui.fragment.base.FragmentBase;
import com.mono40.movil.app.ui.main.settings.bankdetail.BankDetailFragment;
import com.mono40.movil.company.Company;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.company.bank.BankStore;
import com.mono40.movil.d.ui.main.DepMainActivityBase;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

public final class FragmentHelp extends FragmentBase implements HelpPresentation {

    public static FragmentHelp create() {
        return new FragmentHelp();
    }

    private static List<Bank> banks;
    private Adapter adapter = new Adapter();
    private int selectedBankCode;
    private String selectedBankName;
    private String selectedBankLogoURL;

    @Inject
    BankStore bankStore;
    @Inject
    CompanyHelper companyHelper;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected int layoutResId() {
        return R.layout.fragment_help;
    }

    @OnClick(R.id.setting_option_faq)
    final void onFaqPressed() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFrameLayout, FragmentHelpFaq.create(), "fragmentHelpFaq")
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.setting_option_mail_customer_service)
    final void onEmailPressed() {
        String URL = getString(R.string.tpagoemail);
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + URL));
        startActivity(i);
    }

    @OnClick(R.id.setting_option_call_customer_service)
    final void onPhonePressed() {
        String URL = getString(R.string.tpagophone);
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + URL));
        startActivity(i);
    }


    @Override
    public void onStart() {
        super.onStart();

        DepMainActivityBase.get(this.getActivity())
                .toolbar()
                .setTitle(R.string.help);
    }

    @Override
    public void moveToNextScreen() {

        BankDetailFragment bankDetailFragment =
                BankDetailFragment.newInstance(selectedBankCode, selectedBankName, selectedBankLogoURL);

        this.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFrameLayout, bankDetailFragment, "bankDetailFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DepMainActivityBase.get(getActivity()).getComponent().inject(this);
        List<Bank> allBanks = this.bankStore.getAll().flatMapObservable(Observable::fromIterable).toList().blockingGet();
        banks = new ArrayList<>();
        for (Bank bank : allBanks) {
            if (bank.id().equalsIgnoreCase("DKT") || bank.id().equalsIgnoreCase("CTB")) {

            } else {
                banks.add(bank);
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.image_view_background);
            textView = itemView.findViewById(R.id.text_view);
        }

        @Override
        public void onClick(View v) {
            final Bank bank = banks.get(getAdapterPosition());
            selectedBankCode = bank.code();
            selectedBankName = bank.name();
            selectedBankLogoURL = companyHelper.getLogoUri(bank, Company.LogoStyle.COLORED_24).toString();
            moveToNextScreen();
        }
    }

    class Adapter extends RecyclerView.Adapter<FragmentHelp.ViewHolder> {
        @Override
        public FragmentHelp.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FragmentHelp.ViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.d_list_item_bank, parent, false));
        }

        @Override
        public void onBindViewHolder(FragmentHelp.ViewHolder holder, int position) {
            final Bank bank = banks.get(position);
            Picasso.get()
                    .load(companyHelper.getLogoUri(bank, Company.LogoStyle.COLORED_24))
                    .into(holder.imageView);
            holder.textView.setText(bank.name());
        }

        @Override
        public int getItemCount() {
            return banks.size();
        }
    }
}
