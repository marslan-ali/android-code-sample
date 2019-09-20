package co.appdev.invited.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Inject;

import co.appdev.invited.R;
import co.appdev.invited.ui.home.HomeActivity;
import co.appdev.invited.ui.notification.NotificationPresenter;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Inject
    NotificationPresenter notificationPresenter;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String title="",body = "", code = "";
        Integer notificationId = null;

        if (remoteMessage.getData().size() > 0) {
            title = remoteMessage.getData().get("Title");
            body = remoteMessage.getData().get("Body");
            code = remoteMessage.getData().get("code");
            notificationId = Integer.valueOf(remoteMessage.getData().get("notification_id"));
        }

        showNotification(getApplicationContext(), title, body, code, String.valueOf(notificationId));
    }


    public void showNotification(Context context, String title, String body, String code, String notificationId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(getNotificationIcon())
                .setColor(Color.RED)
                .setContentTitle(title)
                .setContentText(body);

        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("code", code);
        intent.putExtra("notification_id", notificationId);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);

        notificationManager.notify(Integer.parseInt(notificationId), mBuilder.build());

    }

    protected static int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.N);
        return useWhiteIcon ? R.mipmap.splash_icon_2: R.mipmap.app_icon;
    }
}