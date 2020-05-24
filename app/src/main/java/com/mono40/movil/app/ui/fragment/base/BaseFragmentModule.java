package com.mono40.movil.app.ui.fragment.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.di.ComponentBuilderSupplier;
import com.mono40.movil.app.ui.fragment.FragmentQualifier;
import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.util.ObjectHelper;

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
