package com.example.realtimechatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.realtimechatting.Fragments.ChatsFragment;
import com.example.realtimechatting.Fragments.UsersFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference databaseReference;
    Toolbar toolbar;
    ArrayList<Chats_item> items;
    ArrayList<User> users;
    RecyclerView recyclerView;
    TabLayout tabLayout;
    ViewPager viewPager;
    CircleImageView profileImage;
    TextView usernameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initViews();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        items = new ArrayList<>();
        users = new ArrayList<>();

/*        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Iterator<QueryDocumentSnapshot> iterator = queryDocumentSnapshots.iterator();
                        while (iterator.hasNext()) {

                            User user = iterator.next().toObject(User.class);
                            users.add(user);

                        }

                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        addToRecycler();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });*/
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                usernameTv.setText(user.getUsername());
                if (user.getImageURL().equals("Default")){
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Picasso.get().load(user.getImageURL()).fit().centerCrop().placeholder(R.mipmap.ic_launcher).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ChatsFragment(), "Chats");
        adapter.addFragment(new UsersFragment(), "Users");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void initViews(){
//        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        profileImage = findViewById(R.id.profile_image);
        usernameTv = findViewById(R.id.username_tv);
    }


/*
    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            Intent intent = new Intent(ChatsActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(UserActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }

    private void addToRecycler(){
        UserAdapter adapter = new UserAdapter(UserActivity.this,users);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager lm = new LinearLayoutManager(UserActivity.this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);

        DividerItemDecoration d = new DividerItemDecoration(UserActivity.this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(d);
    }

    /*
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot d: dataSnapshot.getChildren()) {
//                        User user = d.getValue(User.class);
                        //users.add(user);
                        */
/*User user = new User();

                        user.setUsername(d.child(firebaseUser.getUid()).getValue(User.class).getUsername());
                        user.setUserId(d.child(firebaseUser.getUid()).getValue(User.class).getUserId());
                        user.setImageURL(d.child(firebaseUser.getUid()).getValue(User.class).getImageURL());

                        ArrayList<String> userInfo = new ArrayList<>();
                        userInfo.add(user.getUsername());
                        userInfo.add(user.getUserId());
                        userInfo.add(user.getImageURL());
*//*


     */
/*users.clear();
                        ArrayList<String> userInfo = new ArrayList<>();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            userInfo.add(d.getKey());
                            User user = d.getValue(User.class);
                            users.add(user);
                        }*//*



//                    }


                */
/*users.username.setText(user.getUsername());
                if (user.getImageURL().equals("Default")){
                    holder.profileImage.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Picasso.get().load(user.getImageURL()).fit().into(holder.profileImage);
                }*//*


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
*/



}
