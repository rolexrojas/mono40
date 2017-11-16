package com.tpago.movil.app.ui.main.code;

import com.tpago.movil.app.ui.Presentation;

/**
 * @author hecvasro
 */
public interface CodeCreatorPresentation extends Presentation {

  void setTitle(String text);

  void setSubtitle(String text);

  void setValue(String text);
}
