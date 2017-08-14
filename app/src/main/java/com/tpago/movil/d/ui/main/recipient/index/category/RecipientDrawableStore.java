package com.tpago.movil.d.ui.main.recipient.index.category;

import android.support.annotation.DrawableRes;
import com.tpago.movil.R;
import com.tpago.movil.d.domain.PhoneNumberRecipient;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.UserRecipient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hector Vasquez
 */
public final class RecipientDrawableStore {

  private static final List<Integer> DRAWABLE_ARRAY;

  static {
    DRAWABLE_ARRAY = new ArrayList<>();
    DRAWABLE_ARRAY.add(R.drawable.avatar_glasses_32);
    DRAWABLE_ARRAY.add(R.drawable.avatar_exes_32);
    DRAWABLE_ARRAY.add(R.drawable.avatar_wink_32);
    DRAWABLE_ARRAY.add(R.drawable.avatar_smile_32);
    DRAWABLE_ARRAY.add(R.drawable.avatar_tongue_32);
    DRAWABLE_ARRAY.add(R.drawable.avatar_monster_32);
  }

  public static RecipientDrawableStore create() {
    return new RecipientDrawableStore();
  }

  private final Map<Recipient, Integer> map = new HashMap<>();
  private final AtomicInteger index = new AtomicInteger(0);

  private RecipientDrawableStore() {
  }

  private int get(Recipient recipient) {
    final int drawableId;

    if (this.map.containsKey(recipient)) {
      drawableId = this.map.get(recipient);
    } else {
      final int i = this.index.get();

      drawableId = DRAWABLE_ARRAY.get(i);
      this.index.set((i + 1) % DRAWABLE_ARRAY.size());
    }

    return drawableId;
  }

  @DrawableRes
  public final int get(PhoneNumberRecipient recipient) {
    return this.get((Recipient) recipient);
  }

  @DrawableRes
  public final int get(UserRecipient recipient) {
    return this.get((Recipient) recipient);
  }

  private void remove(Recipient recipient) {
    if (this.map.containsKey(recipient)) {
      this.map.remove(recipient);
    }
  }

  public final void remove(PhoneNumberRecipient recipient) {
    this.remove((Recipient) recipient);
  }

  public final void remove(UserRecipient recipient) {
    this.remove((Recipient) recipient);
  }

  public final void clear() {
    this.map.clear();
    this.index.set(0);
  }
}
