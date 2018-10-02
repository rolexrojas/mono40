package com.tpago.movil.app.ui.picture;

import android.support.v4.app.FragmentManager;

import com.tpago.movil.app.ui.fragment.FragmentQualifier;
import com.tpago.movil.app.ui.fragment.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class PictureCreatorModule {

  @Provides
  @FragmentScope
  PictureCreator pictureCreator(@FragmentQualifier FragmentManager fragmentManager) {
    return PictureCreator.create(fragmentManager);
  }
}
