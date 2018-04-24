package com.tpago.movil.store;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test cases of the {@link DiskStore} class
 */
public final class DiskStoreTest {

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  @Mock SharedPreferences preferences;
  @Mock SharedPreferences.Editor editor;
  @Mock Gson gson;

  private final String key = "key";
  private final String value = "value";
  private final Class<String> valueType = String.class;

  private DiskStore store;

  @Before
  public final void setUp() {
    this.store = DiskStore.builder()
      .preferences(this.preferences)
      .gson(this.gson)
      .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public final void isSet_nullAsKey_throwsIllegalArgumentException() {
    this.store.isSet(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public final void isSet_emptyStringAsKey_throwsIllegalArgumentException() {
    this.store.isSet("");
  }

  @Test
  public final void isSet_nonExistentKey_returnsFalse() {
    when(this.preferences.contains(this.key))
      .thenReturn(false);
    assertFalse(this.store.isSet(this.key));
    verify(this.preferences, only())
      .contains(this.key);
  }

  @Test
  public final void isSet_existentKey_returnsTrue() {
    when(this.preferences.contains(this.key))
      .thenReturn(true);
    assertTrue(this.store.isSet(this.key));
    verify(this.preferences, only())
      .contains(this.key);
  }

  @Test(expected = IllegalArgumentException.class)
  public final void set_nullAsKey_throwsIllegalArgumentException() {
    this.store.set(null, this.value);
  }

  @Test(expected = IllegalArgumentException.class)
  public final void set_emptyStringAsKey_throwsIllegalArgumentException() {
    this.store.set("", this.value);
  }

  @Test(expected = NullPointerException.class)
  public final void set_nullAsValue_throwsNullPointerException() {
    this.store.set(this.key, null);
  }

  @Test
  public final void set_keyAndValue_savesValue() {
    when(this.preferences.edit())
      .thenReturn(this.editor);
    when(this.editor.putString(this.key, this.value))
      .thenReturn(this.editor);
    when(this.gson.toJson(this.value))
      .thenReturn(this.value);
    this.store.set(this.key, this.value);
    verify(this.preferences)
      .edit();
    verify(this.editor)
      .putString(this.key, this.value);
    verify(this.editor)
      .apply();
    verify(this.gson)
      .toJson(this.value);
  }

  @Test(expected = IllegalArgumentException.class)
  public final void get_nullAsKey_throwsIllegalArgumentException() {
    this.store.get(null, this.valueType);
  }

  @Test(expected = IllegalArgumentException.class)
  public final void get_emptyStringAsKey_throwsIllegalArgumentException() {
    this.store.get("", this.valueType);
  }

  @Test(expected = NullPointerException.class)
  public final void get_nullAsValueType_throwsNullPointerException() {
    this.store.get(this.key, null);
  }

  @Test(expected = ClassCastException.class)
  public final void get_invalidValueType_throwsClassCastException() {
    when(this.preferences.contains(this.key))
      .thenReturn(true);
    when(this.preferences.getString(eq(this.key), any()))
      .thenReturn(this.value);
    when(this.gson.fromJson(this.value, Integer.class))
      .thenThrow(ClassCastException.class);
    this.store.get(this.key, Integer.class);
  }

  @Test
  public final void get_nonExistentKey_returnsNull() {
    when(this.preferences.contains(this.key))
      .thenReturn(false);
    assertNull(this.store.get(this.key, this.valueType));
    verify(this.preferences, only())
      .contains(this.key);
  }

  @Test
  public final void get_existentKey_returnsValue() {
    when(this.preferences.contains(this.key))
      .thenReturn(true);
    when(this.preferences.getString(eq(this.key), any()))
      .thenReturn(this.value);
    when(this.gson.fromJson(this.value, this.valueType))
      .thenReturn(this.value);
    assertEquals(this.value, this.store.get(this.key, this.valueType));
    verify(this.preferences)
      .contains(this.key);
    verify(this.preferences)
      .getString(eq(this.key), any());
    verify(this.gson)
      .fromJson(this.value, this.valueType);
  }

  @Test(expected = IllegalArgumentException.class)
  public final void remove_nullAsKey_throwsIllegalArgumentException() {
    this.store.remove(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public final void remove_emptyStringAsKey_throwsIllegalArgumentException() {
    this.store.remove("");
  }

  @Test
  public final void remove_nonExistentKey_removesNothing() {
    when(this.preferences.contains(this.key))
      .thenReturn(false);
    this.store.remove(this.key);
    verify(this.preferences, only())
      .contains(this.key);
  }

  @Test
  public final void remove_existentKey_removesValue() {
    when(this.preferences.contains(this.key))
      .thenReturn(true);
    when(this.preferences.edit())
      .thenReturn(this.editor);
    when(this.editor.remove(this.key))
      .thenReturn(this.editor);
    this.store.remove(this.key);
    verify(this.preferences)
      .contains(this.key);
    verify(this.preferences)
      .edit();
    verify(this.editor)
      .remove(this.key);
    verify(this.editor)
      .apply();
  }
}
