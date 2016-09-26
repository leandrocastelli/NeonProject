package lcs.neonproject.contacts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lcs.neonproject.model.Contact;
import lcs.neonproject.model.Model;

/**
 * Created by Leandro on 9/24/2016.
 */

public class ContactsManager {
    private static ContactsManager instance;
    private Context ctx;
    private ContactsManager(Context ctx) {
        this.ctx = ctx;
    }

    public static synchronized ContactsManager getInstance(Context ctx) {
        if (instance == null) {
            instance = new ContactsManager(ctx);
        }
        return  instance;
    }

    public static ContactsManager getInstance() {
        return instance;
    }

    public Contact searchForContact(int id) {
        String where = ContactsContract.Data.CONTACT_ID + " = " + id;
        Cursor cursor = ctx.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                new String[] {ContactsContract.Data.CONTACT_ID, ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Data.HAS_PHONE_NUMBER, ContactsContract.Data.PHOTO_ID},where, null, null);
        if (cursor != null && cursor.moveToFirst()) {


            Cursor numberCursor = ctx.getContentResolver()
                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER},
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ cursor.getString(0) }, null);
            numberCursor.moveToNext();
            String number = numberCursor.getString(0);
            numberCursor.close();
            return new Contact(cursor.getString(0),cursor.getString(1),number,
                    cursor.getInt(3));


        }
        if (cursor!= null)
            cursor.close();
        return null;
    }
    public Contact searchContactByName(String name) {
        String where = ContactsContract.Data.DISPLAY_NAME + " = " + "\""+ name + "\"";
        Cursor cursor = ctx.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                new String[] {ContactsContract.Data.CONTACT_ID, ContactsContract.Data.DISPLAY_NAME,
                        ContactsContract.Data.HAS_PHONE_NUMBER, ContactsContract.Data.PHOTO_ID},where, null, null);
        if (cursor != null && cursor.moveToFirst()) {
           return new Contact(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                   cursor.getInt(3));
        }
        return null;
    }
    public List<Model> getAllContacts() {
        List<Model> list = new ArrayList<Model>();
        String where = ContactsContract.Data.HAS_PHONE_NUMBER + " = " + "1";

        Cursor cursor = ctx.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                new String[] {ContactsContract.Data.CONTACT_ID, ContactsContract.Data.DISPLAY_NAME,
                        ContactsContract.Data.HAS_PHONE_NUMBER, ContactsContract.Data.PHOTO_ID},where, null, ContactsContract.Data.DISPLAY_NAME);
        if (cursor == null ) {
            return null;
        }

        while (cursor.moveToNext()) {
            Cursor numberCursor = ctx.getContentResolver()
                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER},
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ cursor.getString(0) }, null);
            numberCursor.moveToNext();
            list.add(new Contact(cursor.getString(0),cursor.getString(1),numberCursor.getString(0),
                    cursor.getInt(3)));
            numberCursor.close();
        }
        if (cursor!= null) {
            cursor.close();
        }
        return list;
    }

    public void insertContact(Contact contact) {

    }
}
