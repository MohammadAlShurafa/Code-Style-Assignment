package com.example.realtimechatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    TextView loginTv;
    EditText usernameEd, emailEd, passwordEd, confPasswordEd;
    Button registerBtn;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference databaseReference;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initItems();
        initClicks();

    }

    private void initClicks() {
        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEd.getText().toString();
                String email = emailEd.getText().toString();
                String password = passwordEd.getText().toString();
                String confPassword = confPasswordEd.getText().toString();

                /*if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    register(username, email, password, confPassword);
                } else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 character", Toast.LENGTH_SHORT).show();
                } else if (confPassword.equals(password)) {
                    Toast.makeText(RegisterActivity.this, "password doesn't compatible", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }*/
                if (username.isEmpty() && email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }else if (!confPassword.equals(password)){
                    Toast.makeText(RegisterActivity.this, "password doesn't compatible", Toast.LENGTH_SHORT).show();
                }else {
                    register(username, email, password, confPassword);
                }
            }
        });

    }

    private void initItems() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        setSupportActionBar(toolbar);
    }

    private void initViews() {
        loginTv = findViewById(R.id.loginTv);
        usernameEd = findViewById(R.id.usernameEd);
        emailEd = findViewById(R.id.emailEd);
        passwordEd = findViewById(R.id.passwordEd);
        confPasswordEd = findViewById(R.id.confPasswordEd);
        registerBtn = findViewById(R.id.loginBtn);
        toolbar = findViewById(R.id.toolbar);
    }

    private void register(final String username, String email, String password, String confPassword) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /*FirebaseUser user = firebaseAuth.getCurrentUser(); // to Firestore

                            Map<String, String> hashMap = new HashMap<>();
                            hashMap.put("userId", userId);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "Default");
                            firebaseFirestore.collection("users").add(hashMap);
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();*/

                            String userId = firebaseAuth.getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("userId", userId);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "Default");

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        } else {
                            Toast.makeText(RegisterActivity.this, "You can't register", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
