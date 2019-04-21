package ordo.azurewebsites.net.ordo;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SignUpActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SignUpFragment();
    }

}
