package com.mono40.movil.product.disbursable;

import com.mono40.movil.app.StringMapper;
import com.mono40.movil.product.ProductTypeNameMapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class DisbursableProductModule {

  @Provides
  @Singleton
  DisbursableProductHelper disbursableProductHelper(
    StringMapper stringMapper,
    ProductTypeNameMapper productTypeNameMapper
  ) {
    return DisbursableProductHelperImpl.builder()
      .stringMapper(stringMapper)
      .productTypeNameMapper(productTypeNameMapper)
      .build();
  }

  @Provides
  @Singleton
  DisbursableProductStore disbursableProductStore() {
    return DisbursableProductStoreMemoized.create();
  }
}
