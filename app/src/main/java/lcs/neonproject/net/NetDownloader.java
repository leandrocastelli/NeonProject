package lcs.neonproject.net;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lcs.neonproject.MainActivity;
import lcs.neonproject.R;
import lcs.neonproject.contacts.ContactsAdapter;
import lcs.neonproject.model.Model;
import lcs.neonproject.model.Transaction;
import lcs.neonproject.utils.Constants;
import lcs.neonproject.utils.Utils;

/**
 * Created by Leandro on 9/24/2016.
 */

public class NetDownloader {

    private static NetDownloader ourInstance = new NetDownloader();
    private RequestQueue requestQueue;
    private static final String TAG = NetDownloader.class.toString();
    private NetDownloader() {

    }
    public static NetDownloader getInstance() {
        return ourInstance;
    }

    public void jsonRequest(final int method, String url, final JSONObject parameters, final Context context) {

        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(context);

        final Request request;

        if (url.contains("?token")) {
            request = new JsonArrayRequest(method, url, parameters, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG,"Json response");

                    Message msg = new Message();
                    msg.what = Constants.MSG_DATE_UPDATE;
                    Bundle bundle = new Bundle();
                    bundle.putString("History",response.toString());
                    msg.setData(bundle);
                    ContactsAdapter.myHandler.sendMessage(msg);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ContactsAdapter.myHandler.sendEmptyMessage(0);
                    View rootView =  ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
                    Snackbar.make(rootView, "Falha ao receber o histórico",Snackbar.LENGTH_SHORT).show();

                }
            });
        } else {
            request = new StringRequest(method, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d(TAG, response);
                    if (response != null) {
                        if (method == Request.Method.GET) {
                            Utils.setPreferenceString(Constants.TOKEN, response.replace("\"", ""), context);
                        } else {
                            View rootView =  ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
                            if (Boolean.parseBoolean(response)) {
                                Log.d(TAG, "Money Sent");

                                Snackbar.make(rootView, R.string.sent_sucess,Snackbar.LENGTH_LONG).show();
                            }
                            else {
                                Log.d(TAG, "Money Sent fail");
                                Snackbar.make(rootView, R.string.sent_fail,Snackbar.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }

            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    if (method == Method.POST) {
                        try {
                            params.put("ClienteId",parameters.getString("ClienteId"));
                            params.put("token",parameters.getString("token"));
                            params.put("valor",parameters.getString("valor"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    return  params;
                }
            };
        }
        requestQueue.add(request);
        requestQueue.start();
    }

    public void sendMoney (String value, String clientId, Context ctx) {
        Double parsedValue = Utils.parseValue(value);
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("ClienteId", clientId);
            parameters.put("token",Utils.getPreferenceString(Constants.TOKEN, ctx));
            parameters.put("valor",parsedValue.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonRequest(Request.Method.POST, Constants.BASE_URL + Constants.SEND_MONEY, parameters, ctx);
    }

    public void getHistory(Context ctx) {
        jsonRequest(Request.Method.GET, Constants.BASE_URL + Constants.GET_TRANSFERS +Utils.getPreferenceString(Constants.TOKEN, ctx),
                null, ctx);
    }
}
