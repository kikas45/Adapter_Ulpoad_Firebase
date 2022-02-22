package com.example.adapter_recycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class View_Activity extends AppCompatActivity {
    TextView  TName;
    ImageView imageView_v;
    DatabaseReference ref;
    private ArrayList<Car> messagesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

     /*   ref = FirebaseDatabase.getInstance().getReference().child("Car");*/

        imageView_v = findViewById(R.id.imageView_v);
        TName = findViewById(R.id.textView_v);


        String CarKey = getIntent().getStringExtra("CarKey");

        ref.child(CarKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){



                /*    String Name = snapshot.child("CarName").getValue().toString();
                    String image = snapshot.child("ImageUrl").getValue().toString();

                    //ininliazting them
                    TName.setText(Name);
                    Picasso.get().load(image).into(imageView_v);*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}