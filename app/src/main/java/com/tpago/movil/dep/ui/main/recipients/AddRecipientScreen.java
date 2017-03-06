package com.tpago.movil.dep.ui.main.recipients;

import com.tpago.movil.dep.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.ui.Refreshable;
import com.tpago.movil.dep.ui.Screen;

/**
 * TODO
 *
 * @author hecvasro
 */
interface AddRecipientScreen extends Screen, Refreshable {
  void startNonAffiliatedProcess(NonAffiliatedPhoneNumberRecipient recipient);
  void finish(Recipient recipient);
}
