package jensfranz.de.fritzboxgooglemusicindexer.business;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.text.MessageFormat;

import jensfranz.de.fritzboxgooglemusicindexer.dao.FritzBoxDao;

public class FritzBoxBusiness {

    private static Response.ErrorListener errorCallback;

    public static void login(final Activity activity, final String host, final LoginCallback callback) {
        errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.failed(error.getMessage());
            }
        };
        FritzBoxDao.login(activity, host, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(FritzBoxBusiness.class.getName(), "Received challenge response: " + response);
                String challenge = XmlParserHelper.parseChallenge(response);
                Log.d(FritzBoxBusiness.class.getName(), "Got challenge: " + challenge);
                final String challengeResponse = createChallengeResponse("pfeffer", challenge);
                Log.d(FritzBoxBusiness.class.getName(), "Got challengeResponse: " + challengeResponse);

                FritzBoxDao.login(activity, host, challengeResponse, new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.d(FritzBoxBusiness.class.getName(), "Got SID response: " + response);
                        final String sid = XmlParserHelper.parseSid(response);
                        Log.d(FritzBoxBusiness.class.getName(), "Got SID: " + sid);
                        FritzBoxDao.refreshGmusic(activity, host, sid, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(FritzBoxBusiness.class.getName(), "Got gmusic response: " + response);
                                callback.loggedIn(sid);
                            }
                        }, errorCallback);
                    }
                }, errorCallback);

            }
        }, errorCallback);
    }


    private static String createChallengeResponse(final String password, final String challenge) {
        final String concatedSecret = MessageFormat.format("{0}-{1}", challenge, password);
        final byte[] utf16 = BusinessHelper.encodeInUtf16(concatedSecret);
        final String md5 = BusinessHelper.md5(utf16);
        return MessageFormat.format("{0}-{1}", challenge, md5);
    }

    public static interface LoginCallback {
        public void loggedIn(final String sid);

        public void failed(final String reason);
    }
}
