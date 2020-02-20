package com.example.pedagang;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pedagang.model.Seller;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Register extends AppCompatActivity {
    private EditText input_username, input_email, input_password;
    private FirebaseAuth mAuth;
    private Button msignup;
    private StorageReference SR;
    private TextView signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialize Firebase Auth / Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        SR = FirebaseStorage.getInstance().getReference().child("gambar");

        msignup = (Button) findViewById(R.id.sign_up_btn);
        input_username = (EditText) findViewById(R.id.name_sgnup);
        input_email = (EditText) findViewById(R.id.email_sgnup);
        input_password = (EditText) findViewById(R.id.password_sgnup);
        signin = (TextView) findViewById(R.id.sign_in_btn);

        SignUp();
        SignIn();



    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            //handle the already login user
        }
    }

    private void SignUp(){
        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = input_username.getText().toString().trim();
                final String user_email = input_email.getText().toString().trim();
                final String user_password = input_password.getText().toString().trim();
                final String user_avatar = "";

                // Pesan kalau email kosong
                if (TextUtils.isEmpty(user_email)){
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Pesan kalau username kosong
                else if(TextUtils.isEmpty(username)){
                    Toast.makeText(getApplicationContext(), "Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Pesan kalau username lebih kecil dari 5
                else if (username.length() < 5){
                    Toast.makeText(getApplicationContext(), "Username minimum 5", Toast.LENGTH_SHORT).show();
                }
                // Pesan kalau password kosong
                else if (TextUtils.isEmpty(user_password)){
                    Toast.makeText(getApplicationContext(), "Enter your password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Pesan kalau password lebih kecil dari 4
                else if (user_password.length() < 4){
                    Toast.makeText(getApplicationContext(), "Password is too short, enter password minimum 4", Toast.LENGTH_SHORT).show();
                }

                mAuth.createUserWithEmailAndPassword( user_email, user_password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()){
                                    // Message Sign Up gagal
                                    Toast.makeText(Register.this, "Sign Up failed " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    // membuat table User ke Firebase real time db
                                    String id = mAuth.getUid();

                                    Seller seller = new Seller(id,username,user_email,user_password, user_avatar);

                                    Intent intent = new Intent(getApplicationContext(), BottomNavigation.class);
                                    intent.putExtra(id,seller.getId());
                                    intent.putExtra(username,seller.getName());
                                    intent.putExtra(user_email,seller.getEmail());
                                    intent.putExtra(user_password,seller.getPassword());
                                    intent.putExtra(user_avatar,seller.getAvatar());


                                    FirebaseDatabase.getInstance().getReference("Seller")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(seller).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                //display message sign up berhasil

                                                Toast.makeText(Register.this, "Sign Up success", Toast.LENGTH_SHORT).show();
                                            }
                                            else{

                                            }
                                        }
                                    });
                                    startActivity(new Intent(Register.this, Login.class));
                                }
                            }
                        });
            }
        });
    }

    private void SignIn() {
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }
}
