package com.tpago.movil.app.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import dagger.MapKey;

/**
 * @author hecvasro
 */
@MapKey
@Target(ElementType.METHOD)
public @interface ContainerKey {

  Class<?> value();
}
