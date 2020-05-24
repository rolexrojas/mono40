package com.mono40.movil.d.domain;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mono40.movil.dep.content.SharedPreferencesCreator;
import com.mono40.movil.util.ObjectHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author hecvasro
 */
@Deprecated
public final class RecipientManager {

  private static final String KEY_INDEX_SET = "indexSet";

  private static boolean isProductRecipient(Recipient recipient) {
    return recipient instanceof ProductRecipient;
  }

  private final SharedPreferences sharedPreferences;
  private final Gson gson;

  private final Set<String> indexSet;

  private List<Recipient> recipientList;

  public RecipientManager(SharedPreferencesCreator sharedPreferencesCreator, Gson gson) {
    this.sharedPreferences = sharedPreferencesCreator
      .create(RecipientManager.class.getCanonicalName());
    this.gson = gson;
    this.indexSet = this.sharedPreferences.getStringSet(KEY_INDEX_SET, new HashSet<>());
  }

  @Deprecated
  final void syncRecipients(final List<Recipient> remoteRecipientList) {
    if (ObjectHelper.isNull(this.recipientList)) {
      this.recipientList = new ArrayList<>();
    } else {
      this.recipientList.clear();
    }

    for (String id : this.indexSet) {
      final Recipient recipient = this.gson
        .fromJson(this.sharedPreferences.getString(id, null), Recipient.class);
      this.recipientList.add(recipient);
    }

    final List<Recipient> recipientToAddList = new ArrayList<>(remoteRecipientList);
    final List<Recipient> recipientToRemoveList = new ArrayList<>();
    for (Recipient recipient : this.recipientList) {
      if (recipient instanceof BillRecipient && !remoteRecipientList.contains(recipient)) {
        recipientToRemoveList.add(recipient);
      } else if (isProductRecipient(recipient)) {
        recipientToRemoveList.add(recipient);
      }
    }

    final SharedPreferences.Editor editor = this.sharedPreferences.edit();

    for (Recipient recipient : recipientToAddList) {
      this.recipientList.add(recipient);
      if (!isProductRecipient(recipient)) {
        this.indexSet.add(recipient.getId());
        editor.putString(recipient.getId(), this.gson.toJson(recipient, Recipient.class));
      }
    }
    for (Recipient recipient : recipientToRemoveList) {
      this.indexSet.remove(recipient.getId());
      editor.remove(recipient.getId());
    }

    editor
      .putStringSet(KEY_INDEX_SET, this.indexSet)
      .apply();

    Collections.sort(this.recipientList, Recipient.comparator());
  }

  public final void clear() {
    this.recipientList.clear();
    this.indexSet.clear();
    this.sharedPreferences.edit()
      .clear()
      .apply();
  }

  @Deprecated
  public final List<Recipient> getAll() {
    return this.recipientList;
  }

  @Deprecated
  public final boolean checkIfExists(Recipient recipient) {
    return this.recipientList.contains(recipient);
  }

  @Deprecated
  public final void add(Recipient recipient) {
    if (!checkIfExists(recipient)) {
      this.recipientList.add(recipient);
      if (!isProductRecipient(recipient)) {
        this.indexSet.add(recipient.getId());
        this.sharedPreferences.edit()
          .putStringSet(KEY_INDEX_SET, this.indexSet)
          .putString(recipient.getId(), this.gson.toJson(recipient, Recipient.class))
          .apply();
      }
      Collections.sort(this.recipientList, Recipient.comparator());
    }
  }

  @Deprecated
  public final void update(final Recipient recipient) {
    if (checkIfExists(recipient)) {
      this.recipientList.set(this.recipientList.indexOf(recipient), recipient);
    } else {
      this.recipientList.add(recipient);
    }

    if (!isProductRecipient(recipient)) {
      this.indexSet.add(recipient.getId());
      this.sharedPreferences.edit()
        .putStringSet(KEY_INDEX_SET, this.indexSet)
        .putString(recipient.getId(), this.gson.toJson(recipient, Recipient.class))
        .apply();
    }

    Collections.sort(this.recipientList, Recipient.comparator());
  }

  public final void remove(Recipient recipient) {
    if (!isProductRecipient(recipient)) {
      final String id = recipient.getId();
      this.indexSet.remove(id);
      this.sharedPreferences.edit()
        .putStringSet(KEY_INDEX_SET, this.indexSet)
        .remove(id)
        .apply();
    }
    this.recipientList.remove(recipient);
    Collections.sort(this.recipientList, Recipient.comparator());
  }
}
