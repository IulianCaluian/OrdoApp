package ordo.azurewebsites.net.ordo;

import android.support.v4.app.Fragment;

public class LogInActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new LogInFragment();
    }
}
