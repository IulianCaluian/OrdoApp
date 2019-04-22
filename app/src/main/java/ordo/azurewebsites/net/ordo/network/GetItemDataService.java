package ordo.azurewebsites.net.ordo.network;

import ordo.azurewebsites.net.ordo.model.ItemList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetItemDataService {
    @GET("/api/values/{RestaurantId}")
    Call<ItemList> getItemData(@Path("RestaurantId") int curentRestaurantId);
}
