package lcs.neonproject.contacts;

import android.os.Bundle;
import android.os.Message;

import java.io.Serializable;
import java.util.List;

import lcs.neonproject.model.Model;
import lcs.neonproject.utils.Constants;

/**
 * Created by Leandro on 9/25/2016.
 */

public class ContactsLoaderThread implements Runnable {
    @Override
    public void run() {
        List<Model> list = ContactsManager.getInstance().getAllContacts();

        Message msg = new Message();
        msg.what = Constants.MSG_CONTACTS_UPDATE;
        Bundle bundle = new Bundle();
        bundle.putSerializable("Contacts",(Serializable)list);
        msg.setData(bundle);
        ContactsAdapter.myHandler.sendMessage(msg);
    }
}
