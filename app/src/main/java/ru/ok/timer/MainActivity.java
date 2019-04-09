package ru.ok.timer;

import androidx.appcompat.app.AppCompatActivity;
import ru.ok.timer.services.TimerService;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView timerView = findViewById(R.id.timer);
    private static final String TIMER_VALUE_TAG = "TIMER_VALUE";
    private Button startStopButton = findViewById(R.id.btn_start_stop);
    private Button resetButton = findViewById(R.id.btn_reset);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startTimerService() {
        String timerValue = timerView.toString();
        Intent serviceIntent = new Intent(this, TimerService.class);
        serviceIntent.putExtra(TIMER_VALUE_TAG, timerValue);
        startService(serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, TimerService.class);
        stopService(serviceIntent);
    }
}
