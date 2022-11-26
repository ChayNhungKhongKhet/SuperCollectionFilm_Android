package com.phamtantb24.superfilmcollection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    EditText userName, password;
    Button loginBtn;
    TextView newUser, forgetPass;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mapping();
        myRef = FirebaseDatabase.getInstance().getReference();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userName.getText().toString().trim();
                String pass = password.getText().toString().trim();
                myRef.child("Accounts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(user)) {// Check user has exist on firebase ?
                            // if exist get password
                            String password = snapshot.child(user).child("password").getValue(String.class);
                            assert password != null;
                            if (password.equals(pass)) {
                                Toast.makeText(getApplicationContext(),"Login succeed",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                finish();
                            }
                            else
                                Toast.makeText(getApplicationContext(),"Password was wrong !",Toast.LENGTH_LONG).show();

                        }else
                            Toast.makeText(getApplicationContext(),"User name was wrong !",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FIREBASE",error.toString());
                    }
                });
            }
        });
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    private void mapping() {
        userName = findViewById(R.id.userNameEditTxt);
        password = findViewById(R.id.passwordEditTxt);
        loginBtn = findViewById(R.id.loginBtn);
        newUser = findViewById(R.id.newUser);
        forgetPass= findViewById(R.id.forgetPass);
    }
}