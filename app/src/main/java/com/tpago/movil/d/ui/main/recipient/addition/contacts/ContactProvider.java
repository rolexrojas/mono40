package com.tpago.movil.d.ui.main.recipient.addition.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.PhoneNumber;
import com.tpago.movil.d.ui.main.recipient.addition.Contact;
import com.tpago.movil.util.ComparisonChain;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;
import com.tpago.movil.util.digit.DigitUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
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
    = new String[]{COLUMN_CONTACT_NAME, COLUMN_CONTACT_PHONE_NUMBER};
  private static final String QUERY_ORDER
    = String.format("%1$s, %2$s ASC", COLUMN_CONTACT_NAME, COLUMN_CONTACT_PHONE_NUMBER);

  private final ContentResolver contentResolver;

  private List<Contact> contactList;

  ContactProvider(@NonNull ContentResolver contentResolver) {
    this.contentResolver = contentResolver;
  }

  private boolean isContained(String name, String query) {
    return Single.just(name)
      .map(String::toUpperCase)
      .zipWith(
        Single.just(query)
          .map(String::toUpperCase),
        String::contains
      )
      .blockingGet();
  }

  private boolean isNumberContained(String phoneNumber, String query) {
    if (StringHelper.isNullOrEmpty(query)) {
      return false;
    }
    return isContained(phoneNumber,query);
  }

  @NonNull
  Observable<List<Contact>> getAll(@Nullable final String query) {
    return Observable.defer(() -> {
      this.contactList = null;
      if (ObjectHelper.isNull(this.contactList)) {
        this.contactList = new ArrayList<>();
        final Cursor cursor = this.contentResolver.query(
          QUERY_URI,
          QUERY_PROJECTION,
          null,
          null,
          QUERY_ORDER
        );
        if (ObjectHelper.isNotNull(cursor)) {
          String name;
          String phoneNumber;
          Contact currentContact;
          while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME));
            phoneNumber = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_PHONE_NUMBER));
            if (PhoneNumber.isValidWithAdditionalCode(phoneNumber) &&
               (isContained(name, query) ||
               isNumberContained(DigitUtil.removeNonDigits(phoneNumber), DigitUtil.removeNonDigits(query)))) {

              currentContact = Contact.builder()
                .phoneNumber(PhoneNumber.create(phoneNumber))
                .name(name)
                .pictureUri(null)
                .build();
              if (!this.contactList.contains(currentContact)) {
                this.contactList.add(currentContact);
              }
            }
          }
          cursor.close();
        }
      }
      return Observable.fromIterable(this.contactList)
        .toSortedList((a, b) -> ComparisonChain.create()
          .compare(a.name(), b.name())
          .compare(a.phoneNumber(), b.phoneNumber())
          .result()
        )
        .toObservable();
    });
  }
}
