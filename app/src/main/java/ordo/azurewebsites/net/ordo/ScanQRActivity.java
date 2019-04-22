package ordo.azurewebsites.net.ordo;

import android.support.v4.app.Fragment;

import ordo.azurewebsites.net.ordo.SingleFragmentActivity;

public class ScanQRActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ScanQRFragment();
    }
}
