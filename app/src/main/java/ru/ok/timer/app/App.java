package ru.ok.timer.app;


import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String TIMER_TAG = "APPLICATION_TIMER";
    public static final String NOTIFICATION_TIMER_CHANNEL_DESCRIPTION = "NOTIFICATION_TIMER_CHANNEL_DESCRIPTION";
    private NotificationManager notificationManager;
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel timerChannel = new NotificationChannel(TIMER_TAG, "timerChannel", NotificationManager.IMPORTANCE_DEFAULT);
            timerChannel.setDescription(NOTIFICATION_TIMER_CHANNEL_DESCRIPTION);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(timerChannel);
        }
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public static App getInstance() {
        return instance;
    }
}
