package com.android.google.mmt.twodmyanmar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AlarmManagerCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private TextView setTextView;
    private TextView valTextView;
    private TextView numberTextView;
    private TextView statusTextView;
    private Handler handler;
    private String set="--";
    private String val="--";
    private String status="--";
    LiveData liveData;
    Runnable fetchDataRunnable;
    Thread thread;
    YourBroadcastReceiver yourBroadcastReceiver;

    boolean fetch;

    private int startTime;
    TextView tvAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTextView = findViewById(R.id.set_text_view);
        valTextView = findViewById(R.id.val_text_view);
        numberTextView = findViewById(R.id.number_text_view);
        statusTextView = findViewById(R.id.status_text_view);
        tvAnimation = findViewById(R.id.tvAnimation);

        Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade_in);
        numberTextView.setAnimation(animation);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                boolean hasExactAlarmPermission = alarmManager.canScheduleExactAlarms();
                if (!hasExactAlarmPermission) {
                    // Intent to navigate the user to the app's settings screen
                    Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(intent);
                }
            }
        }
         yourBroadcastReceiver = new YourBroadcastReceiver(MainActivity.this);

        liveData = new LiveData();
        handler = new Handler();
        fetchAndUpdateData();
        //setAlarmForTime(MainActivity.this);

    }
    public void setAlarmForTime(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 24);
        calendar.set(Calendar.SECOND, 0);

        // If the time has already passed for today, set for next day
        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent intent = new Intent(context, YourBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // For exact timing, choose the appropriate method based on OS version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    private void fetchAndUpdateData() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fetchData();
                    handler.postDelayed(this, 6000); // Fetch data every 1 second
                    Log.d("asdfg","fetchAndUpdateData"+fetch);
                }
            }, 0); // Start immediately
    }

    private void fetchData() {
            liveData.fetchData("https://www.set.or.th/th/market/product/stock/overview", new LiveData.DocumentCallback() {
                @Override
                public void onDocumentReceived(Document document) {
                    filterValues(document);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Update TextViews with extracted values
                            setTextView.setText("Set: " + set);
                            valTextView.setText("Val: " + val);
                            numberTextView.setText("Number: " + generateNumber(set, val));
                            statusTextView.setText("Status: " + status);
                        }
                    });
                }
                @Override
                public void onError(Exception e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void filterValues(Document doc) {
        Elements cols = doc.select("div.table-index-overview table").eq(1).select("tr").eq(1).select("td");
        set = cols.eq(1).text();
        val = cols.eq(7).text();
        Element statusElement = doc.select("div > small.text-end").first();
        assert statusElement != null;
        status = statusElement.text().substring(statusElement.text().indexOf(':') + 1).trim();
    }

    private String generateNumber(String set, String val) {
        String number = "";
        if (set.length() > 0 && val.length() > 1) {
            number += set.charAt(set.length() - 1);
            int index = Math.min(val.indexOf('.') + 1, val.length());
            if (index > 1) {
                number += val.substring(0, index).charAt(index - 2);
            }
        }
        return number;
    }
}

//class FetchDataTime {
//    public boolean fetchTime(int startHrMon,int stopHrMon,int startHrEve,int stopHrEve,int stopMinEve) {
//        Calendar currentTime = Calendar.getInstance();
//        int currentHr = currentTime.get(Calendar.HOUR_OF_DAY);
//        int currentMin = currentTime.get(Calendar.MINUTE);
//
//        if (currentHr >= startHrMon && currentHr <= stopHrMon) {
//            return true;
//        } else if (currentHr >= startHrEve && ( currentHr <= stopHrEve && currentMin <= stopMinEve)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//}

