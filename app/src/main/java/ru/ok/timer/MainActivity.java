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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, EventContext {


    private TextView timerView;
    private Button startStopButton;
    private Button resetButton;
    private boolean isBound = false;
    private TimerService timerService;
    private TimerService.TimerServiceBinder binder;
    private boolean isTimerRunning = false;
    private Intent timerIntent;

    @Override
    protected void onStop() {
        super.onStop();
        if(isBound) {
            unbindService(timerConnection);
            isBound = false;
        }
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

                    }
                    else {
                        ContextCompat.startForegroundService(this, timerIntent);
                    }
                }
                break;
            case R.id.btn_reset:
                if(isBound && binder != null) {

                }
                break;
        }
    }

    private ServiceConnection timerConnection = new ServiceConnection() {

        private EventContext eventContext = MainActivity.this;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (TimerService.TimerServiceBinder) service;
            binder.setEventContext(eventContext);
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
