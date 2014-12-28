package jensfranz.de.fritzboxgooglemusicindexer.dao;

import android.app.Activity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class FritzBoxDao {

    private static Map<Activity, RequestQueue> requestQueueMap;

    static {
        requestQueueMap = new HashMap<>();
    }

    /**
     * First request for obtaining the challenge.
     *
     * @param activity activity
     * @param host e.g. fritz.box
     * @param callback for success
     * @param errorCallback for error
     */
    public static void login(final Activity activity, final String host, final Response.Listener<String> callback, final Response.ErrorListener errorCallback) {
        final String loginUrl = FritzBoxUrl.getLoginUrl(host);
        get(activity, callback, errorCallback, loginUrl);
    }

    /**
     * Following request for obtaining the SID.
     *
     * @param activity activity
     * @param host e.g. fritz.box
     * @param callback for success
     * @param errorCallback for error
     */
    public static void login(final Activity activity, final String host, final String challengeResponse, final Response.Listener<String> callback, final Response.ErrorListener errorCallback) {
        final String loginUrl = FritzBoxUrl.getLoginUrl(host, challengeResponse);
        get(activity, callback, errorCallback, loginUrl);
    }

    public static void refreshGmusic(final Activity activity, final String host, final String sid, final Response.Listener<String> callback, final Response.ErrorListener errorCallback) {
        Log.i(FritzBoxDao.class.getName(), "Rereshing GMusic with SID " + sid);
        final String url = FritzBoxUrl.getMediaSettingsUrl(host, sid);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, callback, errorCallback) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("google_refresh", "");
                params.put("gpath", "SMI-USBDISK-01");
                return params;
            }
        };
        startRequest(activity, stringRequest);
    }

    private static void get(Activity activity, Response.Listener<String> callback, Response.ErrorListener errorCallback, String loginUrl) {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, loginUrl, callback, errorCallback);
        startRequest(activity, stringRequest);
    }

    public static void startRequest(final Activity activity, final Request<String> request) {
        final RequestQueue requestQueue = getOrCreateRequestQueue(activity);

        Log.d(FritzBoxDao.class.getName(), MessageFormat.format("{0} {1}", request.getMethod(), request.getUrl()));

        requestQueue.add(request);
    }

    private static RequestQueue getOrCreateRequestQueue(final Activity activity) {
        final RequestQueue requestQueue = requestQueueMap.get(activity);
        if(requestQueue == null) {
            requestQueueMap.put(activity, Volley.newRequestQueue(activity));
            return requestQueueMap.get(activity);
        } else {
            return requestQueue;
        }
    }
}
