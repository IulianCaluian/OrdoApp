package ordo.azurewebsites.net.ordo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ItemList {

    @SerializedName("items")
    private ArrayList<Item> Items;

    public ArrayList<Item> getItemsArrayList() {
        return Items;
    }

    public void setEmployeeArrayList(ArrayList<Item> itemArrayList) {
        this.Items = itemArrayList;
    }
}
