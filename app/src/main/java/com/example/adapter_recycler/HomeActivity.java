package com.example.adapter_recycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
        Powell.getDatabase();

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
            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Car model) {
                holder.textView.setText(model.getCarName());
                Picasso.get().load(model.getImageUrl()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(getApplicationContext(), "Home is working" + position, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(HomeActivity.this, Test.class);
                       // intent.putExtra("CarKey", getRef(position).getKey());
                        intent.putExtra("CarKey", "Powell is good boy "+ position);
                        startActivity(intent);
                    }
                });

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

    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }


    /*@Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        adapter.stopListening();
    }


    public static class Powell {
        private static FirebaseDatabase mDatabase;

        public static FirebaseDatabase getDatabase() {
            if (mDatabase == null) {
                mDatabase = FirebaseDatabase.getInstance();
                mDatabase.setPersistenceEnabled(true);
            }
            return mDatabase;
        }

    }
}
