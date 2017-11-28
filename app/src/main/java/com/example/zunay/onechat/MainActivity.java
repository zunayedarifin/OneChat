package com.example.zunay.onechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mtool;
    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mtool = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mtool);
        getSupportActionBar().setTitle("One Chat");
        viewPager= findViewById(R.id.main_tab_pager);//creates pages or
        sectionsPagerAdapter= new SectionsPagerAdapter(getSupportFragmentManager());//creates sections for the element to view viewpager
        viewPager.setAdapter(sectionsPagerAdapter);//show content on each fragments
        tabLayout= findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(viewPager);//Initiating viewpager to tablayout


    }
    @Override
    public void onStart(){
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null)
        {
            sendToStart();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Authentication Success",Toast.LENGTH_SHORT).show();
        }
    }

    private void sendToStart() {
        Intent startIntent= new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId()==R.id.main_logout_button)
        {
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        if (item.getItemId()==R.id.account_settings)
        {
            Intent intent= new Intent(MainActivity.this,ActivitySettings.class);
            startActivity(intent);
        }
        if (item.getItemId()==R.id.all_users)
        {
            Intent intent= new Intent(MainActivity.this,UsersActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
