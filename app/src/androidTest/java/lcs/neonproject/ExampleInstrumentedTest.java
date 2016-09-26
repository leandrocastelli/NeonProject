package lcs.neonproject;

import android.Manifest;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import lcs.neonproject.contacts.ContactsManager;
import lcs.neonproject.model.Contact;
import lcs.neonproject.model.Model;
import lcs.neonproject.utils.Utils;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest{
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("lcs.neonproject", appContext.getPackageName());
    }
    @Test
    public void testGetToken() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        SharedPreferences preferences = appContext.getSharedPreferences(appContext.getPackageName()+"_preferences",Context.MODE_PRIVATE);
        preferences.edit().remove("token").commit();

        Utils.getToken("Teste","testando@teste.com",appContext);
        synchronized (this) {
            wait(4000);
        }
        String token = Utils.getToken("Teste","testando@teste.com",appContext);
        assertEquals(token, "bdefda76-2e13-42de-8d1a-b4080ef98292");
        preferences.edit().remove("token").commit();
    }
    @Test
    public void testGetContacts() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        List<Model> list = ContactsManager.getInstance(appContext).getAllContacts();
        assertTrue(list.size() > 0);
        assertTrue(list.get(0) instanceof Contact);
        assertTrue(list.get(0).getContact().getPhoneNumber() != null);
    }
}
