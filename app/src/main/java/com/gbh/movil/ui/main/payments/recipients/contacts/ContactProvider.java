package com.gbh.movil.ui.main.payments.recipients.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.rx.RxUtils;
import com.google.i18n.phonenumbers.NumberParseException;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import timber.log.Timber;

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
   * @param pageSize
   *   TODO
   * @param query
   *   TODO
   * @param contact
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<List<Contact>> getAll(final int pageSize, @Nullable final String query,
    @Nullable final Contact contact) {
    return Observable.defer(new Func0<Observable<List<Contact>>>() {
      @Override
      public Observable<List<Contact>> call() {
        if (Utils.isNull(contactList)) {
          contactList = new ArrayList<>();
          final Cursor cursor = contentResolver.query(QUERY_URI, QUERY_PROJECTION, null, null,
            QUERY_ORDER);
          if (Utils.isNotNull(cursor)) {
            String name;
            PhoneNumber phoneNumber;
            Contact currentContact;
            while (cursor.moveToNext()) {
              try {
                name = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME));
                phoneNumber = new PhoneNumber(cursor.getString(cursor.getColumnIndex(
                  COLUMN_CONTACT_PHONE_NUMBER)));
                currentContact = new Contact(phoneNumber, name, null);
                if (!contactList.contains(currentContact)) {
                  contactList.add(currentContact);
                  Timber.d(currentContact.toString());
                }
              } catch (NumberParseException exception) {
                // Since we only want valid phone numbers, we ignore these cases.
              }
            }
            cursor.close();
          }
        }
        return Observable.just(contactList);
      }
    })
      .map(new Func1<List<Contact>, List<Contact>>() {
        @Override
        public List<Contact> call(List<Contact> contactList) {
          if (Utils.isNull(contact)) {
            return contactList;
          } else {
            final int index = contactList.indexOf(contact);
            if (index == -1) {
              return contactList;
            } else if (index == (contactList.size() - 1)) {
              return new ArrayList<>();
            } else {
              return contactList.subList(index, contactList.size());
            }
          }
        }
      })
      .compose(RxUtils.<Contact>fromCollection())
      .filter(new Func1<Contact, Boolean>() {
        @Override
        public Boolean call(Contact contact) {
          return contact.matches(query);
        }
      })
      .limit(pageSize)
      .toList();
  }
}
