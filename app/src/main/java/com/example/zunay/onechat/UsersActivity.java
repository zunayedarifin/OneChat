package com.example.zunay.onechat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        toolbar=findViewById(R.id.user_app_bar);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=findViewById(R.id.user_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Users,UserViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Users, UserViewHolder>
                (Users.class,R.layout.users_single_layout,UserViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, Users model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setThumbImage(model.getImage(),getApplicationContext());

                final String user_id=getRef(position).getKey();
                //final String user_name=getRef(position).getDatabase().getReference().

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(UsersActivity.this,ProfileActivity.class);
                        intent.putExtra("user_id",user_id);
                        //intent.putExtra("")
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        View view;
        public UserViewHolder(View itemView) {
            super(itemView);
            view=itemView;
        }
        public void setName(String name)
        {
            TextView userNameView=view.findViewById(R.id.textView_user_single_name);
            userNameView.setText(name);
        }
        public void setStatus(String status)
        {
            TextView statusView=view.findViewById(R.id.textView_user_single_status);
            statusView.setText(status);
        }

        public void setThumbImage(String thumbImage, Context context) {
            CircleImageView circleImageView=view.findViewById(R.id.circleImageView);
            Picasso.with(context).load(thumbImage).placeholder(R.drawable.ic_action_name).into(circleImageView);
        }
    }
}
