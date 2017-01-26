package com.tpago.movil.ui.main.recipients;

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

import com.tpago.movil.R;
import com.tpago.movil.ui.ChildFragment;
import com.tpago.movil.ui.main.recipients.contacts.ContactListFragment;
import com.tpago.movil.ui.view.widget.SearchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public class SearchOrChooseRecipientFragment extends ChildFragment<AddRecipientContainer>
  implements SearchOrChooseRecipientContainer {
  private Unbinder unbinder;

  @BindView(R.id.search_view)
  SearchView searchView;
  @BindView(R.id.tab_layout)
  TabLayout tabLayout;
  @BindView(R.id.view_pager)
  ViewPager viewPager;

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static SearchOrChooseRecipientFragment newInstance() {
    return new SearchOrChooseRecipientFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_search_or_choose_recipient, container, false);
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
  public void onContactClicked(@NonNull Contact contact) {
    getContainer().onContactClicked(contact);
  }

  @NonNull
  @Override
  public Observable<String> onQueryChanged() {
    return searchView.onQueryChanged();
  }

  /**
   * TODO
   */
  private class RecipientListAdapter extends FragmentPagerAdapter {
    RecipientListAdapter(FragmentManager fragmentManager) {
      super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
      return new ContactListFragment();
    }

    @Override
    public int getCount() {
      return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return getString(R.string.contacts);
    }
  }
}
