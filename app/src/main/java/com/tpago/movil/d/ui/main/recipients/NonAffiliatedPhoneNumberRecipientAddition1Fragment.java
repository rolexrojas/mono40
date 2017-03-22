package com.tpago.movil.d.ui.main.recipients;

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

import com.squareup.picasso.Picasso;
import com.tpago.movil.Bank;
import com.tpago.movil.R;
import com.tpago.movil.api.ApiImageUriBuilder;
import com.tpago.movil.app.App;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.view.widget.LoadIndicator;
import com.tpago.movil.d.ui.view.widget.SwipeRefreshLayoutRefreshIndicator;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
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

  private Subscription subscription = Subscriptions.unsubscribed();

  @Inject DepApiBridge apiBridge;
  @Inject SessionManager sessionManager;

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
    ((App) getActivity().getApplication()).getAppComponent().inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(
      R.layout.d_fragment_non_affiliated_phone_number_recipient_addition_1,
      container,
      false);
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
      false));
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
    if (bankList.isEmpty()) {
      subscription = apiBridge.banks(sessionManager.getSession().getAuthToken())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Action0() {
          @Override
          public void call() {
            loadIndicator.show();
          }
        })
        .subscribe(new Action1<ApiResult<List<Bank>>>() {
          @Override
          public void call(ApiResult<List<Bank>> result) {
            loadIndicator.hide();
            if (result.isSuccessful()) {
              adapter.notifyItemRangeRemoved(0, bankList.size());
              bankList.addAll(result.getData());
              adapter.notifyItemRangeInserted(0, bankList.size());
            } else {
              Dialogs.builder(getContext())
                .setTitle(R.string.error_title)
                .setMessage(result.getError().getDescription())
                .setPositiveButton(R.string.error_positive_button_text, null)
                .create()
                .show();
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Loading all available banks");
            loadIndicator.hide();
            Dialogs.builder(getContext())
              .setTitle(R.string.error_title)
              .setMessage(R.string.error_message)
              .setPositiveButton(R.string.error_positive_button_text, null)
              .create()
              .show();
          }
        });
    } else {
      adapter.notifyDataSetChanged();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (!subscription.isUnsubscribed()) {
      subscription.unsubscribe();
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
      activity.getSupportFragmentManager().beginTransaction()
        .setCustomAnimations(
          R.anim.fragment_transition_enter_sibling,
          R.anim.fragment_transition_exit_sibling,
          R.anim.fragment_transition_enter_sibling,
          R.anim.fragment_transition_exit_sibling)
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
        .load(ApiImageUriBuilder.build(getContext(), bank, ApiImageUriBuilder.Style.PRIMARY_24))
        .into(holder.imageView);
      holder.textView.setText(Bank.getName(bank));
    }

    @Override
    public int getItemCount() {
      return bankList.size();
    }
  }
}
