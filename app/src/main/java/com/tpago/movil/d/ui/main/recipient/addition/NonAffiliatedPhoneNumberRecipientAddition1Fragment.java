package com.tpago.movil.d.ui.main.recipient.addition;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tpago.movil.d.domain.Banks;
import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.R;
import com.tpago.movil.dep.App;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.view.widget.LoadIndicator;
import com.tpago.movil.d.ui.view.widget.SwipeRefreshLayoutRefreshIndicator;
import com.tpago.movil.d.domain.BankProvider;
import com.tpago.movil.d.domain.FailureData;
import com.tpago.movil.d.domain.LogoStyle;
import com.tpago.movil.d.domain.ErrorCode;
import com.tpago.movil.d.domain.Result;
import com.tpago.movil.dep.reactivex.Disposables;
import com.tpago.movil.util.ObjectHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public class NonAffiliatedPhoneNumberRecipientAddition1Fragment extends Fragment {

  private Unbinder unbinder;
  private NonAffiliatedPhoneNumberRecipient recipient;

  private Adapter adapter = new Adapter();
  private List<Bank> bankList = new ArrayList<>();

  private LoadIndicator loadIndicator;

  private Disposable disposable = Disposables.disposed();

  @Inject BankProvider bankProvider;

  @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    recipient = ((NonAffiliatedPhoneNumberRecipientAdditionActivity) getActivity()).recipient;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.get(getContext())
      .component()
      .inject(this);
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

  @Override
  public void onResume() {
    super.onResume();
    disposable = bankProvider.getAll()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe(new Consumer<Disposable>() {
        @Override
        public void accept(Disposable disposable) throws Exception {
          loadIndicator.show();
        }
      })
      .subscribe(new Consumer<Result<Set<Bank>, ErrorCode>>() {
        @Override
        public void accept(Result<Set<Bank>, ErrorCode> result) throws Exception {
          if (result.isSuccessful()) {
            if (ObjectHelper.isNotNull(bankList)) {
              bankList = new ArrayList<>();
            } else {
              adapter.notifyItemRangeRemoved(0, bankList.size());
              bankList.clear();
            }
            bankList.addAll(result.getSuccessData());
            adapter.notifyItemRangeInserted(0, bankList.size());
          } else {
            final FailureData<ErrorCode> failureData = result.getFailureData();
            final Context context = getContext();
            switch (failureData.getCode()) {
              case UNAVAILABLE_NETWORK:
                Toast.makeText(context, R.string.error_unavailable_network, Toast.LENGTH_LONG)
                  .show();
                break;
              case UNEXPECTED:
                Dialogs.builder(context)
                  .setTitle(R.string.error_generic_title)
                  .setMessage(failureData.getDescription())
                  .setPositiveButton(R.string.ok, null)
                  .show();
                break;
            }
          }
        }
      }, new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
          Timber.e(throwable);
          Dialogs.builder(getContext())
            .setTitle(R.string.error_generic_title)
            .setMessage(R.string.error_generic)
            .setPositiveButton(R.string.error_positive_button_text, null)
            .show();
        }
      }, new Action() {
        @Override
        public void run() throws Exception {
          loadIndicator.hide();
        }
      });
  }

  @Override
  public void onPause() {
    super.onPause();
    if (!disposable.isDisposed()) {
      disposable.dispose();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    recipient = null;
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
      recipient.setBank(bankList.get(getAdapterPosition()));
      final AppCompatActivity activity = (AppCompatActivity) getActivity();
      activity.getSupportFragmentManager()
        .beginTransaction()
        .setCustomAnimations(
          R.anim.fragment_transition_enter_sibling,
          R.anim.fragment_transition_exit_sibling,
          R.anim.fragment_transition_enter_sibling,
          R.anim.fragment_transition_exit_sibling
        )
        .addToBackStack(null)
        .replace(R.id.container, new NonAffiliatedPhoneNumberRecipientAddition2Fragment())
        .commit();
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
      final Bank bank = bankList.get(position);
      Picasso.with(getContext())
        .load(bank.getLogoUri(LogoStyle.PRIMARY_24))
        .noFade()
        .into(holder.imageView);
      holder.textView.setText(Banks.getName(bank));
    }

    @Override
    public int getItemCount() {
      return bankList.size();
    }
  }
}
