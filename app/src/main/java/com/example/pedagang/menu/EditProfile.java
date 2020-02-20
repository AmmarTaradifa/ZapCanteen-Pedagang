package com.example.pedagang.menu;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pedagang.BottomNavigation;
import com.example.pedagang.R;
import com.example.pedagang.model.Seller;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    private EditText edtNama, edtEmail, edtPassword;
    private Button btn_avatar, mSubmit;
    private DatabaseReference dbr;
    private FirebaseStorage FS;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseUser currentUser;

    private static int PICK_IMAGE = 123;
    private Uri mGambarUri;
    private CircleImageView mAvatar;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();
        id = mAuth.getUid();
        dbr = FirebaseDatabase.getInstance().getReference("Seller");

        component();
        loadUserInformation();

        btn_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFileChooser();
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtProfile();
                sendAvatar();
            }
        });
    }

    private void component(){

        edtNama = findViewById(R.id.edtnama);
        edtEmail = findViewById(R.id.edtemail);
        edtPassword = findViewById(R.id.edtpass);
        mSubmit = findViewById(R.id.submit_edt);
        btn_avatar = findViewById(R.id.btn_img);
        mAvatar = findViewById(R.id.img_avatar);


    }

    private void loadUserInformation(){
        dbr.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String user_name = dataSnapshot.child("name").getValue(String.class);
                edtNama.setText(user_name);

                if (currentUser != null){
                    if (currentUser.getEmail() != null){
                        edtEmail.setText(currentUser.getEmail());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void edtProfile(){


        final StorageReference imgReference = storageReference.child("image").child(id).child("test");
        UploadTask uploadTask = imgReference.putFile(mGambarUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                id = currentUser.getUid();
                final String email = edtEmail.getText().toString().trim();
                final String password = edtPassword.getText().toString().trim();

                imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String name = edtNama.getText().toString().trim();
                        String avatar = uri.toString();

                        Seller seller = new Seller(id, name, email, password, avatar);
                        dbr.child(id).setValue(seller).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                currentUser.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });

                                currentUser.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                            }
                        });
                        startActivity(new Intent(EditProfile.this, BottomNavigation.class));
                    }
                });



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            mGambarUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mGambarUri);
                mAvatar.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }


    }

    private void sendAvatar(){

    }

    private void OpenFileChooser(){
        Intent profileIntent = new Intent();
        profileIntent.setType("image/*");
        profileIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(profileIntent, "Select Image."), PICK_IMAGE);

    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
