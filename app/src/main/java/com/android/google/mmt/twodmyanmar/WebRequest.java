package com.android.google.mmt.twodmyanmar;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class WebRequest {

    private static WebRequest instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private WebRequest(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized WebRequest getInstance(Context context) {
        if (instance == null) {
            instance = new WebRequest(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
