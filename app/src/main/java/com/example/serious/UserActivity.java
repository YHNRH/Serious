package com.example.serious;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tw = new TextView(this);
        tw.setText("Hi, " + getIntent().getCharSequenceExtra("login"));
        setContentView(tw);
    }
}