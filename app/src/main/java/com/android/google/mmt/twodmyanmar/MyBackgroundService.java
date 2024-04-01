package com.android.google.mmt.twodmyanmar;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyBackgroundService extends Service {
    private Handler handler;
    private Runnable fetchDataRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        fetchAndUpdateData();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Check if intent has a stop action, and if so, stop the service
        if (intent != null && intent.getAction() != null && intent.getAction().equals("STOP_FETCHING")) {
            stopSelf();
        }
        return START_STICKY;
    }

    private void fetchAndUpdateData() {
        fetchDataRunnable = new Runnable() {
            @Override
            public void run() {
                // Implement your data fetching logic here
                fetchData();

                // Schedule the next data fetch after 6 seconds
                handler.postDelayed(this, 6000);
            }
        };
        handler.post(fetchDataRunnable); // Start fetching immediately
    }

    private void fetchData() {
        // Implement your data fetching logic here
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

