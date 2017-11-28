package com.example.zunay.onechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ActivitySettings extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private CircleImageView circleImageView;
    private TextView displayName;
    private TextView displayMessage;
    private Button button_status;
    private Button button_image;
    private static final int GALARY_PIC=1;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        toolbar=findViewById(R.id.account_settings_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Activity Settings");
        displayName=findViewById(R.id.textView_display_name);
        displayMessage=findViewById(R.id.textView_display_message);
        circleImageView=findViewById(R.id.profile_image);
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image=dataSnapshot.child("thumbnail").getValue().toString();
                displayName.setText(name);
                displayMessage.setText(status);
                if (!thumb_image.equals("default"))
                {
                    Picasso.with(ActivitySettings.this).load(thumb_image).placeholder(R.drawable.ic_action_name).into(circleImageView);
                }


                //circleImageView.setImageResource(image);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        button_status=findViewById(R.id.button_change_status);
        button_image=findViewById(R.id.button_change_image);
        button_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ActivitySettings.this,StatusActivity.class);
                startActivity(intent);
            }
        });
        button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galaryIntent= new Intent();
                galaryIntent.setType("image/*");
                galaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galaryIntent,"Select Image"),GALARY_PIC);

                //CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(ActivitySettings.this);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALARY_PIC && resultCode==RESULT_OK) {
            Uri image_uri= data.getData();
            CropImage.activity(image_uri).setAspectRatio(1,1).start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                progressDialog= new ProgressDialog(ActivitySettings.this);
                progressDialog.setTitle("Uploading");
                progressDialog.setMessage("Please wait for a while");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                Uri resultUri = result.getUri();
                File file = new File(resultUri.getPath());
                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(70)
                            .compressToBitmap(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    final byte[] thumb_byte = baos.toByteArray();

                StorageReference filePath= mStorageRef.child("profile_images").child(currentUser.getUid()+".jpg");
                final StorageReference thumb_file_path= mStorageRef.child("profile_images").child("thumbs").child(currentUser.getUid()+".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            final String download_uri= task.getResult().getDownloadUrl().toString();
                            UploadTask uploadTask= thumb_file_path.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    String thumb_downloadUri= task.getResult().getDownloadUrl().toString();
                                    if(task.isSuccessful())
                                    {
                                        Map update_Hashmap= new HashMap();
                                        update_Hashmap.put("image",download_uri);
                                         update_Hashmap.put("thumbnail",thumb_downloadUri);
                                        databaseReference.updateChildren(update_Hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Uploading Successful", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(),"Error in uploading",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                            databaseReference.child("image").setValue(download_uri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Profile Picture is uploaded",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Toast.makeText(getApplicationContext(),"working",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"problem",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
