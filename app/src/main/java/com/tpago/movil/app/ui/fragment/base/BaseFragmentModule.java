package com.tpago.movil.app.ui.fragment.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ComponentBuilderSupplier;
import com.tpago.movil.app.ui.fragment.FragmentQualifier;
import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.util.ObjectHelper;

import java.util.Map;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class BaseFragmentModule {

  public static BaseFragmentModule create(Fragment fragment) {
    return new BaseFragmentModule(fragment);
  }

  private final Fragment fragment;

  private BaseFragmentModule(Fragment fragment) {
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
