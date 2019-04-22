package ordo.azurewebsites.net.ordo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.UUID;

public class ItemOrderActivity extends SingleFragmentActivity {
    public static final String EXTRA_ITEM_ORDER_ID = "ordo.azurewebsites.net.ordo.item_order_id";

    private Toolbar mToolbar;

    public static Intent newIntent(Context packageContext, UUID itemOrderId) {
        Intent intent = new Intent(packageContext, ItemOrderActivity.class);
        intent.putExtra(EXTRA_ITEM_ORDER_ID, itemOrderId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID itemOrderId = (UUID) getIntent().getSerializableExtra(EXTRA_ITEM_ORDER_ID);
        return ItemOrderFragment.newInstance(itemOrderId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_toolbar);


        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
