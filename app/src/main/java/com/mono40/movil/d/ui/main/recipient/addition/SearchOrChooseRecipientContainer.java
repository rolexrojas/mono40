package com.mono40.movil.d.ui.main.recipient.addition;

import androidx.annotation.NonNull;

import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.d.data.SchedulerProvider;
import com.mono40.movil.d.ui.Container;

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
