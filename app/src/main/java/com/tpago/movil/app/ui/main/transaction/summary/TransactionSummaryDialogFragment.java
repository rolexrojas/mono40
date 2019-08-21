package com.tpago.movil.app.ui.main.transaction.summary;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.d.ui.main.DepMainActivityBase;
import com.tpago.movil.transaction.TransactionSummary;
import com.tpago.movil.util.ObjectHelper;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author hecvasro
 */
public final class TransactionSummaryDialogFragment extends DialogFragment {

  private static final String KEY = TransactionSummary.class.getCanonicalName();

  public static TransactionSummaryDialogFragment create(TransactionSummary transactionSummary) {
    ObjectHelper.checkNotNull(transactionSummary, "transactionSummary");
    final Bundle arguments = new Bundle();
    arguments.putParcelable(KEY, transactionSummary);
    final TransactionSummaryDialogFragment fragment = new TransactionSummaryDialogFragment();
    fragment.setArguments(arguments);
    return fragment;
  }

  @Inject TransactionSummary transactionSummary;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Extracts the transaction summary from the arguments.
    final Bundle arguments = ObjectHelper
      .checkNotNull(this.getArguments(), "this.getArguments()");
    final TransactionSummary transactionSummary = ObjectHelper
      .checkNotNull(arguments.getParcelable(KEY), "arguments.getParcelable(KEY)");

    // Injects all annotated dependencies.
    final TransactionSummaryComponent component = DepMainActivityBase.get(this.getActivity())
      .componentBuilderSupplier()
      .get(TransactionSummaryDialogFragment.class, TransactionSummaryComponent.Builder.class)
      .transactionSummaryModule(TransactionSummaryModule.create(transactionSummary))
      .build();

    component.inject(this);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return new AlertDialog.Builder(this.getContext())
      .setCancelable(false)
      .setTitle(R.string.transactionSucceeded)
      .setView(R.layout.dialog_transaction_summary)
      .setPositiveButton(R.string.ok, null)
      .create();
  }

  @Override
  public void onStart() {
    super.onStart();

    final Dialog dialog = this.getDialog();

    //TODO: Change message for ID
    if(ObjectHelper.isNull(this.transactionSummary.message())){
      ButterKnife.<TextView>findById(dialog, R.id.transactionIdTextView)
              .setText(this.transactionSummary.id());
    }else{
      ButterKnife.<TextView>findById(dialog, R.id.transactionIdTextView)
              .setText(this.transactionSummary.message());
    }


    final TextView recipientQuestionTextView = ButterKnife
      .findById(dialog, R.id.recipientQuestionTextView);
    final TextInputLayout recipientAnswerTextInputLayout = ButterKnife
      .findById(dialog, R.id.recipientAnswerTextInputLayout);
    final EditText recipientAnswerEditText = ButterKnife
      .findById(dialog, R.id.recipientAnswerEditText);

    // TODO: Check if saving the recipient should be offered to the user.

    recipientQuestionTextView.setVisibility(View.GONE);
    recipientAnswerTextInputLayout.setVisibility(View.GONE);
    recipientAnswerEditText.setVisibility(View.GONE);
  }
}
