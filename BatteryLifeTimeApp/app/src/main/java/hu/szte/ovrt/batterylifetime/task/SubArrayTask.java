package hu.szte.ovrt.batterylifetime.task;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.uima.internal.util.rb_trees.RedBlackTree;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hu.szte.ovrt.batterylifetime.MainActivity;

public class SubArrayTask implements Task, Serializable {

    private static final String INPUT1 = "/res/input/input1.txt";
    private static final String INPUT2 = "/res/input/input2.txt";
    private static final String INPUT3 = "/res/input/input3.txt";
    private String inputFile;

    public SubArrayTask(Integer input) {
        switch (input) {
            case 1: inputFile = INPUT1;
            break;
            case 2: inputFile = INPUT2;
            break;
            case 3: inputFile = INPUT3;
            break;
        }
    }

    @Override
    public int getId() {
        return 2;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void execute() {
        validateInput(inputFile);

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(inputFile)).getFile());
        Path path = file.toPath();

        try (Stream<String> stream = Files.lines(path)) {
            List<String> inputValues = stream.collect(Collectors.toList());
            checkInputSize(inputValues, 3);
            String ignore = inputValues.get(0);
            List<Long> nAndM =
                    Arrays.stream(inputValues.get(1).split(" "))
                            .mapToLong(Long::parseLong)
                            .boxed()
                            .collect(Collectors.toList());

            List<Long> array =
                    Arrays.stream(inputValues.get(2).split(" "))
                            .mapToLong(Long::parseLong)
                            .boxed()
                            .collect(Collectors.toList());

            long start = System.currentTimeMillis();
            maxSumMod(nAndM, array);
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
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, System.getenv("sub_array_local"), jsonObject,
                    response -> {
                        //ignore

                    }, error -> {
                // ignore
            });

            queue.add(jsonRequest);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Long maxSumMod(List<Long> nAndM, List<Long> array) {
        checkInputSize(nAndM, 2);
        int size = nAndM.get(0).intValue();
        Long m = nAndM.get(1);
        Long[] sum = new Long[size];
        sum[0] = array.get(0) % m;
        RedBlackTree<Long> RB = new RedBlackTree<>();
        RB.put(sum[0].intValue(), sum[0]);
        Long result = sum[0];
        for (int i = 1; i < size; i++) {
            boolean flag = true;
            sum[i] = sum[i - 1] + array.get(i - 1);
            sum[i] %= m;
            int k = ceilingItem(RB, sum[i].intValue());
            if (k == -1) {
                result = Math.max(sum[i], result);
                flag = false;
            }
            if (flag) {
                result = Math.max((sum[i] - k) % m, result);
            }
            if (result == (m - 1)) {
                break;
            }
            RB.put(sum[i].intValue(), sum[i]);
        }
        return result;
    }

    private int ceilingItem(RedBlackTree<Long> RB, int key) {
        return Arrays.stream(RB.keySet()).filter(value -> value >= key).sorted().findFirst().orElse(-1);
    }

    private <T> void checkInputSize(List<T> values, int size) {
        if (values.size() != size) {
            throw new IllegalArgumentException("Not supported input size");
        }
    }

    private void validateInput(String input) {
        List<String> inputs = new ArrayList<>();
        inputs.add(INPUT1);
        inputs.add(INPUT2);
        inputs.add(INPUT3);
        String nonNullInput = Optional.ofNullable(input).orElseThrow(IllegalArgumentException::new);
        if (!inputs.contains(nonNullInput)) {
            throw new IllegalArgumentException("Invalid input. Not among possible input files");
        }
    }
}
