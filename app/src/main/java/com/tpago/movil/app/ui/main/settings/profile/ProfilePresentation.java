package com.tpago.movil.app.ui.main.settings.profile;

import android.net.Uri;

import com.tpago.movil.app.ui.Presentation;

/**
 * @author hecvasro
 */
interface ProfilePresentation extends Presentation {

  void setUserPicture(Uri uri);

  void setUserFirstName(String content);

  void setUserLastName(String content);

  void setUserPhoneNumber(String content);

  void setUserEmail(String content);
}
