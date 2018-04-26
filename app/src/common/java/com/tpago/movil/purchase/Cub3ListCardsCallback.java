package com.tpago.movil.purchase;

import com.cube.sdk.Interfaces.CubeSdkCallback;
import com.cube.sdk.storage.operation.Card;
import com.cube.sdk.storage.operation.CubeError;
import com.cube.sdk.storage.operation.ListCards;
import com.tpago.movil.util.ObjectHelper;

import java.util.List;

import io.reactivex.MaybeEmitter;
import timber.log.Timber;

final class Cub3ListCardsCallback implements CubeSdkCallback<ListCards, CubeError> {

  static Cub3ListCardsCallback create(MaybeEmitter<List<Card>> emitter) {
    return new Cub3ListCardsCallback(emitter);
  }

  private final MaybeEmitter<List<Card>> emitter;

  private Cub3ListCardsCallback(MaybeEmitter<List<Card>> emitter) {
    this.emitter = ObjectHelper.checkNotNull(emitter, "emitter");
  }

  @Override
  public void success(ListCards listCards) {
    Timber.i(Cub3PosServiceUtils.mapToString(listCards));

    final List<Card> cards = listCards.getCards();
    if (ObjectHelper.isNotNull(cards) && !cards.isEmpty()) {
      this.emitter.onSuccess(cards);
    }
    this.emitter.onComplete();
  }

  @Override
  public void failure(CubeError error) {
    this.emitter.onError(Cub3PosServiceException.create(error));
  }
}
