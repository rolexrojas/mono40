package com.mono40.movil.app.ui.main.transaction.insurance.micro.purchase;

import android.os.Parcelable;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.mono40.movil.insurance.micro.MicroInsurancePartner;
import com.mono40.movil.insurance.micro.MicroInsurancePlan;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@AutoValue
public abstract class MicroInsurancePurchase implements Parcelable {

  public static Builder builder() {
    return new AutoValue_MicroInsurancePurchase.Builder();
  }

  private AtomicReference<MicroInsurancePlan> plan = new AtomicReference<>();
  private AtomicReference<MicroInsurancePlan.Term> term = new AtomicReference<>();
  private AtomicReference<MicroInsurancePlan.Request> request = new AtomicReference<>();

  MicroInsurancePurchase() {
  }

  public abstract MicroInsurancePartner partner();

  public abstract List<MicroInsurancePlan> plans();

  public final void plan(@Nullable MicroInsurancePlan plan) {
    this.plan.set(plan);
  }

  @Nullable
  public final MicroInsurancePlan plan() {
    return this.plan.get();
  }

  public final void term(@Nullable MicroInsurancePlan.Term term) {
    this.term.set(term);
  }

  @Nullable
  public final MicroInsurancePlan.Term term() {
    return this.term.get();
  }

  public final void request(@Nullable MicroInsurancePlan.Request request) {
    this.request.set(request);
  }

  @Nullable
  public final MicroInsurancePlan.Request request() {
    return this.request.get();
  }

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder partner(MicroInsurancePartner partner);

    public abstract Builder plans(List<MicroInsurancePlan> plans);

    public abstract MicroInsurancePurchase build();
  }
}
