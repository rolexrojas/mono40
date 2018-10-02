package com.tpago.movil.product;

import com.tpago.movil.app.StringMapper;
import com.tpago.movil.product.disbursable.DisbursableProductModule;
import com.tpago.movil.store.DiskStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module(includes = {
  DisbursableProductModule.class
})
public final class ProductModule {

  @Provides
  @Singleton
  ProductTypeNameMapper productTypeNameMapper(StringMapper stringMapper) {
    return ProductTypeNameMapper.create(stringMapper);
  }

  @Provides
  @Singleton
  ProductHelper productHelper(
    StringMapper stringMapper,
    ProductTypeNameMapper productTypeNameMapper
  ) {
    return ProductHelperImpl.builder()
      .stringMapper(stringMapper)
      .productTypeNameMapper(productTypeNameMapper)
      .build();
  }

  @Provides
  @Singleton
  ProductStore productStore(DiskStore diskStore) {
    return ProductStoreMemoized.create(ProductStoreDisk.create(diskStore));
  }
}
