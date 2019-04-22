package ordo.azurewebsites.net.ordo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import ordo.azurewebsites.net.ordo.R;
import ordo.azurewebsites.net.ordo.model.Item;
import ordo.azurewebsites.net.ordo.model.ItemList;
import ordo.azurewebsites.net.ordo.network.GetItemDataService;
import ordo.azurewebsites.net.ordo.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import ordo.azurewebsites.net.ordo.network.GetItemDataService;
import ordo.azurewebsites.net.ordo.network.RetrofitInstance;

public class MenuFragment extends Fragment {
    private ItemAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_menu,container,false);

        /*Create handle for the RetrofitInstance interface*/
        GetItemDataService service = RetrofitInstance.getRetrofitInstance().create(GetItemDataService.class);

        /*Call the method with parameter in the interface to get the employee data*/
        Call<ItemList> call = service.getItemData(1);


        call.enqueue(new Callback<ItemList>() {
            @Override
            public void onResponse(Call<ItemList> call, Response<ItemList> response) {
                generateEmployeeList(view,response.body().getItemsArrayList());
            }

            @Override
            public void onFailure(Call<ItemList> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.wtf("URL Called", call.request().url() + t.toString());
            }
        });

        return view;
    }


    /*Method to generate List of employees using RecyclerView with custom adapter*/
    private void generateEmployeeList(View view,ArrayList<Item> itemDataList) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_item_list);

        adapter = new ItemAdapter(itemDataList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }


    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        private ArrayList<Item> dataList;

        public ItemAdapter (ArrayList<Item> dataList){
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View view = layoutInflater.inflate(R.layout.row_item, viewGroup, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
            itemViewHolder.txtItemName.setText(dataList.get(i).getItemName());
            itemViewHolder.txtItemPrice.setText(dataList.get(i).getItemPrice().toString() + " lei");
            itemViewHolder.txtItemQuantity.setText(dataList.get(i).getItemQuantity() + " ml");
            itemViewHolder.itm = dataList.get(i);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView txtItemName, txtItemQuantity, txtItemPrice;
            Button btn;
            Item itm;

            ItemViewHolder(View itemView) {
                super(itemView);
                txtItemName = (TextView) itemView.findViewById(R.id.txt_item_name);
                txtItemQuantity = (TextView) itemView.findViewById(R.id.txt_item_quantity);
                txtItemPrice = (TextView) itemView.findViewById(R.id.txt_item_price);
                btn = (Button) itemView.findViewById(R.id.button_send);
                btn.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                // trimit itm-ul;
                final String message = "1x " + itm.getItemName() + "," + itm.getItemQuantity() + ".............." + itm.getItemPrice().toString();
                final ConnectionFactory factory = new ConnectionFactory();
                final String QUEUE_NAME = "restaurant_q_" + itm.getRestaurantId();

                final JSONObject obj = new JSONObject();
                try {
                    obj.put("order", message);
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

                Thread thread = new Thread() {
                    @Override
                    public void run() {

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
                    }
                };
                thread.start();
            }


        }
    }

}
