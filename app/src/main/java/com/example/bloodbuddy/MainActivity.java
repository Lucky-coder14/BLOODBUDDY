package com.example.bloodbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bloodbuddy.Adapter.UserAdapter;
import com.example.bloodbuddy.Model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
   private DrawerLayout drawerLayout;
   private Toolbar toolbar;
   private NavigationView nav_View;
   private CircleImageView nav_profile_image;

   private TextView nav_fullname,nav_email,nav_bloodgroup,nav_type;

   private DatabaseReference userRef;



   private RecyclerView recyclerview;
   private ProgressBar progressBar;

   private List<User> userList;
   private UserAdapter userAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Blood Donation App");
        drawerLayout = findViewById(R.id.drawerLayout);
        nav_View = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav_View.setNavigationItemSelectedListener(this);

        progressBar = findViewById(R.id.progressbar);
        recyclerview = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerview.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(MainActivity.this,userList);

        recyclerview.setAdapter(userAdapter);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid());

       ref.addValueEventListener(new ValueEventListener(){
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String type = snapshot.child("type").getValue().toString();
               if(type.equals("donor")){
                   readReceipients();
               }
               else{
                   readDonors();
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });








        nav_profile_image= nav_View.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_fullname= nav_View.getHeaderView(0).findViewById(R.id.nav_user_fullname);
        nav_email= nav_View.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_bloodgroup= nav_View.getHeaderView(0).findViewById(R.id.nav_user_bloodgroup);
        nav_type= nav_View.getHeaderView(0).findViewById(R.id.nav_user_type);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()) {
                   String name = snapshot.child("name").getValue().toString();
                   nav_fullname.setText(name);

                   String email = snapshot.child("email").getValue().toString();
                   nav_email.setText(email);

                   String bloodgroup = snapshot.child("bloodgroup").getValue().toString();
                   nav_bloodgroup.setText(bloodgroup);

                   String type = snapshot.child("type").getValue().toString();
                   nav_type.setText(type);
                  if(snapshot.hasChild("profilepictureurl")) {
                      String imageUrl = snapshot.child("profilepictureurl").getValue().toString();
                      Glide.with(getApplicationContext()).load(imageUrl).into(nav_profile_image);
                  }else{
                      nav_profile_image.setImageResource(R.drawable.profile_image);
                  }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







    }

    private void readDonors() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = reference.orderByChild("type").equalTo("donor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user= dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);


                if(userList.isEmpty()){
                    Toast.makeText(MainActivity.this,"No Donors",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void readReceipients() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = reference.orderByChild("type").equalTo("recipient");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user= dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);


                if(userList.isEmpty()){
                    Toast.makeText(MainActivity.this,"No Receipients",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent iintent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(iintent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}