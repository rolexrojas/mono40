package com.mono40.movil.d.ui.main.recipient.addition;

import com.mono40.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.mono40.movil.d.domain.Recipient;
import com.mono40.movil.d.ui.Refreshable;
import com.mono40.movil.d.ui.Screen;

/**
 * TODO
 *
 * @author hecvasro
 */
interface AddRecipientScreen extends Screen, Refreshable {

  void startNonAffiliatedProcess(NonAffiliatedPhoneNumberRecipient recipient);

  void finish(Recipient recipient);
}
