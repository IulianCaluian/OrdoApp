package ordo.azurewebsites.net.ordo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import ordo.azurewebsites.net.ordo.model.Item;
import ordo.azurewebsites.net.ordo.model.ItemOrder;
import ordo.azurewebsites.net.ordo.model.ItemOrderLib;

public class ConfirmOrderFragment extends DialogFragment {

    private static final String ARG_ITEM = "itemComandat";
    private TextView mOrderTextView;
    public static ConfirmOrderFragment newInstance(Item item) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, item);
        ConfirmOrderFragment fragment = new ConfirmOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_confirmare,null);

        mOrderTextView = v.findViewById(R.id.dialog_text_order);

        final Item itm = (Item) getArguments().getSerializable(ARG_ITEM);
        final String message = "1 x " + itm.getItemName() + " , " + itm.getItemQuantity() + " ..... " + itm.getItemPrice().toString();

        mOrderTextView.setText(message);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.string_confirmare)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // trimit itm-ul;
                        final String message = "1 x " + itm.getItemName() + " , " + itm.getItemQuantity() + " ...... " + itm.getItemPrice().toString();

                        //Adaug itm-ul in db.
                        ItemOrder itemOrder = new ItemOrder();
                        itemOrder.setTitle(message);
                        ItemOrderLib.get(getActivity()).addItemOrder(itemOrder);

                        new SendRequestTask().execute(itemOrder);

                    }
                })
                .setNegativeButton(android.R.string.cancel,null)
                .create();
    }

    private class SendRequestTask extends AsyncTask<ItemOrder, Void, Void>{

        @Override
        protected Void doInBackground(ItemOrder... itemOrders) {
            if(itemOrders == null || itemOrders.length ==0) return null;
            ItemOrder itemOrder = itemOrders[0];

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            int restaurant_id = sharedPref.getInt(getString(R.string.save_restaurant_id),1);

            String message = itemOrder.getTitle();
            ConnectionFactory factory = new ConnectionFactory();
            String QUEUE_NAME = "restaurant_q_" + restaurant_id;



            final JSONObject obj = new JSONObject();
            try {
                obj.put("order", message);
                obj.put("order_id",itemOrder.getId());
                obj.put("client","test@test.ro");
                obj.put("table", 2);
            } catch (JSONException e) {
                e.printStackTrace();
            }




            factory.setUsername("mxexrkxf");
            factory.setPassword("GFppRhHEqe047SjDERyqjrBAFqNBvehV");
            // factory.Port = 15672;
            factory.setHost("reindeer.rmq.cloudamqp.com");
            factory.setVirtualHost("mxexrkxf");
            Log.e("ROZ", "avem o incercare " + message + " Pe canalul " + QUEUE_NAME);

            Connection connection = null;
            try {
                connection = factory.newConnection();
                Channel channel = null;
                try {


                    channel = connection.createChannel();
                    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
                    channel.basicPublish("", QUEUE_NAME, null, obj.toString().getBytes("UTF-8"));
                    Log.e("ROZ", "avem o publicare " + message + " Pe canalul " + QUEUE_NAME);
                }finally {
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

            return null;
        }
    }
}
