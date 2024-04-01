package com.android.google.mmt.twodmyanmar;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class MainViewModel extends ViewModel {

    private MutableLiveData<String[]> valuesLiveData = new MutableLiveData<>();
    private Context context; // Store the context

    public MutableLiveData<String[]> getValuesLiveData() {
        return valuesLiveData;
    }

    // Pass the context to WebRequest
    public void initWebRequest(Context context) {
        this.context = context;
    }

    public void fetchData() {
        String url = "https://www.set.or.th/th/market/product/stock/overview";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String[] values = parseHTML(response);
                            valuesLiveData.setValue(values);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }
        });

        // Pass the stored context to WebRequest
        if (context != null) {
            WebRequest.getInstance(context).addToRequestQueue(stringRequest);
        }
    }

    private String[] parseHTML(String html) {
        // Parse HTML here and return values
        // You can use regular expressions or other parsing techniques
        return new String[]{"Set Value", "Val Value", "Number Value", html};
    }
}
