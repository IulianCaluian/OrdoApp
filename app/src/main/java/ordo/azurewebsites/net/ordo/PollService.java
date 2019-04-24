package ordo.azurewebsites.net.ordo;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.QueueingConsumer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ordo.azurewebsites.net.ordo.helper.NotificationHelper;

public class PollService extends IntentService {
    private static final String TAG = "PollService";
    private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);
    private static final int NOTIFICATION_ORDER = 1100; // Notificare comanda.

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public static void setServiceAlarm(Context context,boolean isOn){
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context,0,i,0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if(isOn) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL_MS, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context){
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context,0,i,PendingIntent.FLAG_NO_CREATE);
            return  pi != null;
    }

    public PollService() {
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if(!isNetworkAvailableAndConnected()){
            return;
        }

        boolean newMessage = false;
        final ConnectionFactory factory = new ConnectionFactory();
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = sharedPref.getString(getString(R.string.save_user_email_key),"no_user");
        final String QUEUE_NAME = "client_q_" + userName;
        String message = "mesaj_eroare";
        UUID order_id = UUID.randomUUID();
        factory.setUsername("mxexrkxf");
        factory.setPassword("GFppRhHEqe047SjDERyqjrBAFqNBvehV");
        // factory.Port = 15672;
        factory.setHost("reindeer.rmq.cloudamqp.com");
        factory.setVirtualHost("mxexrkxf");

        Connection connection = null;
        try {
            connection = factory.newConnection();
            Channel channel = null;
            try {

                channel = connection.createChannel();
                channel.queueDeclare(QUEUE_NAME, true, false, false, null);


                boolean autoAck = false;
                //Log.wtf(TAG,QUEUE_NAME);
                GetResponse response = channel.basicGet(QUEUE_NAME, autoAck);

                if (response == null) {
                    Log.wtf(TAG, "Nu am mesaj");
                } else {
                    byte[] body = response.getBody();
                    long deliveryTag = response.getEnvelope().getDeliveryTag();
                    //Log.wtf(TAG, "Mesaj: "  + new String(body));
                    channel.basicAck(deliveryTag, false); // acknowledge receipt of the message
                    try {
                        JSONObject obj = new JSONObject(new String(body));
                        message = obj.getString("message");
                        String uuidString = obj.getString("order_id");
                        order_id = UUID.fromString(uuidString);
                        Log.wtf(TAG, "Mesaj: "  + message + "," + order_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    newMessage = true;
                }
            } finally {
                try {
                    if(channel!=null && channel.isOpen())
                        channel.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (TimeoutException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally{
            try {
                if(connection!=null && connection.isOpen())
                    connection.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if(newMessage){
            Log.wtf(TAG,"Id pus in not:" + order_id);
            Intent i = ItemOrderActivity.newIntent(this,order_id);
            // both of these approaches now work: FLAG_CANCEL, FLAG_UPDATE; the uniqueInt may be the real solution.
            //PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueInt, showFullQuoteIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
            PendingIntent pi = PendingIntent.getActivity(this, uniqueInt, i, PendingIntent.FLAG_UPDATE_CURRENT);



            NotificationHelper mNotificationHelper = new NotificationHelper(this);
            Notification.Builder notificationBuilder = null;
            notificationBuilder = mNotificationHelper.getNotification("Comanda a fost preluata!",message);

            if (notificationBuilder != null) {
                notificationBuilder.setContentIntent(pi);
                mNotificationHelper.notify(NOTIFICATION_ORDER, notificationBuilder);
            }








        }

        //Log.wtf(TAG, "Received an intent: " + intent);
    }

    private boolean isNetworkAvailableAndConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
}
