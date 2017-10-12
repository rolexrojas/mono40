package com.tpago.movil.user;

import android.net.Uri;

/**
 * @author hecvasro
 */
public interface PictureConsumer {

  void accept(Uri pictureUri);
}
