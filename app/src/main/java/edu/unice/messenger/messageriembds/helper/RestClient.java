package edu.unice.messenger.messageriembds.helper;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class RestClient {

    public JsonObjectRequest createJsonRequest(final int method, final String url,
                                               final JSONObject params,
                                               final Response.Listener<JSONObject> onResponse,
                                               final Response.ErrorListener onError) {
        JsonObjectRequest request = new JsonObjectRequest(method, url, params, onResponse, onError) {

        };
        request.setShouldCache(false);
        return request;
    }
}
