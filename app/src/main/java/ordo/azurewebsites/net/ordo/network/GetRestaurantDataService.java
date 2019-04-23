package ordo.azurewebsites.net.ordo.network;

import ordo.azurewebsites.net.ordo.model.Restaurant;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetRestaurantDataService {
    @GET("/api/restaurant/{RestaurantId}")
    Call<Restaurant> getItemData(@Path("RestaurantId") int curentRestaurantId);
}
