package hu.szte.ovrt.batterylifetime.task;

import android.os.Build;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import hu.szte.ovrt.batterylifetime.MainActivity;

public class AwsTask implements Task, Serializable {
    String runUrl;
    String saveRuntime;
    String finalUrl;
    int input;

    public AwsTask(int input, String runUrl, String saveRuntime) {
        this.input = input;
        this.runUrl = runUrl;
        finalUrl = runUrl.concat("/" + input);
        this.saveRuntime = saveRuntime;
    }

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public void execute() {
        callTask(finalUrl);
    }

    private void callTask(String url) {

        RequestQueue queue = Volley.newRequestQueue(MainActivity.getContext());
        long start = System.currentTimeMillis();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    long stop = System.currentTimeMillis();
                    try {
                        saveRuntime(stop - start);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // ignore
        });

        queue.add(stringRequest);
    }

    private void saveRuntime(long runtime) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ms", runtime);
        jsonObject.put("local", false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            jsonObject.put("date", LocalDateTime.now());
        } else {
            Date date = new Date(System.currentTimeMillis());
            jsonObject.put("date", date);
        }

        RequestQueue queue = Volley.newRequestQueue(MainActivity.getContext());
        long startTime = System.currentTimeMillis();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, saveRuntime, jsonObject,
                response -> {
                    //ignore

                }, error -> {
            // ignore
        });

        queue.add(jsonRequest);
    }
}
