package ordo.azurewebsites.net.ordo.model;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class Item implements Serializable {
    @SerializedName("item_id")
    public UUID ItemId;
    @SerializedName("item_name")
    public String ItemName;
    @SerializedName("item_quantity")
    public int ItemQuantity;
    @SerializedName("item_price")
    public BigDecimal ItemPrice;
    @SerializedName("restaurant_id")
    public int RestaurantId;

    public Item(UUID itemId, String itemName, int itemQuantity, BigDecimal itemPrice, int restaurantId) {
        ItemId = itemId;
        ItemName = itemName;
        ItemQuantity = itemQuantity;
        ItemPrice = itemPrice;
        RestaurantId = restaurantId;
    }

    public UUID getItemId() {
        return ItemId;
    }

    public void setItemId(UUID itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getItemQuantity() {
        return ItemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        ItemQuantity = itemQuantity;
    }

    public BigDecimal getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        ItemPrice = itemPrice;
    }

    public int getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        RestaurantId = restaurantId;
    }
}