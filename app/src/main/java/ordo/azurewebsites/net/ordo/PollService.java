package ordo.azurewebsites.net.ordo;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PollService extends IntentService {
    private static final String TAG = "PollService";
    private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);

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


        final ConnectionFactory factory = new ConnectionFactory();
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = sharedPref.getString(getString(R.string.save_user_email_key),"no_user");
        final String QUEUE_NAME = "client_q_" + userName;
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
                GetResponse response = channel.basicGet(QUEUE_NAME, autoAck);

                if (response == null) {
                    Log.wtf(TAG, "Nu am mesaj");
                } else {
                    byte[] body = response.getBody();
                    long deliveryTag = response.getEnvelope().getDeliveryTag();
                    Log.wtf(TAG, "Mesaj: "  + new String(body));
                    channel.basicAck(deliveryTag, false); // acknowledge receipt of the message
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

        Log.wtf(TAG, "Received an intent: " + intent);
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
}
