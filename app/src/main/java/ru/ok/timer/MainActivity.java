package ru.ok.timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import ru.ok.timer.services.TimerService;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView timerView;
    private static final String TIMER_VALUE_TAG = "TIMER_VALUE";
    private Button startStopButton;
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerView = findViewById(R.id.timer);
        resetButton = findViewById(R.id.btn_reset);
        startStopButton = findViewById(R.id.btn_start_stop);
        startTimerService();
    }

    public void startTimerService() {
        String timerValue = timerView.getText().toString();
        Intent serviceIntent = new Intent(this, TimerService.class);
        serviceIntent.putExtra(TIMER_VALUE_TAG, timerValue);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, TimerService.class);
        stopService(serviceIntent);
    }
}
