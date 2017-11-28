package com.example.zunay.onechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    Button create_btn;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;//progressbar
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar=findViewById(R.id.registration_app_bar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog=new ProgressDialog(this);

        name=findViewById(R.id.editText_name_register);
        email=findViewById(R.id.editText_email_register);
        password=findViewById(R.id.editText_password_register);

        create_btn=findViewById(R.id.create_button_register);
        mAuth=FirebaseAuth.getInstance();
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String m_name=name.getText().toString();
                String m_email=email.getText().toString();
                String m_password=password.getText().toString();

                if(!TextUtils.isEmpty(m_name) || !TextUtils.isEmpty(m_email) || !TextUtils.isEmpty(m_password))
                {
                    progressDialog.setTitle("Registration Going On");
                    progressDialog.setMessage("Please wait for a while");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    registerUser(m_name,m_email,m_password);
                }

            }
        });

    }

    private void registerUser(final String m_name, final String m_email, final String m_password) {
        mAuth.createUserWithEmailAndPassword(m_email, m_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                            databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

                            HashMap<String,String> userMap= new HashMap<>();
                            userMap.put("name",m_name);
                            userMap.put("email",m_email);
                            userMap.put("password",m_password);
                            userMap.put("image","default");
                            userMap.put("thumbnail","default");
                            userMap.put("status","I am using one chat");
                            databaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    Intent main_intent= new Intent(RegisterActivity.this,MainActivity.class);
                                    //Now it will not go back to login page when user submit the back button
                                    main_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(main_intent);
                                    finish();
                                }
                            });

                        }
                        else
                        {
                            progressDialog.hide();
                            Toast.makeText(getApplicationContext(), "Authentication failed. Please check your details",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
