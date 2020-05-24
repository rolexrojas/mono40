package com.mono40.movil.app.ui.picture;

import androidx.fragment.app.FragmentManager;

import com.mono40.movil.app.ui.fragment.FragmentQualifier;
import com.mono40.movil.app.ui.fragment.FragmentScope;

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
