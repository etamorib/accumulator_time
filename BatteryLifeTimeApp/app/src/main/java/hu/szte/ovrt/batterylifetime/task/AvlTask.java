package hu.szte.ovrt.batterylifetime.task;

import android.os.Build;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import hu.szte.ovrt.batterylifetime.MainActivity;

public class AvlTask implements Task, Serializable {

    private final Integer input;

    public AvlTask(Integer input) {
        this.input = input;
    }

    @Override
    public void execute() {
        long start = System.currentTimeMillis();
        numberOfAvlTrees(input);
        long stop = System.currentTimeMillis();
        JSONObject jsonObject = new JSONObject();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    jsonObject.put("date", LocalDateTime.now());
            }
            jsonObject.put("ms", stop-start);
            jsonObject.put("local", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestQueue queue = Volley.newRequestQueue(MainActivity.getContext());
        long startTime = System.currentTimeMillis();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, System.getenv("avl_local"), jsonObject,
                response -> {
                    //ignore

                }, error -> {
            // ignore
        });

        queue.add(jsonRequest);
    }

    @Override
    public int getId() {
        return 1;
    }

    private Long numberOfAvlTrees(Integer N) {
        validateInput(N);
        Long[][] array = new Long[N + 1][N + 1];
        array[0][0] = 1L;
        array[1][1] = 1L;
        for (int n = 0; n <= N; n++) {
            for (int h = 0; h <= N; h++) {
                if ((h > n) || (h == 0 && h != n) || (h == 1 && h != n)) {
                    array[n][h] = 0L;
                }
                if (n >= h && h > 1) {
                    Long sum = 0L;
                    for (int l = 1; l <= n; l++) {

                        sum +=
                                array[l - 1][h - 1] * array[n - l][h - 1]
                                        + array[l - 1][h - 1] * array[n - l][h - 2]
                                        + array[l - 1][h - 2] * array[n - l][h - 1];
                    }
                    array[n][h] = sum;
                }
            }
        }
        Long sum = 0L;

        for (int i = 0; i <= N; i++) {
            sum += array[N][i];
        }
        return sum;
    }

    private void validateInput(Integer n) {
        if (n==null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        if (n < 0) {
            throw new IllegalArgumentException("Input less than 0");
        }
    }
}