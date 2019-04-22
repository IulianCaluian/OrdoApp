package ordo.azurewebsites.net.ordo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class ItemOrderActivity extends SingleFragmentActivity {
    public static final String EXTRA_ITEM_ORDER_ID = "ordo.azurewebsites.net.ordo.item_order_id";

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
}
