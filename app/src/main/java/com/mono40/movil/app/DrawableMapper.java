package com.mono40.movil.app;

import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;

/**
 * Functional interface that transforms a given {@link DrawableRes drawable resource identifier}
 * into a {@link Drawable drawable}.
 */
public interface DrawableMapper {

  /**
   * Transforms a given {@link DrawableRes drawable resource identifier} into a {@link Drawable
   * drawable}.
   *
   * @param drawableId
   *   {@link DrawableRes Drawable resource identifier} that will be transformed.
   *
   * @return A {@link Drawable drawable} for the given {@link DrawableRes drawable resource
   * identifier}.
   */
  Drawable apply(@DrawableRes int drawableId);
}
