package com.example.pedagang;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pedagang.model.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class DetailMakanan extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    public String id_makanan, name ,price, seller ,image, mkey,test;
    private Food foods;
    private ImageView gambarBarang;
    private EditText edtnama, edtharga, edtseller;
    private DatabaseReference dbr;
    private StorageReference SR;
    private FirebaseAuth mAuth;
    private Button btn_edit;
    private ProgressBar mProgressBar;
    private RelativeLayout mAddGambar;

    private Uri mGambarUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_makanan);

        id_makanan = getIntent().getStringExtra("id_makanan");
        name = getIntent().getStringExtra("name");
        price = getIntent().getStringExtra("price");
        seller = getIntent().getStringExtra("seller");
        image = getIntent().getStringExtra("image");

        foods = new Food();

        component();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String id = currentUser.getUid();
        SR = FirebaseStorage.getInstance().getReference("Img_Barang");
        dbr = FirebaseDatabase.getInstance().getReference("Food").child(id);


        edtnama.setText(name);
        edtharga.setText(price);
        edtseller.setText(seller);
        Glide.with(getApplicationContext()).load(image).into(gambarBarang);


        editData();

        // Buat memasukan gambar
        mAddGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });


    }

    public void editData(){
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = foods.setName(edtnama.getText().toString().trim());
                final String price = foods.setprice(edtharga.getText().toString().trim());
                final String seller = foods.setSeller(edtseller.getText().toString().trim());
                final String gambar = foods.setimage(image);

                if (mGambarUri != null){
                    final StorageReference fileReference = SR.child(System.currentTimeMillis() + "."+ getFileExtension(mGambarUri));
                    fileReference.putFile(mGambarUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mProgressBar.setProgress(0);
                                        }
                                    }, 4000);


                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
//                                            String gambar = uri.toString();

                                            String id = id_makanan;
                                            Food food = new Food(id, name, price, seller, gambar);
                                            dbr.child(id).setValue(food);

                                        }
                                    });

                                    startActivity(new Intent(DetailMakanan.this, BottomNavigation.class));
                                }
                            })
                            //----------------------------------------------------------------------------------

                            // kalau input gagal
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            //----------------------------------------------------------------------------------

                            // Input sedang dalam tahap proses
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    mProgressBar.setProgress((int) progress);
                                }
                            });
                }
                else {
                    String id = id_makanan;
                    Food food = new Food(id, name, price,seller, gambar);
                    dbr.child("id").setValue(food).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 4000);

                            Toast.makeText(DetailMakanan.this, "Edit berhasil", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DetailMakanan.this, BottomNavigation.class));
                        }
                    });
                }

//
//
            }
        });
    }

    private void component(){
        edtnama = findViewById(R.id.sbtnama_barang);
        edtharga = findViewById(R.id.sbtharga);
        edtseller = findViewById(R.id.sbtseller);
        mAddGambar = findViewById(R.id.layout_addGambar);
        gambarBarang = findViewById(R.id.img_barang);
        mProgressBar = findViewById(R.id.progress_bar);
        btn_edit = findViewById(R.id.btn_edt);
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    // Buat ngambil gambar
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mGambarUri = data.getData();
            gambarBarang.setImageURI(mGambarUri);
        }
    }

    // extension buat gambar
    private String getFileExtension(Uri uri){
        ContentResolver cR = getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}

