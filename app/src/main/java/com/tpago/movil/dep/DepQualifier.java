package com.tpago.movil.dep;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * @author hecvasro
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface DepQualifier {
}
