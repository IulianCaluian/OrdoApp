package ordo.azurewebsites.net.ordo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ordo.azurewebsites.net.ordo.model.Restaurant;
import ordo.azurewebsites.net.ordo.network.GetRestaurantDataService;
import ordo.azurewebsites.net.ordo.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantFragment extends Fragment {
    private boolean mActivityPaused = true;
    private TextView mNameTextView;
    private TextView mLocationTextView;
    private TextView mOrarTextView;
    private TextView mWelcomeTextView;
    private TextView mTableTextView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant,container,false);
        mNameTextView = view.findViewById(R.id.restaurant_name_text_view);
        mOrarTextView = view.findViewById(R.id.restaurant_orar_text_view);
        mLocationTextView = view.findViewById(R.id.restaurant_location_text_view);
        mWelcomeTextView = view.findViewById(R.id.restaurant_welcome_text_view);
        mTableTextView = view.findViewById(R.id.restaurant_table_text_view);
        mWelcomeTextView.setVisibility(View.GONE);

        /*Create handle for the RetrofitInstance interface*/
        GetRestaurantDataService service = RetrofitInstance.getRetrofitInstance().create(GetRestaurantDataService.class);

        /*Call the method with parameter in the interface to get the employee data*/
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        int restaurantID = sharedPref.getInt(getString(R.string.save_restaurant_id),0);
        Call<Restaurant> call = service.getItemData(restaurantID);

        Log.wtf("URL Calling: ", call.request().url() + ".");
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (mActivityPaused) return;
                try {
                    mWelcomeTextView.setVisibility(View.VISIBLE);
                    mNameTextView.setText(response.body().getRestaurantName());
                    mLocationTextView.setText(response.body().getRestaurantLocation());
                    mOrarTextView.setText(response.body().getRestaurantOrar());

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    int table = sharedPref.getInt((getString(R.string.save_table)),0);
                    mTableTextView.setText("Masa " + table);
                } catch (Exception ex){
                    Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                if (mActivityPaused) return;
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.wtf("URL Called", call.request().url() + t.toString());
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivityPaused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivityPaused = false;
    }
}
