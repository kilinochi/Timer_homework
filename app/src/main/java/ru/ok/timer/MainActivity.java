package ru.ok.timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import ru.ok.timer.callbacks.EventContext;
import ru.ok.timer.services.TimerService;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, EventContext {


    private TextView timerView;
    private boolean isBound = false;
    private TimerService.TimerServiceBinder binder;
    private boolean isTimerRunning = false;
    private Intent timerIntent;
    private Button startStopButton;
    private Button resetButton;

    @Override
    protected void onStop() {
        super.onStop();
        if(!isBound) {
            return;
        }
        unbindService(timerConnection);
        isBound = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(timerIntent, timerConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerView = findViewById(R.id.timer);
        resetButton = findViewById(R.id.btn_reset);
        startStopButton = findViewById(R.id.btn_start_stop);
        timerIntent = new Intent(this, TimerService.class);
        bindService(timerIntent, timerConnection, BIND_AUTO_CREATE);
        startStopButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_stop:
                if(isBound){
                    if(isTimerRunning) {
                        isTimerRunning = false;
                        startStopButton.setText(getString(R.string.start));
                        resetButton.setVisibility(View.VISIBLE);
                        binder.stopTimer();
                    }
                    else {
                        isTimerRunning = true;
                        startStopButton.setText(getString(R.string.stop));
                        resetButton.setVisibility(View.GONE);
                        ContextCompat.startForegroundService(this, timerIntent);
                    }
                }
                break;
            case R.id.btn_reset:
                if(isBound && binder != null) {
                    binder.resetTimer();
                    setTimerValue("10:00:0000");
                    isTimerRunning = false;
                }
                break;
        }
    }

    private ServiceConnection timerConnection = new ServiceConnection() {

        private EventContext eventContext = MainActivity.this;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (TimerService.TimerServiceBinder) service;
            binder.bind(eventContext);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    public void setTimerValue(String value) {
        timerView.setText(value);
    }
}