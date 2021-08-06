package com.example.firebasechatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private static final String TAG = "SingInActivity";

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText repetpasswordEditText;

    private TextView toggleLoginSignUpButton;
    private Button loginSingUpButton;

    private boolean loginModeActive;

    private FirebaseDatabase database;
    private DatabaseReference usersDatabaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialisations();

        listeners();

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(SignInActivity.this, UserListActivity.class));
        }

    }

    void initialisations(){
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        repetpasswordEditText = findViewById(R.id.repeatpasswordEditText);

        toggleLoginSignUpButton = findViewById(R.id.toggleLoginSignUpButton);
        loginSingUpButton = findViewById(R.id.loginSingUpButton);

        database = FirebaseDatabase.getInstance();
        usersDatabaseReference = database.getReference().child("users");
    }

    void listeners(){
        loginSingUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginSignUpUser(emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim());
            }
        });
    }

    void loginSignUpUser(String email, String password){

        if(loginModeActive){
            if(passwordEditText.getText().toString().trim().length() < 7){
                Toast.makeText(this,"Passwords must be at least 7 characters!", Toast.LENGTH_SHORT).show();
            }
            else if(emailEditText.getText().toString().trim().equals("")){
                Toast.makeText(this,"Please, input your email", Toast.LENGTH_SHORT).show();
            }
            else if(nameEditText.getText().toString().trim().length() < 7){
                Toast.makeText(this,"Please, input your name", Toast.LENGTH_SHORT).show();
            }else{
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                Intent intent = new Intent(SignInActivity.this, UserListActivity.class);
                                intent.putExtra("userName", nameEditText.getText().toString().trim());
                                startActivity(intent);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
            }
        }
        else {
            if(!passwordEditText.getText().toString().trim().equals(repetpasswordEditText.getText().toString().trim()))
            {
                Toast.makeText(this,"Passwords don't match", Toast.LENGTH_SHORT).show();
            }
            else if(passwordEditText.getText().toString().trim().length() < 7){
                Toast.makeText(this,"Passwords must be at least 7 characters!", Toast.LENGTH_SHORT).show();
            }
            else if(emailEditText.getText().toString().trim().equals("")){
                Toast.makeText(this,"Please, input your email", Toast.LENGTH_SHORT).show();
            }
            else if(nameEditText.getText().toString().trim().length() < 7){
                Toast.makeText(this,"Please, input your name", Toast.LENGTH_SHORT).show();
            }
            else{
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                createUser(user);
                                Intent intent = new Intent(SignInActivity.this, UserListActivity.class);
                                intent.putExtra("userName", nameEditText.getText().toString().trim());
                                startActivity(intent);
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
            }
        }
    }

    private void createUser(FirebaseUser fb_user) {
        User user = new User();
        user.setUserID(fb_user.getUid());
        user.setEmail(fb_user.getEmail());
        user.setName(nameEditText.getText().toString().trim());

        usersDatabaseReference.push().setValue(user);

    }

    public void toggleLoginMode(View view) {
        if(loginModeActive){
            loginModeActive = false;
            loginSingUpButton.setText(R.string.signup);
            toggleLoginSignUpButton.setText(R.string.orlogin);
            repetpasswordEditText.setVisibility(View.VISIBLE);
        }else{
            loginModeActive = true;
            loginSingUpButton.setText(R.string.log_in);
            toggleLoginSignUpButton.setText(R.string.orsignup);
            repetpasswordEditText.setVisibility(View.GONE);

        }
    }
}