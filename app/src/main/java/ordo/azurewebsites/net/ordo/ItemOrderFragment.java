package ordo.azurewebsites.net.ordo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import ordo.azurewebsites.net.ordo.cv_helper.BitmapUtils;
import ordo.azurewebsites.net.ordo.cv_helper.FaceDetectorCC;
import ordo.azurewebsites.net.ordo.model.ItemOrder;
import ordo.azurewebsites.net.ordo.model.ItemOrderLib;

public class ItemOrderFragment extends Fragment {
    private static final String ARG_ITEM_ORDER_ID = "item_order_id";

    private TextView mTitleTextView;
    private TextView mDateTextView;
    private ItemOrder mItemOrder;
    ImageView mClientImageView;
    TextView mClientStateTextView;
    Button mReviewButton;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private static final String FILE_PROVIDER_AUTHORITY = "ordo.azurewebsites.net.fileprovider";
    private String mTempPhotoPath;
    private Bitmap mResultsBitmap;

    public static ItemOrderFragment newInstance(UUID itemOrderId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM_ORDER_ID, itemOrderId);
        ItemOrderFragment fragment = new ItemOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID itemOrderId = (UUID) getArguments().getSerializable(ARG_ITEM_ORDER_ID);
        Log.wtf("TEST","Am id-ul:"+itemOrderId+".");
        mItemOrder = ItemOrderLib.get(getActivity()).getItemOrder(itemOrderId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_order,container,false);


        mTitleTextView = v.findViewById(R.id.item_order_title);
        mDateTextView = v.findViewById(R.id.item_order_date);
        mClientImageView = v.findViewById(R.id.client_image_view);
        mClientStateTextView = v.findViewById(R.id.client_state_text_view);
        mReviewButton = v.findViewById(R.id.review_button_rv);
       // mClientStateTextView.setText("safasdf xxx");
        if(mItemOrder!=null) {
            mTitleTextView.setText(mItemOrder.getTitle());
            mDateTextView.setText(mItemOrder.getDate().toString());
        }

        mReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),"ok",Toast.LENGTH_LONG).show();
                emojifyMe();
            }
        });

        return v;
    }





    /**
     * OnClick method for "Review Me!" Button. Launches the camera app.
     */
    public void emojifyMe() {
        // Check for the external storage permission
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // If you do not have permission, request it
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            // Launch the camera if the permission exists
            launchCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Called when you request permission to read and write to external storage
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, launch the camera
                    launchCamera();
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(getContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    /**
     * Creates a temporary image file and captures a picture to store in it.
     */
    private void launchCamera() {

        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(getContext());
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                mTempPhotoPath = photoFile.getAbsolutePath();

                // Get the content URI for the image file
                Log.wtf("ttt" , "AM CEVA" + FILE_PROVIDER_AUTHORITY);

                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the image capture activity was called and was successful
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // Process the image and set it to the TextView
            processAndSetImage();
        } else {

            // Otherwise, delete the temporary image file
            BitmapUtils.deleteImageFile(getContext(), mTempPhotoPath);
        }
    }

    /**
     * Method for processing the captured image and setting it to the TextView.
     */
    private void processAndSetImage() {

        // Toggle Visibility of the views
        mReviewButton.setVisibility(View.GONE);


        // Resample the saved image to fit the ImageView
        mResultsBitmap = BitmapUtils.resamplePic(getContext(), mTempPhotoPath);


        // Detect the faces and overlay the appropriate emoji
        mResultsBitmap = FaceDetectorCC.detectFacesandOverlayEmoji(getContext(), mResultsBitmap);
        int state = FaceDetectorCC.detectFacesandOverlayEmojiState(getContext(), mResultsBitmap);

        // Set the new bitmap to the ImageView
        mClientImageView.setImageBitmap(Bitmap.createScaledBitmap(mResultsBitmap, 360, 640, false));
        if(state == 0) {
            mClientStateTextView.setText("Nu am putut detecta.");
        }else if (state == 1){
            mClientStateTextView.setText("Client fericit!");
        }else {
            mClientStateTextView.setText("Client nemultumit!");
        }
    }





}
