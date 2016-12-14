package com.mongodb.baas.sdk;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class Volley {
    public static class JsonStringRequest extends JsonRequest<String> {

        /**
         * Creates a new request.
         * @param method the HTTP method to use
         * @param url URL to fetch the JSON from
         * @param jsonRequest What to post with the request
         * @param listener Listener to receive the JSON response
         * @param errorListener Error listener, or null to ignore errors.
         */
        public JsonStringRequest(
                final int method,
                final String url,
                final String jsonRequest,
                final Response.Listener<String> listener,
                final Response.ErrorListener errorListener
        ) {
            super(method, url, jsonRequest, listener, errorListener);
        }

        @Override
        protected Response<String> parseNetworkResponse(final NetworkResponse response) {
            try {
                return Response.success(
                        new String(
                                response.data,
                                HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            }
        }
    }

    public static class AuthedJsonObjectRequest extends JsonRequest<JSONObject> {

        private final Map<String, String> _headers;

        /**
         * Creates a new request.
         * @param method the HTTP method to use
         * @param url URL to fetch the JSON from
         * @param jsonRequest What to post with the request
         * @param headers Headers to set on the request
         * @param listener Listener to receive the JSON string response
         * @param errorListener Error listener, or null to ignore errors.
         */
        public AuthedJsonObjectRequest(
                final int method,
                final String url,
                final String jsonRequest,
                final Map<String, String> headers,
                final Response.Listener<JSONObject> listener,
                final Response.ErrorListener errorListener
        ) {
            super(method, url, jsonRequest, listener, errorListener);
            _headers = headers;
        }

        @Override
        public Map<String, String> getHeaders() {
            return _headers;
        }

        @Override
        protected Response<JSONObject> parseNetworkResponse(final NetworkResponse response) {
            try {
                final String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                return Response.success(new JSONObject(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }

    public static class AuthedJsonStringRequest extends JsonRequest<String> {

        private final Map<String, String> _headers;

        /**
         * Creates a new request.
         * @param method the HTTP method to use
         * @param url URL to fetch the JSON from
         * @param jsonRequest What to post with the request
         * @param headers Headers to set on the request
         * @param listener Listener to receive the JSON string response
         * @param errorListener Error listener, or null to ignore errors.
         */
        public AuthedJsonStringRequest(
                final int method,
                final String url,
                final String jsonRequest,
                final Map<String, String> headers,
                final Response.Listener<String> listener,
                final Response.ErrorListener errorListener
        ) {
            super(method, url, jsonRequest, listener, errorListener);
            _headers = headers;
        }

        @Override
        public Map<String, String> getHeaders() {
            return _headers;
        }

        @Override
        protected Response<String> parseNetworkResponse(final NetworkResponse response) {
            try {
                return Response.success(
                        new String(
                                response.data,
                                HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            }
        }
    }
}
