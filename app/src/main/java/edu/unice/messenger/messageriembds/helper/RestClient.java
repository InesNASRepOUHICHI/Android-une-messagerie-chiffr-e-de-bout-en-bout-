package edu.unice.messenger.messageriembds.helper;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

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

    public JsonObjectRequest createJsonRequestWithHeaders(final int method, final String url,
                                               final JSONObject params,
                                               final Response.Listener<JSONObject> onResponse,
                                               final Response.ErrorListener onError,
                                               final Map<String, String> headers) {
        JsonObjectRequest request = new JsonObjectRequest(method, url, params, onResponse, onError) {
                       @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        }; {

        };
        request.setShouldCache(false);
        return request;
    }
}
