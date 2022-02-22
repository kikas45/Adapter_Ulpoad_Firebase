package com.example.adapter_recycler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_IMAGE = 10;
    private ImageView imageViewViewAdd;
    private EditText inputImageName;
    private TextView textViewprogress;
    private ProgressBar progressBar;
    private Button btn_upload;

    /// crate the image request
    Uri imageUri;
    boolean isImageAdded = false;

    //firebase refrence

    DatabaseReference DataRef;
    StorageReference StoragerRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        imageViewViewAdd = findViewById(R.id.imageViewAdd);
        inputImageName = findViewById(R.id.inputImageName);
        textViewprogress = findViewById(R.id.textViewProgress);
        progressBar = findViewById(R.id.progress_bar);
        btn_upload = findViewById(R.id.btn_upload);

        ///settig up progress bar
        textViewprogress.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        // Initilazing the data

        DataRef   = FirebaseDatabase.getInstance().getReference().child("Car");
        StoragerRef = FirebaseStorage.getInstance().getReference().child("CarImage");


        // OPENING THE PHINE STORAGE
        imageViewViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

        //what next when bnt_upload button is clecke?

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///we need to get the data and uri
                final String imgName = inputImageName.getText().toString();
                if (isImageAdded!=false && imgName !=null){
                    UploadImage(imgName);
                }
            }
        });

    }

    private void UploadImage( final  String imgName) {
        textViewprogress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        final  String key  = DataRef.push().getKey();
        StoragerRef.child(key + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                StoragerRef.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        HashMap hashMap = new HashMap();
                        hashMap.put("CarName", imgName);
                        hashMap.put("ImageUrl", uri.toString());
                        // add the data
                        DataRef.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                               // Toast.makeText(MainActivity.this, "Data successfully added ", Toast.LENGTH_SHORT).show();
                                textViewprogress.setText("Completed");
                                /// taking the activity to new place

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        // Setting up a dialog
                                        AlertDialog.Builder builder
                                                = new AlertDialog
                                                .Builder(MainActivity.this);
                                        // Set the message show for the Alert time
                                        builder.setMessage("Do you want to exit or Go back to Home Activity ?");
                                        // Set Alert Title
                                        builder.setTitle("Task is Completed");
                                        // Set Cancelable false
                                        // for when the user clicks on the outside
                                        // the Dialog Box then it will remain show
                                        builder.setCancelable(false);
                                        // Set the positive button with yes name
                                        // OnClickListener method is use of
                                        // DialogInterface interface.
                                        builder.setPositiveButton("Yes, Go back", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // When the user click yes button
                                                // then app will close
                                                //finish();
                                                Intent i=new Intent(MainActivity.this,HomeActivity.class);
                                                startActivity(i);
                                                finish();
                                                //
                                            }
                                        });

                                        // Set the Negative button with No name
                                        // OnClickListener method is use
                                        // of DialogInterface interface.
                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                // If user click no
                                                // then dialog box is canceled.
                                                dialog.cancel(); }});
                                        // Create the Alert dialog
                                        AlertDialog alertDialog = builder.create();
                                        // Show the Alert Dialog box
                                        alertDialog.show();
                                        // setting up a dialog
                                    }}, 1000);

                                ////END OF HANDLER


                            }
                        });

                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot tasksnapshot) {


                double progress = (tasksnapshot.getBytesTransferred() * 100)/tasksnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                //progressBar.setProgress(((int) progress));
                textViewprogress.setText(progress + "%");



            }
        });

    }

    /// Back to Obncrete Mathod


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE && data!=null){
            imageUri = data.getData();
            isImageAdded =true;
            imageViewViewAdd.setImageURI(imageUri);


        }
    }

}