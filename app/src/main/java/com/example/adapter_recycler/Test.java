package com.example.adapter_recycler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Test extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textView = findViewById(R.id.textView);

        String CarKey = getIntent().getStringExtra("CarKey");
        textView.setText(CarKey);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}