package com.tpago.movil.app.ui.main.transaction.disburse.product;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.di.ContainerKey;
import com.tpago.movil.app.ui.fragment.FragmentQualifier;
import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.app.ui.main.transaction.disburse.product.amount.FragmentComponentDisburseProductAmount;
import com.tpago.movil.app.ui.main.transaction.disburse.product.amount.FragmentDisburseProductAmount;
import com.tpago.movil.app.ui.main.transaction.disburse.product.confirm.FragmentComponentDisburseProductConfirm;
import com.tpago.movil.app.ui.main.transaction.disburse.product.confirm.FragmentDisburseProductConfirm;
import com.tpago.movil.app.ui.main.transaction.disburse.product.term.FragmentComponentDisburseProductTerm;
import com.tpago.movil.app.ui.main.transaction.disburse.product.term.FragmentDisburseProductTerm;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * @author hecvasro
 */
@Module(subcomponents = {
  FragmentComponentDisburseProductAmount.class,
  FragmentComponentDisburseProductTerm.class,
  FragmentComponentDisburseProductConfirm.class
})
public abstract class FragmentModuleComponentBuilderDisburseProduct {

  @Binds
  @IntoMap
  @ContainerKey(FragmentDisburseProductAmount.class)
  @FragmentScope
  @FragmentQualifier
  public abstract ComponentBuilder amount(FragmentComponentDisburseProductAmount.Builder builder);

  @Binds
  @IntoMap
  @ContainerKey(FragmentDisburseProductTerm.class)
  @FragmentScope
  @FragmentQualifier
  public abstract ComponentBuilder term(FragmentComponentDisburseProductTerm.Builder builder);

  @Binds
  @IntoMap
  @ContainerKey(FragmentDisburseProductConfirm.class)
  @FragmentScope
  @FragmentQualifier
  public abstract ComponentBuilder confirm(FragmentComponentDisburseProductConfirm.Builder builder);
}
