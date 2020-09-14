package com.example.serious;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        TextView tw = new TextView(this);
        tw.setText("Hi, " + getIntent().getCharSequenceExtra("login"));
        ll.addView(tw);
        if(getIntent().getIntExtra("role", 0 ) == 1)
        { Button client = new Button(getApplicationContext());
        client.setText("Перейти к админу");
        ll.addView(client);
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserActivity.this, AdminActivity.class );
                i.putExtra("login", getIntent().getCharSequenceExtra("login"));
                startActivity(i);
            }
        });}
        setContentView(ll);
    }
}