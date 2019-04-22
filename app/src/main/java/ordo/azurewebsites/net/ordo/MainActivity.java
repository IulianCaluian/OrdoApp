package ordo.azurewebsites.net.ordo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar mToolbar;
    private DrawerLayout drawer;
    private NavigationView mNavigationView;

    public static final String EXTRA_BOOL_HAS_RESTAURANT_ID = "ordo.azurewebsites.net.ordo.has_rest_id";

    public static Intent newIntent(Context packageContext, boolean hasAnRestaurantId) {
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(EXTRA_BOOL_HAS_RESTAURANT_ID, hasAnRestaurantId);
        return intent;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        drawer = findViewById(R.id.drawer_layout);
       mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,mToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
            if (fragment == null) {
                fragment = new ScanFragment();
                fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
                mNavigationView.setCheckedItem(R.id.nav_scan);
            }
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean hasARestaurantId = getIntent().getBooleanExtra(EXTRA_BOOL_HAS_RESTAURANT_ID,false);
        if(hasARestaurantId) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RestaurantFragment()).commit();
            mNavigationView.setCheckedItem(R.id.nav_restaurant);
        }
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_scan:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ScanFragment()).commit();
                break;
            case R.id.nav_restaurant:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RestaurantFragment()).commit();
                break;
            case R.id.nav_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MenuFragment()).commit();
                break;
            case R.id.nav_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HistoryFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingsFragment()).commit();
                break;


            case R.id.nav_log_off:
                Intent intent = new Intent(this,LogInActivity.class);
                finish();
                startActivity(intent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
