package ordo.azurewebsites.net.ordo.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemOrderLib {
    private static ItemOrderLib sItemOrderLib;
    private List<ItemOrder> mItemOrders;

    public static ItemOrderLib get(Context context){
        if(sItemOrderLib == null){
            sItemOrderLib = new ItemOrderLib(context);
        }
        return sItemOrderLib;
    }

    private ItemOrderLib(Context context){
        mItemOrders = new ArrayList<>();
        for(int i  =0; i<100; i++){
            ItemOrder itemOrder = new ItemOrder();
            itemOrder.setTitle("Order #" + i);
            mItemOrders.add(itemOrder);
        }
    }
    public List<ItemOrder> getItemOrders(){

        return mItemOrders;
    }

    public ItemOrder getItemOrder (UUID id) {
        for (ItemOrder itemOrder : mItemOrders){
            if(itemOrder.getId().equals(id)) {
                return itemOrder;
            }
        }
        return null;
    }
}
