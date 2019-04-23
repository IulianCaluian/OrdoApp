package ordo.azurewebsites.net.ordo;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

public class PollService extends IntentService {
    private static final String TAG = "PollService";

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
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

                QueueingConsumer consumer = new QueueingConsumer(channel);
                channel.basicConsume(QUEUE_NAME, consumer);
                //while(true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                int n = channel.queueDeclarePassive(QUEUE_NAME).getMessageCount();
                if(delivery != null) {
                    byte[] bs = delivery.getBody();
                    Log.wtf(TAG, "Mesaj: "  + new String(bs));
                    //String message= new String(delivery.getBody());
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    //System.out.println("[x] Received '"+message);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
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






        Log.i(TAG, "Received an intent: " + intent);
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
}
