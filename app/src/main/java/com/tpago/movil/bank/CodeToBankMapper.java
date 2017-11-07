package com.tpago.movil.bank;

import com.tpago.movil.function.Function;
import com.tpago.movil.util.ObjectHelper;

import java.util.List;

import io.reactivex.Maybe;

/**
 * @author hecvasro
 */
public final class CodeToBankMapper implements Function<Integer, Maybe<Bank>> {

  static CodeToBankMapper create(BankListSupplier supplier) {
    return new CodeToBankMapper(supplier);
  }

  private final BankListSupplier supplier;

  private CodeToBankMapper(BankListSupplier supplier) {
    this.supplier = ObjectHelper.checkNotNull(supplier, "supplier");
  }

  private Maybe<Bank> flapMapMaybe(int code, List<Bank> banks) {
    for (Bank bank : banks) {
      if (bank.code() == code) {
        return Maybe.just(bank);
      }
    }
    return Maybe.empty();
  }

  @Override
  public Maybe<Bank> apply(Integer code) {
    ObjectHelper.checkNotNull(code, "code");
    return this.supplier.get()
      .flatMapMaybe((banks) -> this.flapMapMaybe(code, banks));
  }
}
