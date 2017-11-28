package com.example.zunay.onechat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private TextView display_name;
    private TextView status;
    private DatabaseReference databaseReference;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar=findViewById(R.id.profile_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String user_id=getIntent().getStringExtra("user_id");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        display_name=findViewById(R.id.textView_profile_display_name);
        status=findViewById(R.id.textView_profile_status);
        //display.setText(user_id);
    }
}
