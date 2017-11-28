package com.example.zunay.onechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    Button register_now;
    Button sign_in_now;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        register_now=findViewById(R.id.register_now);
        sign_in_now=findViewById(R.id.sign_in_now);
        register_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_register= new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(intent_register);
            }
        });
        sign_in_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_sign_in= new Intent(StartActivity.this,LoginActivity.class);
                startActivity(intent_sign_in);
            }
        });
    }
}
