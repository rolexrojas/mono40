package com.tpago.movil.app.ui.main.profile;

import com.tpago.movil.app.ui.Presentation;

/**
 * @author hecvasro
 */
interface ProfilePresentation extends Presentation {

  void setProfilePictureUri(String uri);

  void setFirstNameTextInputContent(String content);

  void setLastNameTextInputContent(String content);

  void setPhoneNumberTextInputContent(String content);

  void setEmailTextInputContent(String content);
}
