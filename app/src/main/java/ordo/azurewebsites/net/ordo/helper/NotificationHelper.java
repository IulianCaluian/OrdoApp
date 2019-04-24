package ordo.azurewebsites.net.ordo.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import java.util.Random;
import java.util.UUID;

import ordo.azurewebsites.net.ordo.ItemOrderActivity;

public class NotificationHelper extends ContextWrapper {
    private NotificationManager mNotificationManager;
    public static final String ORDERS_CHANNEL = "orders";

    /**
     * Registers notification channels, which can be used later by individual notifications.
     *
     * @param context The application context
     */
    public NotificationHelper(Context context) {
        super(context);

        // Create the channel object with the unique ID FOLLOWERS_CHANNEL
        NotificationChannel followersChannel =
                null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            followersChannel = new NotificationChannel(
                    ORDERS_CHANNEL,
                    "Notificari comenzi",
                    NotificationManager.IMPORTANCE_DEFAULT);
        }

        // Configure the channel's initial settings
        //followersChannel.setLightColor(Color.GREEN);
        //followersChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        // Submit the notification channel object to the notification manager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getNotificationManager().createNotificationChannel(followersChannel);
        }

    }

    /**
     * Get a follow/un-follow notification
     *
     * <p>Provide the builder rather than the notification it's self as useful for making
     * notification changes.
     *
     * @param title the title of the notification
     * @param body the body text for the notification
     * @return A Notification.Builder configured with the selected channel and details
     */
    public Notification.Builder getNotification(String title, String body) {
        return new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(getSmallIcon())
                .setAutoCancel(true);
    }


    /**
     * Create a PendingIntent for opening up the MainActivity when the notification is pressed
     *
     * @return A PendingIntent that opens the MainActivity
     */
    private PendingIntent getPendingIntent(UUID order_id) {
        Intent i = ItemOrderActivity.newIntent(this,order_id);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        return pi;
    }

    /**
     * Send a notification.
     *
     * @param id The ID of the notification
     * @param notification The notification object
     */
    public void notify(int id, Notification.Builder notification) {
        getNotificationManager().notify(id, notification.build());
    }

    /**
     * Get the small icon for this app
     *
     * @return The small icon resource id
     */
    private int getSmallIcon() {
        return android.R.drawable.stat_notify_chat;
    }

    /**
     * Get the notification mNotificationManager.
     *
     * <p>Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

}
