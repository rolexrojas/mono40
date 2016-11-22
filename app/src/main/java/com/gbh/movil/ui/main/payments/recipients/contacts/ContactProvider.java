package com.gbh.movil.ui.main.payments.recipients.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.PhoneNumber;
import com.google.i18n.phonenumbers.NumberParseException;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;

/**
 * TODO
 *
 * @author hecvasro
 */
final class ContactProvider {
  private static final String COLUMN_ID = ContactsContract.Contacts._ID;
  private static final String COLUMN_DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
  private static final String COLUMN_HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

  private static final Uri QUERY_CONTACT_URI = ContactsContract.Contacts.CONTENT_URI;
  private static final String[] QUERY_CONTACT_PROJECTION = new String[] {
    COLUMN_ID,
    COLUMN_DISPLAY_NAME,
    COLUMN_HAS_PHONE_NUMBER
  };
  private static final String QUERY_CONTACT_SELECTION = COLUMN_HAS_PHONE_NUMBER + " = ?";
  private static final String[] QUERY_CONTACT_SELECTION_ARGUMENTS = new String[] { "1" };
  private static final String QUERY_CONTACT_ORDER = COLUMN_DISPLAY_NAME + " ASC";

  private static final String COLUMN_PHONE_NUMBER_ID = ContactsContract.CommonDataKinds.Phone._ID;
  private static final String COLUMN_PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

  private static final Uri QUERY_PHONE_NUMBER_URI
    = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
  private static final String[] QUERY_PHONE_NUMBER_PROJECTION = new String[] {
    COLUMN_PHONE_NUMBER_ID,
    COLUMN_PHONE_NUMBER
  };
  private static final String QUERY_PHONE_NUMBER_SELECTION = COLUMN_PHONE_NUMBER_ID + " = ?";
  private static final String QUERY_PHONE_NUMBER_ORDER = COLUMN_PHONE_NUMBER + " ASC";

  private final ContentResolver contentResolver;

  /**
   * TODO
   *
   * @param contentResolver
   *   TODO
   */
  ContactProvider(@NonNull ContentResolver contentResolver) {
    this.contentResolver = contentResolver;
  }

  /**
   * TODO
   *
   * @param query
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  private String getQuerySelection(@Nullable String query) {
    String selection = COLUMN_HAS_PHONE_NUMBER + "='1'";
    if (Utils.isNotNull(query)) {
      selection += " AND " + COLUMN_DISPLAY_NAME + " LIKE '" + query + "'";
    }
    return selection;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<List<Contact>> getAll() {
    return Observable.defer(new Func0<Observable<List<Contact>>>() {
      @Override
      public Observable<List<Contact>> call() {
        final List<Contact> contacts = new ArrayList<>();
        final Cursor contactCursor = contentResolver.query(QUERY_CONTACT_URI,
          QUERY_CONTACT_PROJECTION, QUERY_CONTACT_SELECTION, QUERY_CONTACT_SELECTION_ARGUMENTS,
          QUERY_CONTACT_ORDER);
        if (Utils.isNotNull(contactCursor)) {
          String id;
          String name;
          int hasPhoneNumber;
          String phoneNumber;
          while (contactCursor.moveToNext()) {
            id = contactCursor.getString(contactCursor.getColumnIndex(COLUMN_ID));
            name = contactCursor.getString(contactCursor.getColumnIndex(COLUMN_DISPLAY_NAME));
            hasPhoneNumber = contactCursor.getInt(contactCursor
              .getColumnIndex(COLUMN_HAS_PHONE_NUMBER));
            if (hasPhoneNumber > 0) {
              final Cursor phoneNumberCursor = contentResolver.query(QUERY_PHONE_NUMBER_URI,
                QUERY_PHONE_NUMBER_PROJECTION, QUERY_PHONE_NUMBER_SELECTION, new String[] { id },
                QUERY_PHONE_NUMBER_ORDER);
              if (Utils.isNotNull(phoneNumberCursor)) {
                while (phoneNumberCursor.moveToNext()) {
                  phoneNumber = phoneNumberCursor.getString(phoneNumberCursor
                    .getColumnIndex(COLUMN_PHONE_NUMBER));
                  if (PhoneNumber.isValid(phoneNumber)) {
                    try {
                      contacts.add(new Contact(new PhoneNumber(phoneNumber), name, null));
                    } catch (NumberParseException exception) {
                      // No supposed to happen because we are validating the phone number first, so
                      // we ignore it.
                    }
                  }
                }
                phoneNumberCursor.close();
              }
            }
          }
          contactCursor.close();
        }
        return Observable.just(contacts);
      }
    });
  }
}
