package com.tpago.movil.d.ui.main.recipients.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.d.misc.Utils;
import com.tpago.movil.d.domain.PhoneNumber;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.ui.main.recipients.Contact;
import com.google.i18n.phonenumbers.NumberParseException;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * TODO
 *
 * @author hecvasro
 */
final class ContactProvider {
  private static final String COLUMN_CONTACT_NAME
    = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
  private static final String COLUMN_CONTACT_PHONE_NUMBER
    = ContactsContract.CommonDataKinds.Phone.NUMBER;

  private static final Uri QUERY_URI
    = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
  private static final String[] QUERY_PROJECTION
    = new String[] { COLUMN_CONTACT_NAME, COLUMN_CONTACT_PHONE_NUMBER };
  private static final String QUERY_ORDER
    = String.format("%1$s, %2$s ASC", COLUMN_CONTACT_NAME, COLUMN_CONTACT_PHONE_NUMBER);

  private final ContentResolver contentResolver;

  private List<Contact> contactList;

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
  Observable<List<Contact>> getAll(@Nullable final String query) {
    return Observable.defer(new Func0<Observable<List<Contact>>>() {
      @Override
      public Observable<List<Contact>> call() {
        if (Utils.isNull(contactList)) {
          contactList = new ArrayList<>();
          final Cursor cursor = contentResolver.query(QUERY_URI, QUERY_PROJECTION, null, null,
            QUERY_ORDER);
          if (Utils.isNotNull(cursor)) {
            String name;
            String phoneNumber;
            Contact currentContact;
            while (cursor.moveToNext()) {
              name = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME));
              phoneNumber = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_PHONE_NUMBER));
              if (PhoneNumber.isValid(phoneNumber)) {
                try {
                  currentContact = new Contact(new PhoneNumber(phoneNumber), name, null);
                  if (!contactList.contains(currentContact)) {
                    contactList.add(currentContact);
                  }
                } catch (NumberParseException exception) {
                  // Ignored, is been validated before creation.
                }
              }
            }
            cursor.close();
          }
        }
        return Observable.just(contactList);
      }
    })
      .compose(RxUtils.<Contact>fromCollection())
      .filter(new Func1<Contact, Boolean>() {
        @Override
        public Boolean call(Contact contact) {
          return contact.matches(query);
        }
      })
      .toList();
  }
}
