package ordo.azurewebsites.net.ordo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class SignUpFragment extends Fragment {
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private Button mSignUpButton;
    private ProgressBar mProgressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up,container,false);
        mEmailInput = v.findViewById(R.id.email_input);
        mPasswordInput = v.findViewById(R.id.password_input);
        mSignUpButton = v.findViewById(R.id.sign_up_button);
        mProgressBar = v.findViewById(R.id.progress_bar_sign_up)

        return v;
    }
}
