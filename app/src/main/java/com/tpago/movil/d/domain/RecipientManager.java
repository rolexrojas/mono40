package com.tpago.movil.d.domain;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.tpago.movil.content.SharedPreferencesCreator;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.api.ApiUtils;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * @author hecvasro
 */
@Deprecated
public final class RecipientManager {
  private static final String KEY_INDEX_SET = "indexSet";

  private final SharedPreferences sharedPreferences;
  private final Gson gson;

  private final Set<String> indexSet;

  private final DepApiBridge apiBridge;

  private List<Recipient> recipientList;

  public RecipientManager(
    SharedPreferencesCreator sharedPreferencesCreator,
    Gson gson,
    DepApiBridge apiBridge) {
    this.sharedPreferences = Preconditions
      .assertNotNull(sharedPreferencesCreator, "sharedPreferencesCreator == null")
      .create(RecipientManager.class.getCanonicalName());
    this.gson = Preconditions
      .assertNotNull(gson, "gson == null");

    this.indexSet = this.sharedPreferences.getStringSet(KEY_INDEX_SET, new HashSet<String>());

    this.apiBridge = Preconditions.assertNotNull(apiBridge, "apiBridge == null");
  }

  @Deprecated
  final void syncRecipients(final List<Recipient> remoteRecipientList) {
    if (Objects.checkIfNull(recipientList)) {
      recipientList = new ArrayList<>();
    }
    recipientList.clear();
    for (String id : indexSet) {
      recipientList.add(gson.fromJson(sharedPreferences.getString(id, null), Recipient.class));
    }

    final List<Recipient> recipientToAddList = new ArrayList<>(remoteRecipientList);
    final List<Recipient> recipientToRemoveList = new ArrayList<>();
    for (Recipient recipient : recipientList) {
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

  @Deprecated
  public final void clear() {
    recipientList.clear();
    indexSet.clear();
    sharedPreferences.edit()
      .clear()
      .apply();
  }

  @Deprecated
  public final List<Recipient> getAll(@Nullable final String query) {
    final List<Recipient> resultList = new ArrayList<>();
    for (Recipient recipient : recipientList) {
      if (recipient.matches(query)) {
        resultList.add(recipient);
      }
    }
    return resultList;
  }

  @Deprecated
  public final boolean checkIfExists(Recipient recipient) {
    return recipientList.contains(recipient);
  }

  @Deprecated
  public final Observable<Boolean> checkIfAffiliated(
    final String authToken,
    final String phoneNumber) {
    return apiBridge.checkIfAffiliated(authToken, phoneNumber)
      .compose(ApiUtils.<Boolean>handleApiResult(true));
  }

  @Deprecated
  public final void add(Recipient recipient) {
    if (!checkIfExists(recipient)) {
      recipientList.add(recipient);
      indexSet.add(recipient.getId());
      sharedPreferences.edit()
        .putStringSet(KEY_INDEX_SET, indexSet)
        .putString(recipient.getId(), gson.toJson(recipient))
        .apply();
      Collections.sort(recipientList, Recipient.comparator());
    }
  }

  @Deprecated
  public final void update(final Recipient recipient) {
    if (!checkIfExists(recipient)) {
      recipientList.add(recipient);
      indexSet.add(recipient.getId());
    }
    sharedPreferences.edit()
      .putStringSet(KEY_INDEX_SET, indexSet)
      .putString(recipient.getId(), gson.toJson(recipient))
      .apply();
    Collections.sort(recipientList, Recipient.comparator());
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
