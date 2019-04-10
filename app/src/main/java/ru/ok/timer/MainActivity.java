package ru.ok.timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import ru.ok.timer.services.TimerService;



import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final long START_TIME_IN_MILLIS = 600000;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private TextView timerView;
    private static final String TIMER_VALUE_TAG = "TIMER_VALUE";
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private Button startStopButton;
    private Button resetButton;
    private Intent timerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerView = findViewById(R.id.timer);
        resetButton = findViewById(R.id.btn_reset);
        startStopButton = findViewById(R.id.btn_start_stop);
        timerIntent = new Intent(this, TimerService.class);
        startStopButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
    }

    public void startTimerService() {
        String timerValue = timerView.getText().toString();
        timerIntent.putExtra(TIMER_VALUE_TAG, timerValue);
        ContextCompat.startForegroundService(this, timerIntent);
    }

    public void stopTimerService() {
        stopService(timerIntent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                resetTimer();
                break;
            case R.id.btn_start_stop:
                if(timerRunning) {
                    stopTimer();
                }
                else {
                    startTimer();
                }
                break;
        }
    }

    private void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        startStopButton.setText(R.string.button_start);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                startStopButton.setText(getString(R.string.button_start));
            }
        }.start();
        timerRunning = true;
        startStopButton.setText(getString(R.string.timer_pause));
    }

    private void updateTimer() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerView.setText(timeLeftFormatted);
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;;
        updateTimer();
    }
}
