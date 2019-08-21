package com.tpago.movil.app.ui.main.settings.primaryPaymentMethod.list;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.main.settings.primaryPaymentMethod.PrimaryPaymentMethodFragment;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;

import butterknife.ButterKnife;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * Created by solucionesgbh on 6/4/18.
 */

public class PaymentMethodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
    private ProductManager productManager;
    private Subscription subscription = Subscriptions.unsubscribed();

    public int productPosition;
    public ImageView icon;
    public ImageView indicator;
    public TextView primaryTextView;
    public PrimaryPaymentMethodFragment fragment;



    public PaymentMethodViewHolder(View itemView, ProductManager productManager) {
        super(itemView);
        itemView.setOnClickListener(this);
        icon = ButterKnife.findById(itemView, R.id.icon);
        indicator = ButterKnife.findById(itemView, R.id.indicator);
        primaryTextView = ButterKnife.findById(itemView, R.id.primaryTextView);
        this.productManager = productManager;
    }

    @Override
    public void onClick(View view) {
        Product product = this.productManager.getPaymentOptionList().get(this.productPosition);
        this.subscription = Single
            .defer(() -> Single.just(this.productManager.setDefaultPaymentOption(product)))
            .flatMap(this::flatMap)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(this::doOnSubscribe)
            .subscribe(this::handleResult, this::handleError);
    }


    private void doOnSubscribe() {

    }

    private Single<Boolean> flatMap(boolean result) {
        if (!result) {
            return Single.just(false);
        }
        return Single.just(true);
    }

    private void handleResult(boolean result) {
        fragment.finish();
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable, "Failed selecting method");
    }
}
