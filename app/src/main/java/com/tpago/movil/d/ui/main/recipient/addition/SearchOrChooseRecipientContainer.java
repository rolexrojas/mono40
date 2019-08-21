package com.tpago.movil.d.ui.main.recipient.addition;

import androidx.annotation.NonNull;

import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.ui.Container;

import io.reactivex.Observable;

/**
 * @author hecvasro
 */
public interface SearchOrChooseRecipientContainer extends Container<AddRecipientComponent> {

  void onContactClicked(Contact contact);

  void onPartnerClicked(Partner partner);

  void onBankClicked(Bank bank);

  /**
   * Creates an {@link Observable observable} that emits all query change events.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference. Unsubscribe to free this
   * reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   * <p>
   * <em>Note:</em> By default {@link #onQueryChanged()} operates on {@link
   * SchedulerProvider#ui()}.
   *
   * @return An {@link Observable observable} that emits all query change events.
   */
  @NonNull
  Observable<String> onQueryChanged();
}
