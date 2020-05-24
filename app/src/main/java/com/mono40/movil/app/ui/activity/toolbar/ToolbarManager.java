package com.mono40.movil.app.ui.activity.toolbar;

import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;

import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.StringHelper;
import com.mono40.movil.util.function.Action;

/**
 * @author hecvasro
 */
public final class ToolbarManager {

  static ToolbarManager create(Toolbar toolbar) {
    return new ToolbarManager(toolbar);
  }

  private final Toolbar toolbar;

  private ToolbarManager(Toolbar toolbar) {
    this.toolbar = ObjectHelper.checkNotNull(toolbar, "toolbar");
  }

  public final ToolbarManager setHomeButtonDrawable(Drawable drawable) {
    this.toolbar.setNavigationIcon(ObjectHelper.checkNotNull(drawable, "drawable"));
    return this;
  }

  public final ToolbarManager setHomeButtonDrawable(@DrawableRes int drawableId) {
    this.toolbar.setNavigationIcon(drawableId);
    return this;
  }

  public final ToolbarManager setHomeButtonAction(Action action) {
    ObjectHelper.checkNotNull(action, "action");
    this.toolbar.setNavigationOnClickListener((v) -> action.run());
    return this;
  }

  public final ToolbarManager setTitleText(String text) {
    this.toolbar.setTitle(StringHelper.checkIsNotNullNorEmpty(text, "titleText"));
    return this;
  }

  public final ToolbarManager setTitleText(@StringRes int stringId) {
    this.toolbar.setTitle(stringId);
    return this;
  }

  public final ToolbarManager setSubtitleText(String text) {
    this.toolbar.setSubtitle(StringHelper.checkIsNotNullNorEmpty(text, "subtitleText"));
    return this;
  }

  public final ToolbarManager setSubtitleText(@StringRes int stringId) {
    this.toolbar.setSubtitle(stringId);
    return this;
  }
}
