package com.tpago.movil.d.domain;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tpago.movil.dep.content.SharedPreferencesCreator;
import com.tpago.movil.util.ObjectHelper;

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
      this.recipientList.add(
        this.gson.fromJson(this.sharedPreferences.getString(id, null), Recipient.class)
      );
    }

    final List<Recipient> recipientToAddList = new ArrayList<>(remoteRecipientList);
    final List<Recipient> recipientToRemoveList = new ArrayList<>();
    for (Recipient recipient : this.recipientList) {
      if (recipient instanceof BillRecipient && !remoteRecipientList.contains(recipient)) {
        recipientToRemoveList.add(recipient);
      }
    }

    final SharedPreferences.Editor editor = sharedPreferences.edit();

    for (Recipient recipient : recipientToAddList) {
      recipientList.add(recipient);
      indexSet.add(recipient.getId());
      editor.putString(recipient.getId(), gson.toJson(recipient, Recipient.class));
    }
    for (Recipient recipient : recipientToRemoveList) {
      indexSet.remove(recipient.getId());
      editor.remove(recipient.getId());
    }

    editor
      .putStringSet(KEY_INDEX_SET, indexSet)
      .apply();

    Collections.sort(recipientList, Recipient.comparator());
  }

  public final void clear() {
    recipientList.clear();
    indexSet.clear();
    sharedPreferences.edit()
      .clear()
      .apply();
  }

  @Deprecated
  public final List<Recipient> getAll() {
    return this.recipientList;
  }

  @Deprecated
  public final boolean checkIfExists(Recipient recipient) {
    return recipientList.contains(recipient);
  }

  @Deprecated
  public final void add(Recipient recipient) {
    if (!checkIfExists(recipient)) {
      recipientList.add(recipient);
      indexSet.add(recipient.getId());
      sharedPreferences.edit()
        .putStringSet(KEY_INDEX_SET, indexSet)
        .putString(recipient.getId(), gson.toJson(recipient, Recipient.class))
        .apply();
      Collections.sort(recipientList, Recipient.comparator());
    }
  }

  @Deprecated
  public final void update(final Recipient recipient) {
    if (checkIfExists(recipient)) {
      this.recipientList.set(this.recipientList.indexOf(recipient), recipient);
    } else {
      this.recipientList.add(recipient);
      this.indexSet.add(recipient.getId());
    }

    this.sharedPreferences.edit()
      .putStringSet(KEY_INDEX_SET, this.indexSet)
      .putString(recipient.getId(), this.gson.toJson(recipient, Recipient.class))
      .apply();

    Collections.sort(this.recipientList, Recipient.comparator());
  }

  public final void remove(Recipient recipient) {
    final String id = recipient.getId();
    indexSet.remove(id);
    sharedPreferences.edit()
      .putStringSet(KEY_INDEX_SET, indexSet)
      .remove(id)
      .apply();
    recipientList.remove(recipient);
    Collections.sort(recipientList, Recipient.comparator());
  }
}
