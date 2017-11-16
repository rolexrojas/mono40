package com.tpago.movil.dep.data;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tpago.movil.dep.content.SharedPreferencesCreator;
import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.d.domain.BankRepo;
import com.tpago.movil.dep.Sets;
import com.tpago.movil.util.ObjectHelper;

import java.util.Set;

/**
 * @author hecvasro
 */
@Deprecated
final class SharedPreferencesBankRepo implements BankRepo {

  private static final String KEY_ID_SET = "idSet";

  private final Gson gson;
  private final SharedPreferences sharedPreferences;

  private final Set<String> idSet;

  SharedPreferencesBankRepo(Gson gson, SharedPreferencesCreator sharedPreferencesCreator) {
    this.gson = ObjectHelper.checkNotNull(gson, "gson");
    this.sharedPreferences = ObjectHelper.checkNotNull(
      sharedPreferencesCreator,
      "sharedPreferencesCreator"
    )
      .create(SharedPreferencesBankRepo.class.getCanonicalName());
    this.idSet = this.sharedPreferences.getStringSet(KEY_ID_SET, Sets.createSet());
  }

  @Override
  public void reset() {
    idSet.clear();
    sharedPreferences.edit()
      .clear()
      .apply();
  }

  @Override
  public Set<Bank> getAll() {
    final Set<Bank> bankSet = Sets.createSortedSet();
    for (String id : idSet) {
      bankSet.add(gson.fromJson(sharedPreferences.getString(id, null), Bank.class));
    }
    return bankSet;
  }

  @Override
  public void saveAll(Set<Bank> bankSet) {
    ObjectHelper.checkNotNull(bankSet, "bankSet");
    reset();
    final SharedPreferences.Editor editor = sharedPreferences.edit();
    for (Bank bank : bankSet) {
      final String id = bank.getId();
      idSet.add(id);
      editor.putString(id, gson.toJson(bank, Bank.class));
    }
    editor.putStringSet(KEY_ID_SET, idSet)
      .apply();
  }
}
