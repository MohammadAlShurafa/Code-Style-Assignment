package com.example.realtimechatting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessagesActivity extends AppCompatActivity {
    EditText messageEd;
    ImageView photosIm,voiceIm,cameraIm,smileIm;
    TextView sendBtn;
    Toolbar toolbar;
    Intent intent;
    CircleImageView circleImageView;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String userId;
    MessageAdapter adapter;
    ArrayList<Message> messages;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messages);

        initViews();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        userId = intent.getStringExtra("userId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("Default")){
                    circleImageView.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Picasso.get().load(user.getImageURL()).fit().centerCrop().placeholder(R.mipmap.ic_launcher).into(circleImageView);
                }

                readMessage(firebaseUser.getUid(), userId, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatMessagesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();

        DatabasePagingOptions<User> options = new DatabasePagingOptions.Builder<User>()
                .setLifecycleOwner(this)
                .setQuery(databaseReference, config, User.class)
                .build();


        initItems();
        initClicks();

        editText();

    }

    private void initClicks() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnimation(sendBtn);
                /*Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
                sendBtn.startAnimation(animation);*/
//                Toast.makeText(ChatMessagesActivity.this, messageEd.getText().toString(), Toast.LENGTH_SHORT).show();
                String msg=  messageEd.getText().toString();
                if (!msg.equals("")){
                    sendMessages(firebaseUser.getUid(), userId,msg);
                }else{
                    Toast.makeText(ChatMessagesActivity.this, "Message is Empty!", Toast.LENGTH_SHORT).show();
                }
                messageEd.setText("");

            }
        });

        cameraIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnimation(cameraIm);
                /*Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
                cameraIm.startAnimation(animation);*/

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        photosIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnimation(photosIm);
                /*Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
                photosIm.startAnimation(animation);*/

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                startActivityForResult(intent, 0);
            }
        });




    }

    private void initItems() {
        sendBtn.setVisibility(View.GONE);
        smileIm.setVisibility(View.GONE);
    }

    private void initViews() {
        photosIm = findViewById(R.id.photosIm);
        voiceIm = findViewById(R.id.voiceIm);
        cameraIm = findViewById(R.id.cameraIm);
        smileIm = findViewById(R.id.smileIm);
        sendBtn = findViewById(R.id.sendBtn);
        messageEd = findViewById(R.id.msgEd);
        toolbar = findViewById(R.id.toolbar);
        circleImageView = findViewById(R.id.profile_image);
        username = findViewById(R.id.username_tv);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void sendMessages(String sender, String receiver, String message){

        databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message", message);

        databaseReference.child("Chats").push().setValue(hashMap);

    }

    private void readMessage(final String myId, final String userId, final String imgURL){
        messages = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        adapter = new MessageAdapter(ChatMessagesActivity.this,messages,imgURL);
        recyclerView.setAdapter(adapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                if (message.getReceiver().equals(myId) && message.getSender().equals(userId) ||
                        message.getReceiver().equals(userId) && message.getSender().equals(myId) ){
                    messages.add(message);
                    adapter.notifyItemInserted(messages.size());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

/*        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Message message = d.getValue(Message.class);
                    if (message.getReceiver().equals(myId) && message.getSender().equals(userId) ||
                            message.getReceiver().equals(userId) && message.getSender().equals(myId) ){
                        messages.add(message);
                    }

                    adapter = new MessageAdapter(ChatMessagesActivity.this,messages,imgURL);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    private void editText(){
        messageEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() == 0){
                    photosIm.setVisibility(View.VISIBLE);
                    voiceIm.setVisibility(View.VISIBLE);
                    cameraIm.setVisibility(View.VISIBLE);
                    sendBtn.setVisibility(View.GONE);
                    smileIm.setVisibility(View.GONE);
                }else{
                    photosIm.setVisibility(View.GONE);
                    voiceIm.setVisibility(View.GONE);
                    cameraIm.setVisibility(View.GONE);
                    sendBtn.setVisibility(View.VISIBLE);
                    smileIm.setVisibility(View.VISIBLE);

                    /*Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.sample_anim);
                    sendBtn.startAnimation(animation);
                    smileIm.startAnimation(animation);*/
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setAnimation(View view){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
        view.startAnimation(animation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ChatMessagesActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }
}
