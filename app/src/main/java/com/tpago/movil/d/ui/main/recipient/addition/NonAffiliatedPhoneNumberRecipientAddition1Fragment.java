package com.tpago.movil.d.ui.main.recipient.addition;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.tpago.movil.R;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.dep.App;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
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
public class NonAffiliatedPhoneNumberRecipientAddition1Fragment extends Fragment {

  private Unbinder unbinder;

  private NonAffiliatedPhoneNumberRecipient recipient;

  private Adapter adapter = new Adapter();
  private List<Bank> banks = new ArrayList<>();

  @Inject
  BankStore bankStore;
  @Inject
  CompanyHelper companyHelper;
  @Inject
  StringMapper stringMapper;

  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  private final int BANRESERVAS_CODE = 4;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    recipient = ((NonAffiliatedPhoneNumberRecipientAdditionActivityBase) getActivity()).recipient;
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
      this.banks = new ArrayList<>();
      this.banks.addAll(
          this.bankStore.getAll()
              .defaultIfEmpty(new ArrayList<>())
              .blockingGet()
      );
      this.adapter.notifyItemRangeInserted(0, this.banks.size());
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    this.unbinder.unbind();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    this.recipient = null;
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
      Bank bank = banks.get(this.getAdapterPosition());
      recipient.setBank(bank);
      if (bank.code() == BANRESERVAS_CODE) {
        moveToNextScreen(new NonAffiliatedBanreservasPhoneNumberRecipientAdditionFragment());
      } else {
        moveToNextScreen(new NonAffiliatedPhoneNumberRecipientAddition2Fragment());
      }
    }

    private void moveToNextScreen(Fragment fragment) {
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
          .replace(R.id.container, fragment)
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
      final Bank bank = banks.get(position);
      Picasso.get()
          .load(companyHelper.getLogoUri(bank, Company.LogoStyle.COLORED_24))
          .noFade()
          .into(holder.imageView);
      holder.textView.setText(bank.name());
    }

    @Override
    public int getItemCount() {
      return banks.size();
    }
  }
}
