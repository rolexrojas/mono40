package com.tpago.movil.d.ui.main.recipient.addition;

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

import com.tpago.movil.Partner;
import com.tpago.movil.R;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.main.recipient.addition.contacts.ContactListFragment;
import com.tpago.movil.d.ui.main.recipient.addition.partners.PartnerListFragment;
import com.tpago.movil.d.ui.view.widget.SearchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

/**
 * @author hecvasro
 */
public class SearchOrChooseRecipientFragment extends ChildFragment<AddRecipientContainer> implements SearchOrChooseRecipientContainer {
  private Unbinder unbinder;

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

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.d_fragment_search_or_choose_recipient, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Prepares the view pager.
    viewPager.setAdapter(new RecipientListAdapter(getChildFragmentManager()));
    // Prepares the tab layout.
    tabLayout.setupWithViewPager(viewPager);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @Nullable
  @Override
  public AddRecipientComponent getComponent() {
    return getContainer().getComponent();
  }

  @Override
  public void onContactClicked(Contact contact) {
    getContainer().onContactClicked(contact);
  }

  @Override
  public void onPartnerClicked(Partner partner) {
    getContainer().onPartnerClicked(partner);
  }

  @NonNull
  @Override
  public Observable<String> onQueryChanged() {
    return searchView.onQueryChanged();
  }

  private class RecipientListAdapter extends FragmentPagerAdapter {
    RecipientListAdapter(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
      if (position == 0) {
        return new PartnerListFragment();
      } else {
        return new ContactListFragment();
      }
    }

    @Override
    public int getCount() {
      return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      if (position == 0) {
        return getString(R.string.partners);
      } else {
        return getString(R.string.contacts);
      }
    }
  }
}
