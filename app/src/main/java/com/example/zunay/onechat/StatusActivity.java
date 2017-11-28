package com.example.zunay.onechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        toolbar=findViewById(R.id.status_bar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editText=findViewById(R.id.editText_status);

        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("status");
        button=findViewById(R.id.button_save_changes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status=editText.getText().toString();
                databaseReference.setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent main_intent= new Intent(StatusActivity.this,ActivitySettings.class);
                        //Now it will not go back to login page when user submit the back button
                        startActivity(main_intent);
                        finish();
                    }
                });
            }
        });

    }
}
