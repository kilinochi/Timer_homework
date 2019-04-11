package ru.ok.timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import ru.ok.timer.services.TimerService;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final long START_TIME_IN_MILLIS = 600000;
    private static final String SHARED_PREFERENCE_TAG = "TIMER_PREFERENCE";
    private static final String TIME_MILLIS_LEFT_TAG = "TIME_MILLIS_LEFT";
    private static final String TIMER_RUNNING_TAG = "TIMER_RUNNING";
    private static final String END_TIMER_TAG = "END_TIMER";
    private long mTimeLeftInMillis;
    private long endTimer;
    private TextView timerView;
    private static final String TIMER_VALUE_TAG = "TIMER_VALUE";
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private Button startStopButton;
    private Button resetButton;
    private Intent timerIntent;



    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(TIME_MILLIS_LEFT_TAG, mTimeLeftInMillis);
        editor.putLong(END_TIMER_TAG, endTimer);
        editor.putBoolean(TIMER_RUNNING_TAG, timerRunning);
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_TAG, MODE_PRIVATE);
        mTimeLeftInMillis = sharedPreferences.getLong(TIME_MILLIS_LEFT_TAG, START_TIME_IN_MILLIS);
        timerRunning = sharedPreferences.getBoolean(TIMER_RUNNING_TAG, false);
        updateTimer();
        updateButtons();
        if(timerRunning) {
            endTimer = sharedPreferences.getLong(END_TIMER_TAG, 0);
            mTimeLeftInMillis = endTimer - System.currentTimeMillis();
            if(mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                timerRunning = false;
                updateTimer();
                updateButtons();
            }
            else {
                startTimer();
            }
        }
    }

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

    private void startTimerService() {
        String timerValue = timerView.getText().toString();
        timerIntent.putExtra(TIMER_VALUE_TAG, timerValue);
        ContextCompat.startForegroundService(this, timerIntent);
    }

    private void stopTimerService() {
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
        updateButtons();
    }

    private void startTimer() {
        endTimer = System.currentTimeMillis() + mTimeLeftInMillis;
        countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                updateButtons();
            }
        }.start();
        timerRunning = true;
        updateButtons();
    }

    private void updateTimer() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerView.setText(timeLeftFormatted);
        if(timerRunning) {
            startTimerService();
        }
        else {
            stopTimerService();
        }
    }

    private void resetTimer() {
        timerRunning = false;
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateTimer();
    }

    private void updateButtons() {
        if(timerRunning) {
            startStopButton.setText(R.string.timer_pause);
            resetButton.setVisibility(View.GONE);
        }
        else {
            startStopButton.setText(R.string.button_start);
            if(mTimeLeftInMillis < 1000) {
                startStopButton.setVisibility(View.GONE);
            }
            else {
                startStopButton.setVisibility(View.VISIBLE);
            }
            if(mTimeLeftInMillis < START_TIME_IN_MILLIS) {
                resetButton.setVisibility(View.VISIBLE);
            }
            else {
                resetButton.setVisibility(View.GONE);
            }
        }
    }
}
