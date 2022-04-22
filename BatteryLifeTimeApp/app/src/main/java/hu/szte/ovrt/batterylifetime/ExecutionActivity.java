package hu.szte.ovrt.batterylifetime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import hu.szte.ovrt.batterylifetime.task.AvlTask;
import hu.szte.ovrt.batterylifetime.task.AwsTask;
import hu.szte.ovrt.batterylifetime.task.SubArrayTask;
import hu.szte.ovrt.batterylifetime.task.Task;

public class ExecutionActivity extends AppCompatActivity {

    private Button stop;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execution);
        stop = findViewById(R.id.stop_button);
        textView = findViewById(R.id.information);
        int freq = getIntent().getIntExtra("freq", 3000);
        boolean local = getIntent().getBooleanExtra("local", false);
        int input = getIntent().getIntExtra("input", 1);
        int task = getIntent().getIntExtra("task", 1);

        String info = String.format("Executing {} on {} with {} input family, every {} seconds.",
                taskName(task), isLocal(local), input, freq);
        textView.setText(info);
        Task actualTask = getActualTask(task, input);
        Alarm alarm = new Alarm(actualTask, freq);

        alarm.setAlarm(this);
        stop.setOnClickListener(view -> {
            alarm.cancelAlarm(this);
            this.finish();
        });
    }

    private Task getActualTask(int i, int input) {
        switch (i) {
            case 1: return new AvlTask(input);
            case 2: return new SubArrayTask(input);
            case 3: return new AwsTask(input, System.getenv("aws_avl_url"), System.getenv("avl_save_runtime"));
            case 4: return new AwsTask(input, System.getenv("aws_sub_array_url"), System.getenv("sub_array_save_runtime"));
        }
        throw new IllegalArgumentException("Not valid task");
    }


    private String taskName(int i) {
        return i % 2 == 0 ? "SubArray" : "AVL";
    }

    private String isLocal(boolean b) {
        return b ? "locally" : "remotely";
    }
}