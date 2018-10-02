package com.tpago.movil.d.ui.main.recipient.addition;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.tpago.movil.R;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.company.bank.BankStore;
import com.tpago.movil.company.bank.BanreservasType;
import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.d.domain.AccountRecipient;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.dep.App;
import com.tpago.movil.dep.net.NetworkService;
import com.tpago.movil.dep.reactivex.Disposables;
import com.tpago.movil.util.ObjectHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

/**
 * @author hecvasro
 */
public class NonAffiliatedBanreservasPhoneNumberRecipientAdditionFragment extends Fragment {


  private static final String KEY_KEYWORD = "keyword";
  private static final String KEY_DATA = "value";

  private String keyword;
  private Parcelable data;
  private RecipientBuilder builder;

  private Unbinder unbinder;

  private Disposable subscription = Disposables.disposed();

  @Inject
  DepApiBridge apiBridge;
  @Inject
  NetworkService networkService;
  @Inject
  CompanyHelper companyHelper;

  private static NonAffiliatedBanreservasPhoneNumberRecipientAdditionFragment internalCreate(String keyword, Parcelable data) {
    final Bundle bundle = new Bundle();
    bundle.putString(KEY_KEYWORD, keyword);
    bundle.putParcelable(KEY_DATA, data);
    final NonAffiliatedBanreservasPhoneNumberRecipientAdditionFragment fragment = new NonAffiliatedBanreservasPhoneNumberRecipientAdditionFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  public static NonAffiliatedBanreservasPhoneNumberRecipientAdditionFragment create(String keyword, Bank data) {
    return internalCreate(keyword, data);
  }

  public static NonAffiliatedBanreservasPhoneNumberRecipientAdditionFragment create(String keyword, Partner data) {
    return internalCreate(keyword, data);
  }

  private NonAffiliatedPhoneNumberRecipient recipient;

  private Adapter adapter = new Adapter();
  private List<Bank> banks = new ArrayList<>();
  private List<BanreservasType> types;

  private Bank bank;

  @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.recycler_view) RecyclerView recyclerView;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    final Bundle bundle = this.getArguments();
    bank = (Bank) bundle.getParcelable(KEY_DATA);
    keyword = bundle.getString(KEY_KEYWORD);
    data = bundle.getParcelable(KEY_DATA);

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
      R.layout.d_fragment_non_affiliated_banreservas_phone_number_recipient_addition,
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

    this.types = new ArrayList<>();
    BanreservasType sav = new BanreservasType().name("Banreservas Ahorros").logoTemplate(bank.logoTemplate()).savType();
    BanreservasType dda = new BanreservasType().name("Banreservas Corriente").logoTemplate(bank.logoTemplate()).ddaType();
    this.types.add(sav);
    this.types.add(dda);
    this.adapter.notifyItemRangeInserted(0, this.types.size());
  }

  @Override
  public void onPause() {
    super.onPause();
    Disposables.dispose(subscription);
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
      String  type = types.get(getAdapterPosition()).type;
      final AppCompatActivity activity = (AppCompatActivity) getActivity();
      getActivity().getSupportFragmentManager().beginTransaction()
          .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
          .replace(
              R.id.containerFrameLayout,
              RecipientBuilderFragment.create(
                  getString(R.string.account)
                      .toLowerCase(),
                  bank,
                  type
              )
          )
          .addToBackStack(null)
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
      final BanreservasType type = types.get(position);
      Picasso.with(getContext())
              .load(companyHelper.getLogoUri(bank, Company.LogoStyle.COLORED_24))
              .into(holder.imageView);
      holder.textView.setText(type.name);
    }

    @Override
    public int getItemCount() {
      return types.size();
    }
  }
}
