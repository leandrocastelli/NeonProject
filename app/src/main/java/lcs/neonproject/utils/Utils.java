package lcs.neonproject.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Locale;

import lcs.neonproject.R;
import lcs.neonproject.net.NetDownloader;

import static lcs.neonproject.utils.Constants.TOKEN;

/**
 * Created by Leandro on 9/24/2016.
 */


public class Utils {


    public static void setPreferenceString(String preference, String value, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(preference, value);
        editor.commit();

    }
    public static String getPreferenceString(String preference, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(preference,null);
    }

    public static String getToken(String userName, String email, Context context) {
        String token = getPreferenceString(TOKEN, context);
        if (token == null) {
            //Get new Token

            NetDownloader.getInstance().jsonRequest(Request.Method.GET,
                    Constants.BASE_URL + String.format(Constants.GENERATE_TOKEN,userName.replace(" ","+"),
                            email), null, context);
        }
        return token;
    }

    public static double parseValue(String value) {

        return Double.parseDouble(value.replace("R$ ","").replace(",","."));

    }

    public static Date formatDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date temp = null;
        try {
            temp = simpleDateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return temp;
    }


}
