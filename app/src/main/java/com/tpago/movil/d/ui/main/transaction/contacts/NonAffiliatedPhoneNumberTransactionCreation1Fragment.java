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
import com.tpago.movil.api.Api;
import com.tpago.movil.app.ui.AlertData;
import com.tpago.movil.app.ui.AlertManager;
import com.tpago.movil.d.domain.Banks;
import com.tpago.movil.d.domain.AccountRecipient;
import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.R;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationComponent;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationContainer;
import com.tpago.movil.d.ui.view.widget.LoadIndicator;
import com.tpago.movil.d.ui.view.widget.SwipeRefreshLayoutRefreshIndicator;
import com.tpago.movil.d.domain.LogoStyle;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.reactivex.DisposableHelper;
import com.tpago.movil.util.ObjectHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public class NonAffiliatedPhoneNumberTransactionCreation1Fragment extends
  ChildFragment<TransactionCreationContainer> {

  private AlertManager alertManager;
  private Unbinder unbinder;
  private LoadIndicator loadIndicator;

  private Adapter adapter = new Adapter();
  private List<Bank> banks = new ArrayList<>();

  private Disposable disposable = Disposables.disposed();

  @Inject Api api;
  @Inject Recipient recipient;
  @Inject StringMapper stringMapper;

  @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.alertManager = AlertManager.create(this.getActivity());

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
    loadIndicator = new SwipeRefreshLayoutRefreshIndicator(swipeRefreshLayout);
    adapter = new Adapter();
    recyclerView.setAdapter(adapter);
    final Context context = getContext();
    recyclerView.setLayoutManager(new LinearLayoutManager(
      context,
      LinearLayoutManager.VERTICAL,
      false
    ));
    final RecyclerView.ItemDecoration divider = new HorizontalDividerItemDecoration.Builder(context)
      .drawable(R.drawable.d_divider)
      .marginResId(R.dimen.space_horizontal_normal)
      .showLastDivider()
      .build();
    recyclerView.addItemDecoration(divider);
  }

  private void handleSuccess(List<Bank> banks) {
    if (ObjectHelper.isNull(this.banks)) {
      this.banks = new ArrayList<>();
    } else {
      this.banks.clear();
    }
    this.banks.addAll(banks);
    this.adapter.notifyItemRangeInserted(0, this.banks.size());
  }

  private void handleError(Throwable throwable) {
    Timber.e(throwable, "Fetching banks");
    this.alertManager.show(AlertData.createForGenericFailure(this.stringMapper));
  }

  @Override
  public void onResume() {
    super.onResume();
    this.disposable = this.api.fetchBanks()
      .flatMapObservable(Observable::fromIterable)
      .map(Bank::create)
      .toSortedList(Bank::compareTo)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe((d) -> this.loadIndicator.show())
      .doFinally(this.loadIndicator::hide)
      .subscribe(this::handleSuccess, this::handleError);
  }

  @Override
  public void onPause() {
    DisposableHelper.dispose(this.disposable);
    super.onPause();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
      getContainer().setChildFragment(new NonAffiliatedPhoneNumberTransactionCreation2Fragment());
    }
  }

  class Adapter extends RecyclerView.Adapter<ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ViewHolder(
        LayoutInflater.from(parent.getContext())
          .inflate(R.layout.d_list_item_bank, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      final Bank bank = banks.get(position);
      Picasso.with(getContext())
        .load(bank.getLogoUri(LogoStyle.PRIMARY_24))
        .into(holder.imageView);
      holder.textView.setText(Banks.getName(bank));
    }

    @Override
    public int getItemCount() {
      return banks.size();
    }
  }
}
