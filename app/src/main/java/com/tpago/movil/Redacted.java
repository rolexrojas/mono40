package com.tpago.movil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hecvasro
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD })
public @interface Redacted {
}
