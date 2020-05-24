package com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase.term;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mono40.movil.R;
import com.mono40.movil.app.ui.activity.ActivityQualifier;
import com.mono40.movil.app.ui.fragment.FragmentReplacer;
import com.mono40.movil.app.ui.fragment.base.FragmentBase;
import com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchaseActivity;
import com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase.MicroInsurancePurchaseComponent;
import com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase.confirm.MicroInsurancePurchaseConfirmFragment;
import com.mono40.movil.insurance.micro.MicroInsurancePlan;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public final class MicroInsurancePurchaseTermFragment extends FragmentBase
  implements MicroInsurancePurchaseTermPresentation {

  public static MicroInsurancePurchaseTermFragment create() {
    return new MicroInsurancePurchaseTermFragment();
  }

  @Inject
  @ActivityQualifier
  FragmentReplacer fragmentReplacer;

  private MicroInsurancePurchaseTermPresenter presenter;

  @BindView(R.id.radio_group) RadioGroup radioGroup;

  @OnClick(R.id.button_submit)
  final void onSubmitButtonPressed() {
    this.presenter.onSubmitButtonPressed();
  }

  @Override
  protected int layoutResId() {
    return R.layout.main_transaction_insurance_micro_purchase_term;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Retrieves the dependency injector.
    final MicroInsurancePurchaseComponent component = MicroInsurancePurchaseActivity
      .get(this.getContext())
      .component();

    // Injects all annotated dependencies.
    component.inject(this);

    // Instantiates the presenter.
    this.presenter = MicroInsurancePurchaseTermPresenter.create(this, component);
  }

  @Override
  public void onResume() {
    super.onResume();

    // Binds the radio group to the presenter.
    this.radioGroup.setOnCheckedChangeListener((group, id) -> this.presenter.onTermSelected(id));

    // Resumes the presenter.
    this.presenter.onPresentationResumed();
  }

  @Override
  public void onPause() {
    // Pauses the presenter.
    this.presenter.onPresentationPaused();

    // Unbinds the radio group from the presenter.
    this.radioGroup.setOnCheckedChangeListener(null);

    super.onPause();
  }

  @Override
  public void setTitle(String text) {
    MicroInsurancePurchaseActivity.get(this.getContext())
      .toolbarManager()
      .setTitleText(text);
  }

  @Override
  public void setSubtitle(String text) {
    MicroInsurancePurchaseActivity.get(this.getContext())
      .toolbarManager()
      .setSubtitleText(text);
  }

  @Override
  public void setTerms(List<MicroInsurancePlan.Term> terms, int selectedTermId) {
    this.radioGroup.removeAllViews();

    final Context context = this.radioGroup.getContext();

    RadioButton radioButton;
    for (MicroInsurancePlan.Term term : terms) {
      radioButton = new RadioButton(context);
      radioButton.setId(term.id());
      radioButton.setText(this.getString(term.stringId()));
      radioButton.setChecked(term.id() == selectedTermId);

      this.radioGroup.addView(radioButton);
    }
  }

  @Override
  public void moveToNextScreen() {
    this.fragmentReplacer.begin(MicroInsurancePurchaseConfirmFragment.create())
      .transition(FragmentReplacer.Transition.SRFO)
      .addToBackStack()
      .commit();
  }
}
