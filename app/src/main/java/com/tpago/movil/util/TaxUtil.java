package com.tpago.movil.util;

import com.tpago.movil.R;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.d.data.Formatter;
import com.tpago.movil.d.domain.Product;

import java.math.BigDecimal;

public class TaxUtil {
    private static final float taxPercentage = 0.15f;

    public static String getConfirmPinTransactionMessage(TransactionType transactionType, double amount,
                                                         Product paymentOption, String partnerName, String currencyCode, String extraCharge,
                                                         StringMapper stringMapper, double fee, double rate, double total, double premium) {
        String confirmationText = getTransactionTypeMessage(transactionType, partnerName,
                Formatter.amount(currencyCode, BigDecimal.valueOf(amount)), extraCharge, stringMapper, fee, rate, total, premium, currencyCode);
        if (!Product.checkIfCreditCard(paymentOption)) {
            double taxAmount = amount * (taxPercentage / 100);
            String taxAmountText = Formatter.amount("RD$", new BigDecimal(taxAmount));
            String taxString = String.format(stringMapper.apply(R.string.transaction_creation_dgii_tax), taxPercentage + "%",
                    taxAmountText);
            confirmationText += "\n" + taxString;
        }
        return confirmationText;
    }

    private static String getTransactionTypeMessage(TransactionType transactionType, String partnerName,
                                                    String amount, String extraCharge, StringMapper stringMapper, double fee, double rate, double total, double premium,
                                                    String currencyCode) {
        switch (transactionType) {
            case MICRO_INSURANCE:
                return String.format(
                        stringMapper.apply(R.string.main_transaction_insurance_micro_confirm_message),
                        partnerName,
                        Money.format(currencyCode, new BigDecimal(premium)));
            case PAYPAL_RECHARGE:
                return StringHelper.builder()
                        .append(String.format(
                                stringMapper.apply(R.string.main_transaction_pay_pal_summary),
                                amount,
                                partnerName,
                                fee
                        ))
                        .append("\n")
                        .append(String.format(stringMapper.apply(R.string.main_transaction_pay_pal_rate), rate))
                        .append("\n")
                        .append(String.format(
                                stringMapper.apply(R.string.main_transaction_pay_pal_total),
                                total
                        ))
                        .toString();
            case RECHARGE:
                return String.format(stringMapper.apply(R.string.main_transaction_recharge_summary),
                        amount, partnerName);

            case PAY:
                return String.format(stringMapper.apply(R.string.transaction_creation_bill_confirmation),
                        amount, partnerName);
            case TRANSFER:
                return String.format(stringMapper.apply(R.string.format_transfer_to),
                        amount, partnerName, extraCharge);
            default:
                return "";
        }
    }
}
