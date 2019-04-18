package ru.ok.timer.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import ru.ok.timer.MainActivity;
import ru.ok.timer.R;
import ru.ok.timer.app.App;
import ru.ok.timer.callbacks.EventContext;

import static ru.ok.timer.app.App.TIMER_TAG;

public class TimerService extends Service {

    private TimerServiceBinder timerServiceBinder = new TimerServiceBinder();
    private static final int TIMER_NOTIFICATION_ID = 1;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private CountDownTimer timer;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long endTimer;
    private static final long START_TIME_IN_MILLIS = 600000;
    private boolean timerRunning;
    private EventContext eventContext;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = App.getInstance().getNotificationManager();

        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder = new NotificationCompat.Builder(this, TIMER_TAG);
        Notification notification = notificationBuilder.setContentTitle("Timer")
                .setContentText("00:00:0000")
                .setSmallIcon(R.drawable.ic_notification_timer)
                .setContentIntent(pendingIntent).build();
        startForeground(TIMER_NOTIFICATION_ID, notification);
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null) {
            timer.onFinish();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return timerServiceBinder;
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }


    private void startTimer() {
        timer = new CustomTimer(mTimeLeftInMillis, 1000);
        Log.d("START_TIMER", String.valueOf(this));
        timer.start();
        timerRunning = true;
    }

    private void stopTimer() {
        timer.cancel();
        timerRunning = false;
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
    }


    public final class TimerServiceBinder extends Binder {

        public void setEventContext(EventContext evContext) {
            eventContext = evContext;
        }
    }

    private final class CustomTimer extends CountDownTimer {

        CustomTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.d("RUNTIMER", String.valueOf(this));
            endTimer = System.currentTimeMillis() + mTimeLeftInMillis;
            mTimeLeftInMillis = millisUntilFinished;
            int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
            int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
            int millis = (int) (mTimeLeftInMillis % 1000);
            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%04d", minutes, seconds, millis);
            notificationBuilder.setContentText(timeLeftFormatted);
            eventContext.setTimerValue(timeLeftFormatted);
            notificationManager.notify(TIMER_NOTIFICATION_ID, notificationBuilder.build());
        }

        @Override
        public void onFinish() {
            timerRunning = false;
        }
    }
}