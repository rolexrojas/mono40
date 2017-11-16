package com.tpago.movil.app.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ComponentBuilderSupplier;
import com.tpago.movil.util.ObjectHelper;

import java.util.Map;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class FragmentModule {

  public static FragmentModule create(Fragment fragment) {
    return new FragmentModule(fragment);
  }

  private final Fragment fragment;

  private FragmentModule(Fragment fragment) {
    this.fragment = ObjectHelper.checkNotNull(fragment, "fragment");
  }

  @Provides
  @FragmentScope
  @FragmentQualifier
  ComponentBuilderSupplier componentBuilderSupplier(Map<Class<?>, ComponentBuilder> map) {
    return ComponentBuilderSupplier.create(map);
  }

  @Provides
  @FragmentScope
  @FragmentQualifier
  FragmentManager fragmentManager() {
    return this.fragment.getChildFragmentManager();
  }
}
