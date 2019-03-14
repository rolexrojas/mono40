package com.tpago.movil.d.ui.main.recipient.index.category.selectbank;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.R;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.d.ui.view.RecyclerViewBaseAdapter;

import java.util.List;

import butterknife.BindView;

public class BanksAdapter extends RecyclerViewBaseAdapter<Bank, BanksAdapter.BankViewHolder> {
  private CompanyHelper companyHelper;

  public BanksAdapter(List<Bank> items, CompanyHelper companyHelper) {
    super(items);
    this.companyHelper = companyHelper;
  }

  public BanksAdapter(List<Bank> items, RecyclerAdapterCallback<Bank> listener,
                      CompanyHelper companyHelper) {
    super(items, listener);
    this.companyHelper = companyHelper;
  }

  @Override
  protected int getLayoutResource() {
    return R.layout.d_list_item_bank;
  }

  @Override
  protected BankViewHolder getViewHolder(View view) {
    return new BankViewHolder(view, item -> {
      if (listener != null) {
        listener.onItemClick(item);
      }
    }, companyHelper);
  }

  public static class BankViewHolder extends RecyclerViewBaseAdapter.BaseViewHolder<Bank> {
    @BindView(R.id.image_view_background)
    ImageView imageView;
    @BindView(R.id.text_view)
    TextView textView;
    CompanyHelper companyHelper;

    public BankViewHolder(View itemView, RecyclerAdapterCallback<Bank> listener,
                          CompanyHelper companyHelper) {
      super(itemView, listener);
      this.companyHelper = companyHelper;
    }

    @Override
    public void bind(Bank item) {
      super.bind(item);
      Picasso.get()
          .load(companyHelper.getLogoUri(item, Company.LogoStyle.COLORED_24))
          .into(imageView);
      textView.setText(item.name());
    }
  }
}
