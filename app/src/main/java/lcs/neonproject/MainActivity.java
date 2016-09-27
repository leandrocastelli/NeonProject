package lcs.neonproject;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import lcs.neonproject.contacts.ContactsAdapter;
import lcs.neonproject.utils.ImageHelper;
import lcs.neonproject.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory() /1024);

        int cacheSize;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            cacheSize = maxMemory / 8;
        }
        else {
            cacheSize = maxMemory / 6;
        }


        ImageHelper.mMemoryCache = new LruCache<String, Bitmap>(cacheSize);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder, new MainActivityFragment())
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        try {
            if (ContactsAdapter.progressDialog != null && ContactsAdapter.progressDialog.isShowing()) {
                ContactsAdapter.progressDialog.dismiss();
                ContactsAdapter.progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
