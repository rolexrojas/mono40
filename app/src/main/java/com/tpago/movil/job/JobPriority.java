package com.tpago.movil.job;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author hecvasro
 */
@IntDef({
  JobPriority.LOW,
  JobPriority.NORMAL,
  JobPriority.HIGH
})
@Retention(RetentionPolicy.SOURCE)
public @interface JobPriority {

  int LOW = 0;
  int NORMAL = 50;
  int HIGH = 100;
}
