package ordo.azurewebsites.net.ordo;

import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInFragment extends Fragment {

        private EditText mEmailInput;
        private EditText mPasswordInput;
        private Button mSignUpButton;
        private Button mLogInButton;
        private ProgressBar mProgressBar;
        private FirebaseAuth mAuth;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mAuth = FirebaseAuth.getInstance();
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_log_in,container,false);
            mEmailInput = v.findViewById(R.id.email_input);
            mPasswordInput = v.findViewById(R.id.password_input);
            mSignUpButton = v.findViewById(R.id.sign_up_button);
            mLogInButton = v.findViewById(R.id.log_in_button);
            mProgressBar = v.findViewById(R.id.progress_bar_sign_up);

            mLogInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = mEmailInput.getText().toString();
                    String password = mPasswordInput.getText().toString();
                    if(email!=null && !email.isEmpty() && password!=null && !password.isEmpty()){
                        mProgressBar.setVisibility(View.VISIBLE);
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        mProgressBar.setVisibility(View.INVISIBLE);
                                        if (task.isSuccessful()) {
                                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                                getActivity().finish();
                                                startActivity(intent);
                                        } else {
                                            Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            });

            mSignUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(getActivity(), SignUpActivity.class);
                    startActivity(intent);
                }
            });






            return v;
        }
}
