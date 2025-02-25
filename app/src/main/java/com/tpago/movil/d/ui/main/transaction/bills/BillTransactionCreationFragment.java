package com.tpago.movil.d.ui.main.transaction.bills;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tpago.movil.R;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationComponent;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationContainer;
import com.tpago.movil.d.ui.view.widget.PrefixableTextView;
import com.tpago.movil.dep.init.InitActivityBase;
import com.tpago.movil.dep.main.transactions.PaymentMethodChooser;
import com.tpago.movil.util.TaxUtil;
import com.tpago.movil.util.TransactionType;
import com.tpago.movil.util.UiUtil;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */

public class BillTransactionCreationFragment extends ChildFragment<TransactionCreationContainer>
        implements BillTransactionCreationPresenter.View {

    private BillTransactionCreationPresenter presenter;

    private Unbinder unbinder;

    @Inject
    StringHelper stringHelper;

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.payment_method_chooser)
    PaymentMethodChooser paymentMethodChooser;
    @BindView(R.id.prefixable_text_view_minimum)
    PrefixableTextView minimumPrefixableTextView;
    @BindView(R.id.prefixable_text_view_total)
    PrefixableTextView totalPrefixableTextView;
    @BindView(R.id.prefixable_text_view_total_owed)
    PrefixableTextView totalOwedPrefixableTextView;
    @BindView(R.id.radio_button_pay_minimum)
    RadioButton minimumRadioButton;
    @BindView(R.id.radio_button_pay_total)
    RadioButton totalRadioButton;
    @BindView(R.id.text_view_due_date)
    TextView dueDateTextView;
    @BindView(R.id.view_minimum)
    View minimumView;
    @BindView(R.id.view_total)
    View totalView;
    private String currencyCode;
    @Inject
    StringMapper stringMapper;

    public static BillTransactionCreationFragment create() {
        return new BillTransactionCreationFragment();
    }

    @OnClick(R.id.view_total)
    void onPayTotalButtonClicked() {
        presenter.onOptionSelectionChanged(BillRecipient.Option.TOTAL);
    }

    @OnClick(R.id.view_minimum)
    void onPayMinimumButtonClicked() {
        presenter.onOptionSelectionChanged(BillRecipient.Option.MINIMUM);
    }

    @OnClick(R.id.button)
    void onPayButtonClicked() {
        presenter.onPayButtonClicked();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TransactionCreationContainer container = getContainer();
        final TransactionCreationComponent component = container.getComponent();
        component.inject(this);
        presenter = new BillTransactionCreationPresenter(this, component);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.d_fragment_transaction_creation_bill, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onViewStarted();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onViewStopped();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    @Override
    public void setCurrencyValue(String value) {
        totalOwedPrefixableTextView.setPrefix(value);
        totalPrefixableTextView.setPrefix(value);
        minimumPrefixableTextView.setPrefix(value);
        currencyCode = value;
    }

    @Override
    public void setDueDateValue(String value) {
        dueDateTextView.setText(value);
    }

    @Override
    public void setTotalValue(String value) {
        totalOwedPrefixableTextView.setContent(value);
        totalPrefixableTextView.setContent(value);
    }

    @Override
    public void setTotalValueEnabled(boolean enabled) {
        this.totalRadioButton.setEnabled(enabled);
        this.totalView.setEnabled(enabled);
    }

    @Override
    public void setMinimumValue(String value) {
        minimumPrefixableTextView.setContent(value);
    }

    @Override
    public void setMinimumValueEnabled(boolean enabled) {
        this.minimumRadioButton.setEnabled(enabled);
        this.minimumView.setEnabled(enabled);
    }

    @Override
    public void setPaymentOptions(List<Product> paymentOptionList) {
        paymentMethodChooser.setPaymentMethodList(paymentOptionList);
    }

    @Override
    public void setOptionChecked(BillRecipient.Option option) {
        if (option.equals(BillRecipient.Option.TOTAL)) {
            minimumRadioButton.setChecked(false);
            totalRadioButton.setChecked(true);
            button.setText(R.string.pay_total);
        } else {
            totalRadioButton.setChecked(false);
            minimumRadioButton.setChecked(true);
            button.setText(R.string.pay_minimum);
        }
    }

    @Override
    public void setPayButtonEnabled(boolean enabled) {
        UiUtil.setEnabled(button, enabled);
    }

    @Override
    public void requestPin(String partnerName, String value, BigDecimal inputAmount) {
        final int x = Math.round((button.getRight() - button.getLeft()) / 2);
        final int y = Math.round(button.getY() + ((button.getBottom() - button.getTop()) / 2));

        PinConfirmationDialogFragment.show(
                getChildFragmentManager(),
                TaxUtil.getConfirmPinTransactionMessage(TransactionType.PAY, inputAmount.doubleValue(), paymentMethodChooser.getSelectedItem(),
                        partnerName, currencyCode, "", stringMapper, 0, 0, 0, 0),
                (PinConfirmationDialogFragment.Callback) pin -> presenter.onPinRequestFinished(paymentMethodChooser.getSelectedItem(), pin),
                x,
                y
        );
    }

    @Override
    public void setPaymentResult(boolean succeeded, String message) {
        PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), succeeded);
        if (succeeded) {
            getContainer().finish(true, message);
        }
    }

    @Override
    public void showGenericErrorDialog(String message) {
        Dialogs.builder(getContext())
                .setTitle(R.string.error_generic_title)
                .setMessage(message)
                .setPositiveButton(R.string.error_positive_button_text, (dialog, which) -> {
                    if (message.contains(getString(R.string.session_expired))) {
                        Intent intent = InitActivityBase.getLaunchIntent(getContext());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        this.startActivity(intent);
                        getActivity().finish();
                    }
                })
                .show();
    }

    @Override
    public void showGenericErrorDialog() {
        showGenericErrorDialog(getString(R.string.error_generic));
    }

    @Override
    public void showUnavailableNetworkError() {
        Toast.makeText(getContext(), R.string.error_unavailable_network, Toast.LENGTH_LONG)
                .show();
    }
}
