package com.tpago.movil.d.ui.main.recipient.addition;

import static com.tpago.movil.d.ui.main.recipient.index.category.Category.PAY;
import static com.tpago.movil.d.ui.main.recipient.index.category.Category.TRANSFER;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.R;
import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.main.recipient.addition.banks.BankListFragment;
import com.tpago.movil.d.ui.main.recipient.addition.contacts.ContactListFragment;
import com.tpago.movil.d.ui.main.recipient.addition.partners.PartnerListFragment;
import com.tpago.movil.d.ui.main.recipient.index.category.Category;
import com.tpago.movil.d.ui.view.widget.SearchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;

import javax.inject.Inject;

/**
 * @author hecvasro
 */
public class SearchOrChooseRecipientFragment extends ChildFragment<AddRecipientContainer> implements
  SearchOrChooseRecipientContainer {

  private Unbinder unbinder;

  @Inject
  Category category;

  @BindView(R.id.search_view)
  SearchView searchView;
  @BindView(R.id.tab_layout)
  TabLayout tabLayout;
  @BindView(R.id.view_pager)
  ViewPager viewPager;

  @NonNull
  public static SearchOrChooseRecipientFragment newInstance() {
    return new SearchOrChooseRecipientFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Injects all annotated dependencies.
    this.getComponent()
      .inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.d_fragment_search_or_choose_recipient, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated views and methods.
    this.unbinder = ButterKnife.bind(this, view);
    // Prepares the view pager.
    this.viewPager.setAdapter(new RecipientListAdapter(getChildFragmentManager()));
    // Prepares the tab layout.
    this.tabLayout.setupWithViewPager(this.viewPager);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Unbinds all the annotated views and methods.
    this.unbinder.unbind();
  }

  @Nullable
  @Override
  public AddRecipientComponent getComponent() {
    return this.getContainer()
      .getComponent();
  }

  @Override
  public void onContactClicked(Contact contact) {
    this.getContainer()
      .onContactClicked(contact);
  }

  @Override
  public void onPartnerClicked(Partner partner) {
    this.getContainer()
      .onPartnerClicked(partner);
  }

  @Override
  public void onBankClicked(Bank bank) {
    this.getContainer()
      .onBankClicked(bank);
  }

  @NonNull
  @Override
  public Observable<String> onQueryChanged() {
    return this.searchView.onQueryChanged();
  }

  private class RecipientListAdapter extends FragmentPagerAdapter {

    RecipientListAdapter(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    @Override
    public int getCount() {
      return category == TRANSFER ? 2 : 1;
    }

    @Override
    public Fragment getItem(int position) {
      if (category == TRANSFER) {
        if (position == 0) {
          return new BankListFragment();
        } else {
          return new ContactListFragment();
        }
      } else if (category == PAY) {
        return new PartnerListFragment();
      } else {
        return new ContactListFragment();
      }
    }

    @Override
    public CharSequence getPageTitle(int position) {
      if (category == TRANSFER) {
        if (position == 0) {
          return getString(R.string.banks);
        } else {
          return getString(R.string.contacts);
        }
      } else if (category == PAY) {
        return getString(R.string.partners);
      } else {
        return getString(R.string.contacts);
      }
    }
  }
}
