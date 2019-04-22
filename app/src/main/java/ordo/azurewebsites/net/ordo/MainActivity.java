package ordo.azurewebsites.net.ordo;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar mToolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,mToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new ScanFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
            navigationView.setCheckedItem(R.id.nav_scan);
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
