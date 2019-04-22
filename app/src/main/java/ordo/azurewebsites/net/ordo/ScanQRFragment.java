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

public class ScanQRFragment extends Fragment {
    private Button mOKButton;
    private Button mNotOkButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_qr,container,false);
        mOKButton = view.findViewById(R.id.qr_code_button_correct);
        mNotOkButton = view.findViewById(R.id.qr_code_button_incorrect);

        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MainActivity.newIntent(getActivity(),true);
                getActivity().finish();
                startActivity(intent);
            }
        });

        mNotOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MainActivity.newIntent(getActivity(),false);
                getActivity().finish();
                startActivity(intent);
            }
        });
        return view;
    }
}
