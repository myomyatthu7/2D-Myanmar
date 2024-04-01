package com.android.google.mmt.twodmyanmar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class YourBroadcastReceiver extends BroadcastReceiver {
    MainActivity mainActivity;
    public YourBroadcastReceiver() {
        // Required empty constructor
    }
    public YourBroadcastReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // This code will execute when the alarm goes off at 12:01 PM
        // Perform your task here
        Toast.makeText(context, "Times up!", Toast.LENGTH_SHORT).show();
    }
}
