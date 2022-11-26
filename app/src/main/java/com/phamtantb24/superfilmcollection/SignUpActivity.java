package com.phamtantb24.superfilmcollection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phamtantb24.superfilmcollection.databinding.ActivitySignUpBinding;
import com.phamtantb24.superfilmcollection.model.Account;

public class SignUpActivity extends AppCompatActivity {

    Button createUser;
    ImageView backLogin;
    EditText userNameEditTxt,passwordEditTxt,locationEditTxt,emailEditTxt,phoneEditTxt;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mapping();
        myRef = FirebaseDatabase.getInstance().getReference();

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                finish();
                startActivity(intent);
            }
        });

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEditTxt.getText().toString().trim();
                String password = passwordEditTxt.getText().toString().trim();
                String location = locationEditTxt.getText().toString().trim();
                String email = emailEditTxt.getText().toString().trim();
                String phone = phoneEditTxt.getText().toString().trim();


                if (userName.isEmpty() || password.isEmpty() || location.isEmpty() || email.isEmpty() || phone.isEmpty())
                    //check fill
                    Toast.makeText(getApplicationContext(),"Please fill all fields !",Toast.LENGTH_LONG).show();
                myRef.child("Accounts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(userName)) // check username is exist
                            Toast.makeText(getApplicationContext(),"User name is exist !",Toast.LENGTH_LONG).show();
                        else {
                            Account account = new Account(userName,password,email,location,phone);
                            myRef.child("Accounts").child(userName).setValue(account)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isComplete()) {
                                                android.app.AlertDialog.Builder builder1 = new AlertDialog.Builder(SignUpActivity.this);
                                                builder1.setMessage("Create user succeed, You want back to login ?");

                                                builder1.setPositiveButton(
                                                        "Yes",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                                startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                                                                finish();
                                                            }
                                                        });

                                                builder1.setNegativeButton(
                                                        "No",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                userNameEditTxt.setText("");
                                                                passwordEditTxt.setText("");
                                                                locationEditTxt.setText("");
                                                                phoneEditTxt.setText("");
                                                                emailEditTxt.setText("");
                                                                dialog.cancel();
                                                            }
                                                        }).show();
                                            }

                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FIREBASE",error.toString());
                    }
                });
            }
        });


    }
    public void mapping() {
        createUser = findViewById(R.id.createUser);
        passwordEditTxt = findViewById(R.id.passwordEditTxt);
        userNameEditTxt = findViewById(R.id.userNameEditTxt);
        phoneEditTxt = findViewById(R.id.phoneEditTxt);
        emailEditTxt = findViewById(R.id.emailEditTxt);
        backLogin = findViewById(R.id.backLogin);
        locationEditTxt = findViewById(R.id.locationEditTxt);

    }

}