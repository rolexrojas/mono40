package com.tpago.movil.purchase;

import com.cube.sdk.altpan.CubeSdkImpl;
import com.cube.sdk.storage.operation.AddCardParams;
import com.cube.sdk.storage.operation.Card;
import com.cube.sdk.storage.operation.SelectCardParams;
import com.tpago.movil.Code;
import com.tpago.movil.function.Supplier;
import com.tpago.movil.product.Product;
import com.tpago.movil.session.User;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * {@link PosService Service} implementation that uses {@link CubeSdkImpl Cub3's} adapter to
 * communicate with POS devices
 */
final class Cub3PosService implements PosService {

  static Builder builder() {
    return new Builder();
  }

  private final Supplier<CubeSdkImpl> sdk;

  private Cub3PosService(Builder builder) {
    this.sdk = builder.sdk;
  }

  private Maybe<List<Card>> getRegisteredProductCards() {
    return Maybe.create((emitter) ->
      this.sdk.get()
        .ListCard(Cub3ListCardsCallback.create(emitter))
    );
  }

  private Maybe<String> getRegisteredProductCardAltPan(Product product) {
    final String productAlias = Cub3PosServiceUtils.productAlias(product);
    return this.getRegisteredProductCards()
      .flatMapObservable(Observable::fromIterable)
      .filter((card) -> productAlias.equals(card.getAlias()))
      .map(Card::getAltpan)
      .firstElement();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Single<Result<Placeholder>> registerProduct(User user, Product product, Code pin) {
    ObjectHelper.checkNotNull(user, "user");
    ObjectHelper.checkNotNull(product, "product");
    ObjectHelper.checkNotNull(pin, "pin");
    return this.getRegisteredProductCardAltPan(product)
      .map((altPan) -> Result.create(Placeholder.get()))
      .switchIfEmpty(Single.create((emitter) -> {
        final AddCardParams params = Cub3AddCardParamsBuilder.create()
          .user(user)
          .product(product)
          .pin(pin)
          .build();
        this.sdk.get()
          .AddCard(params, Cub3AddCardCallback.create(emitter));
      }));
  }

  @Override
  public Completable unregisterProduct(Product product) {
    ObjectHelper.checkNotNull(product, "product");
    return this.getRegisteredProductCardAltPan(product)
      .flatMapCompletable((altPan) -> Completable.create((emitter) -> {
        final SelectCardParams params = Cub3SelectCardParamsBuilder.create()
          .product(product)
          .altPan(altPan)
          .build();
        this.sdk.get()
          .DeleteCard(params, Cub3DeleteCardCallback.create(emitter));
      }));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Completable unregisterAllProducts(User user) {
    ObjectHelper.checkNotNull(user, "user");
    return this.getRegisteredProductCards()
      .flatMapCompletable((cards) -> Completable.create((emitter) -> {
        final String userIdentifier = Cub3PosServiceUtils.userIdentifier(user);
        this.sdk.get()
          .Unregister(userIdentifier, Cub3DeleteAllCardsCallback.create(emitter));
      }));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Completable startPurchase(Product product) {
    ObjectHelper.checkNotNull(product, "product");
    return this.getRegisteredProductCardAltPan(product)
      .switchIfEmpty(Single.error(new IllegalArgumentException("product not registered")))
      .flatMapCompletable((altPan) -> Completable.create((emitter) -> {
        final SelectCardParams params = Cub3SelectCardParamsBuilder.create()
          .product(product)
          .altPan(altPan)
          .build();
        this.sdk.get()
          .SelectCard(params, Cub3SelectCardCallback.create(emitter));
      }));
  }

  static final class Builder {

    private Supplier<CubeSdkImpl> sdk;

    private Builder() {
    }

    final Builder sdk(Supplier<CubeSdkImpl> sdk) {
      this.sdk = ObjectHelper.checkNotNull(sdk, "sdk");
      return this;
    }

    final Cub3PosService build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("sdk", ObjectHelper.isNull(this.sdk))
        .checkNoMissingProperties();
      return new Cub3PosService(this);
    }
  }
}
