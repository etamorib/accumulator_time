package hu.szte.ovrt.batterylifetime;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;

import java.io.Serializable;

import hu.szte.ovrt.batterylifetime.task.AvlTask;
import hu.szte.ovrt.batterylifetime.task.AwsTask;
import hu.szte.ovrt.batterylifetime.task.SubArrayTask;
import hu.szte.ovrt.batterylifetime.task.Task;

public class MainActivity extends AppCompatActivity {
    private static Application instance;
    private Switch local;
    private Button submit;
    private RadioGroup radioGroup;
    private RadioButton avlButton;
    private RadioButton subArrayButton;
    private EditText freq;
    private SeekBar bar;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this.getApplication();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        local = findViewById(R.id.local_execution);
        submit = findViewById(R.id.submit);
        subArrayButton = findViewById(R.id.radioButton1);
        radioGroup = findViewById(R.id.radioGroup);
        avlButton = findViewById(R.id.radioButton1);
        freq = findViewById(R.id.freq);
        bar = findViewById(R.id.bar);
        bar.setMin(1);
        bar.setMax(3);
        submit.setOnClickListener(view -> {
            int task = getTask();
            boolean checked = local.isChecked();
            if (!checked) {
                task += 2;
            }
            int freqRate = Integer.parseInt(freq.getText().toString());
            System.out.println(freqRate + " " + bar.getProgress() + " " + task);
            Intent intent = new Intent(getBaseContext(), ExecutionActivity.class);
            intent.putExtra("freq", freqRate);
            intent.putExtra("local", checked);
            intent.putExtra("input", bar.getProgress());
            intent.putExtra("task", task);
            startActivity(intent);
        });

    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    private int getTask() {
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(radioButtonID);
        int idx = radioGroup.indexOfChild(radioButton);
        switch (idx) {
            case 0:
                return 1;
            case 1:
            break;
        }
        throw new IllegalArgumentException("No valid task");
    }
}