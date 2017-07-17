package com.tpago.movil.d.ui.main.recipients;

import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.ui.Refreshable;
import com.tpago.movil.d.ui.Screen;

/**
 * TODO
 *
 * @author hecvasro
 */
interface AddRecipientScreen extends Screen, Refreshable {
  void startNonAffiliatedProcess(NonAffiliatedPhoneNumberRecipient recipient);
  void finish(Recipient recipient);
}
