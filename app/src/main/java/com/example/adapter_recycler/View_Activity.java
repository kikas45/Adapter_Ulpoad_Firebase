package com.example.adapter_recycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class View_Activity extends AppCompatActivity {
TextView textView_v;
ImageView imageView_V;
Button button_delete;
DatabaseReference ref, Dataref;
StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        textView_v = findViewById(R.id.textView_v);
        imageView_V = findViewById(R.id.imageView_v);
        button_delete = findViewById(R.id.btnDelete_v);
        ref = FirebaseDatabase.getInstance().getReference().child("Car");

        String CarKey = getIntent().getStringExtra("CarKey");

        //// deleteimg the data
        Dataref = FirebaseDatabase.getInstance().getReference().child("Car").child(CarKey);
        storageRef = FirebaseStorage.getInstance().getReference().child("CarImage").child(CarKey + ".jpg");



        ref.child(CarKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    String crname =snapshot.child("CarName").getValue().toString();
                    String imageurl =snapshot.child("ImageUrl").getValue().toString();
                    textView_v.setText(crname);

                    Picasso.get().load(imageurl).into(imageView_V);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dataref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                finish();

                                ///
                                /*  new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                                    }}, 20);*/
                                ///
                            }
                        });

                    }
                });

            }
        });
}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}