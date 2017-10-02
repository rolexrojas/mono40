package com.tpago.movil.app.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.tpago.movil.R;
import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ComponentBuilderSupplier;

import java.util.Map;

import dagger.Module;
import dagger.Provides;

/**
 * {@link Module} that contains providers for objects that related to {@link Fragment fragments} and
 * belong to the {@link Context context} of an {@link Activity}.
 *
 * @author hecvasro
 */
@Module
public final class FragmentActivityModule {

  @Provides
  @ActivityScope
  @ActivityQualifier
  ComponentBuilderSupplier componentBuilderSupplier(Map<Class<?>, ComponentBuilder> map) {
    return ComponentBuilderSupplier.create(map);
  }

  @Provides
  @ActivityScope
  @ActivityQualifier
  FragmentReplacer fragmentReplacer(FragmentManager fragmentManager) {
    return FragmentReplacer.create(fragmentManager, R.id.containerFrameLayout);
  }
}
