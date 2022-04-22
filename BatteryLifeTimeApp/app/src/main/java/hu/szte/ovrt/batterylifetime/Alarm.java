package hu.szte.ovrt.batterylifetime;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;

import hu.szte.ovrt.batterylifetime.task.Task;

public class Alarm extends BroadcastReceiver {
    private static final String TAG = "Alarm";

    int freq;
    Task task;

    public Alarm(Task task, int freq) {
        this.task = task;
        this.freq = freq;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag")
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        BatteryManager bm = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        processData(bm);

        wl.release();

    }

    private void processData(BatteryManager batteryManager) {
        task.execute();
        JSONObject jsonObject = new JSONObject();
        double batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                jsonObject.put("percentage", batteryLevel);
                jsonObject.put("date", LocalDateTime.now());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        RequestQueue queue = Volley.newRequestQueue(MainActivity.getContext());
        long startTime = System.currentTimeMillis();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, System.getenv("battery_save_url"), jsonObject,
                response -> {
                    //ignore

                }, error -> {
            // ignore
        });

        queue.add(jsonRequest);
    }

    public void setAlarm(Context context) {
        Intent i = new Intent(context, Alarm.class);
        boolean alarmUp = (PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_NO_CREATE) != null);
        if (!alarmUp) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), freq * 1000, pi);
        }
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}