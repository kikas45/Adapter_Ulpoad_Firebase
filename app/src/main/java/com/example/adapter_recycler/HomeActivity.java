package com.example.adapter_recycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    EditText input_search;
    RecyclerView recycler_View;
    //
    FloatingActionButton floating_btn;
    // Firebaxse
    FirebaseRecyclerOptions<Car> options;
    FirebaseRecyclerAdapter<Car,MyViewHolder> adapter;
    //Datat refernce

    DatabaseReference Dataref2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        input_search = findViewById(R.id.input_Search);
        recycler_View = findViewById(R.id.recycler_view);

        //for offline capability
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Dataref2   = FirebaseDatabase.getInstance().getReference().child("Car");
        recycler_View.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler_View.setHasFixedSize(true);
        floating_btn = findViewById(R.id.floating_btn);
        floating_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        LoadData();
    }

    private void LoadData() {
        options  = new  FirebaseRecyclerOptions.Builder<Car>().setQuery(Dataref2, Car.class).build();

        adapter = new FirebaseRecyclerAdapter<Car, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Car model) {
                holder.textView.setText(model.getCarName());
                holder.textView_desc.setText(model.getDesc());
                Picasso.get().load(model.getImageUrl()).into(holder.imageView);

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_view, parent, false);
                return new MyViewHolder(view);
            }
        };
        ///outsode oncreate

        adapter.startListening();
        recycler_View.setAdapter(adapter);
    }
}