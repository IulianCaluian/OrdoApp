package ordo.azurewebsites.net.ordo.model;

import com.google.gson.annotations.SerializedName;

public class Restaurant {
    @SerializedName("restaurant_id")
    private int RestaurantId;
    @SerializedName("restaurant_name")
    private String RestaurantName;
    @SerializedName("restaurant_location")
    private String RestaurantLocation;
    @SerializedName("restaurant_orar")
    private String RestaurantOrar;

    public String getRestaurantName() {
        return RestaurantName;
    }

    public String getRestaurantLocation() {
        return RestaurantLocation;
    }

    public String getRestaurantOrar() {
        return RestaurantOrar;
    }
}
