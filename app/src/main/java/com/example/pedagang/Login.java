package com.example.pedagang;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pedagang.model.Seller;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Login extends AppCompatActivity {

    public static final String USER_ID = "user_id";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_NAME = "user_name";
    public static final String USER_PASSWORD = "user_password";


    private Button mlogin;
    private EditText lgn_email, lgn_password;
    private FirebaseAuth mAuth;
    private DatabaseReference dbr;
    private TextView forgot_psw;
    private TextView signup;
    private StorageReference SR;
    private Button btn_google;
    public FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance();
        SR = FirebaseStorage.getInstance().getReference();

        mlogin =  findViewById(R.id.login);
        lgn_email =  findViewById(R.id.email_lgn);
        lgn_password = findViewById(R.id.password_lgn);
        forgot_psw = findViewById(R.id.forgot_password);
        signup = findViewById(R.id.sign_up_btn);


        SignInUser();
        Forgot_Password();
        SignUp();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(Login.this, BottomNavigation.class));
                }
            }
        };

    }


    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(authStateListener);
    }

    private void SignInUser() {
        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = lgn_email.getText().toString().trim();
                final String user_email = lgn_email.getText().toString().trim();
                final String user_password = lgn_password.getText().toString().trim();
                final String avatar =  Glide.with(getApplicationContext()).load(SR.child("boy")).toString();

                if (TextUtils.isEmpty(user_email)){
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(user_password)){
                    Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()){
                    Toast.makeText(getApplicationContext(), "email not valid", Toast.LENGTH_SHORT).show();
                }
                else if (user_password.length() < 4){
                    Toast.makeText(getApplicationContext(), "Password is too short, enter password minimum 4", Toast.LENGTH_SHORT).show();
                }

                mAuth.signInWithEmailAndPassword(user_email, user_password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()){
                                    Toast.makeText(Login.this, "Sign In failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    String id = mAuth.getUid();
                                    Seller user = new Seller(id,username,user_email,user_password, avatar);

                                    //passing data from activity to fragment

                                    Bundle bundle = new Bundle();
                                    bundle.putString(USER_ID, user.getId());

                                    startActivity(new Intent(Login.this, BottomNavigation.class).putExtras(bundle));
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    private void Forgot_Password() {
        forgot_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
            }
        });
    }

    private void SignUp() {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }

    private void signInGoogle(){
    }
}




