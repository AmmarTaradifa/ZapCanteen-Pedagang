package com.example.pedagang.menu;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pedagang.R;
import com.example.pedagang.model.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class AddFood extends Fragment {

    public AddFood() {
        //Required empty public constructor
    }

    //merefers ke firebase Realtime db
    private static final int PICK_IMAGE_REQUEST = 1;

    private RelativeLayout mAddGambar;
    private ImageView gambar_Barang;


    private Uri mGambarUri;

    private DatabaseReference dbr;
    private StorageReference SR, fileReference;
    private FirebaseAuth mAuth;
    private EditText nama_food, add_seller, harga_food;
    //    private TextView tgl;
    private Button btnAdd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.tambah_makanan, container, false);
        nama_food = (view.findViewById(R.id.addnama_produk));
        harga_food = (view.findViewById(R.id.add_harga));
        add_seller = (view.findViewById(R.id.add_seller));
        btnAdd = (view.findViewById(R.id.btn_add));
        mAddGambar = (view.findViewById(R.id.layout_addGambar));
        gambar_Barang = (view.findViewById(R.id.img_barang));




        // Buat memasukan gambar
        mAddGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        // Yang SR untuk membuat folder Img_Barang, sedangkan dbr memasukan data ke table food yang disesuaikan dengan id usernya
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String id = currentUser.getUid();
        SR = FirebaseStorage.getInstance().getReference("Img_Barang");
        dbr = FirebaseDatabase.getInstance().getReference("Food").child(id);

        //----------------------------------------------------------------------------------

        Add_food();
        return view;
    }

    public void Add_food() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = nama_food.getText().toString().trim();
                final String seller = add_seller.getText().toString().trim();
                final String price = harga_food.getText().toString().trim();


                if (mGambarUri != null){
                    // membuat extensinya di dalam Firebase Storage
                    fileReference = SR.child(System.currentTimeMillis() + "."+ getFileExtension(mGambarUri));
                    fileReference.putFile(mGambarUri)
                            //----------------------------------------------------------------------------------
                            // Input berhasil
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                        }
                                    }, 4000);

                                    Toast.makeText(getContext(), "Berhasil Menambah Makanan", Toast.LENGTH_SHORT).show();
                                    final String id = dbr.push().getKey();



                                    // url gambar
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String gambar = uri.toString();
                                            final Food food = new Food(id, name, seller, price, gambar );
                                            dbr.child(id).setValue(food);
                                        }
                                    });


                                }

                            })
                            //----------------------------------------------------------------------------------

                            // kalau input gagal
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    //----------------------------------------------------------------------------------


                    //----------------------------------------------------------------------------------
                }

//
            }
        });
    }








    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    //----------------------------------------------------------------------------------

    // Buat ngambil gambar
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mGambarUri = data.getData();
            gambar_Barang.setImageURI(mGambarUri);
        }
    }
    //----------------------------------------------------------------------------------

    // extension buat gambar
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


}
