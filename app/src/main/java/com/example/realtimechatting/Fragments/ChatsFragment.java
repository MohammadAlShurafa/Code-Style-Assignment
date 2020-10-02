package com.example.realtimechatting.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.realtimechatting.Message;
import com.example.realtimechatting.R;
import com.example.realtimechatting.User;
import com.example.realtimechatting.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {
    RecyclerView recyclerView;
    UserAdapter adapter;
    ArrayList<User> users;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ArrayList<String> usersList;
    ProgressBar progressBar;


    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Message message = d.getValue(Message.class);
                    if (message.getSender().equals(firebaseUser.getUid())) {
                        usersList.add(message.getReceiver());
                    }
                    if (message.getReceiver().equals(firebaseUser.getUid())) {
                        usersList.add(message.getSender());
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void readChats() {
        users = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    User user = d.getValue(User.class);

                    for (String id : usersList) {
                        if (user.getUserId().equals(id)) {
                            if (!users.contains(user)) {
                                users.add(user);
                            }
                        }
                    }
                }

                adapter = new UserAdapter(getContext(), users);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
