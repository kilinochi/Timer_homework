package ru.ok.timer.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import ru.ok.timer.MainActivity;
import ru.ok.timer.R;


import static ru.ok.timer.app.App.TIMER_TAG;

public class TimerService extends Service {

    private static final String TIMER_VALUE_TAG = "TIMER_VALUE";
    private static final int TIMER_NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String timerValue = intent.getStringExtra(TIMER_VALUE_TAG);

        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, 0);

        Notification notification = new NotificationCompat.Builder(this,TIMER_TAG)
                .setContentTitle("Timer")
                .setContentText(timerValue)
                .setSmallIcon(R.drawable.ic_notification_timer)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(TIMER_NOTIFICATION_ID, notification);

        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
