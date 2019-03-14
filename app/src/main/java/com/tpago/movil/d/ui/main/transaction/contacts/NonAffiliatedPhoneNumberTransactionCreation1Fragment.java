package com.tpago.movil.d.ui.main.transaction.contacts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.company.bank.BankStore;
import com.tpago.movil.d.domain.AccountRecipient;
import com.tpago.movil.R;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationComponent;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationContainer;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.util.ObjectHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public class NonAffiliatedPhoneNumberTransactionCreation1Fragment extends
  ChildFragment<TransactionCreationContainer> {

  private Unbinder unbinder;
  private Adapter adapter = new Adapter();
  private static List<Bank> banks;

  @Inject Recipient recipient;
  @Inject BankStore bankStore;
  @Inject StringMapper stringMapper;
  @Inject
  CompanyHelper companyHelper;

  @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  private final int BANRESERVAS_CODE = 4;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final TransactionCreationComponent c = getContainer().getComponent();
    if (ObjectHelper.isNotNull(c)) {
      c.inject(this);
    }
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(
      R.layout.d_fragment_non_affiliated_phone_number_recipient_addition_1,
      container,
      false
    );
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
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
  }

  @Override
  public void onResume() {
    super.onResume();
    if (ObjectHelper.isNull(this.banks)) {
      this.banks = this.bankStore.getAll()
        .defaultIfEmpty(new ArrayList<>())
        .blockingGet();
      this.adapter.notifyItemRangeInserted(0, this.banks.size());
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    this.unbinder.unbind();
  }

  final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView imageView;
    private TextView textView;

    ViewHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
      imageView = ButterKnife.findById(itemView, R.id.image_view_background);
      textView = ButterKnife.findById(itemView, R.id.text_view);
    }

    @Override
    public void onClick(View v) {
      final Bank bank = banks.get(getAdapterPosition());
      if (recipient instanceof AccountRecipient) {
        ((AccountRecipient) recipient).bank(bank);
      } else {
        ((NonAffiliatedPhoneNumberRecipient) recipient).setBank(bank);
      }
      if (BANRESERVAS_CODE == bank.code()) {
        getContainer().setChildFragment(new NonAffiliatedPhoneNumberBanReservasTransactionCreationFragment());
      } else {
        getContainer().setChildFragment(new NonAffiliatedPhoneNumberTransactionCreation2Fragment());
      }
    }
  }

  public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ViewHolder(
        LayoutInflater.from(parent.getContext())
          .inflate(R.layout.d_list_item_bank, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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
