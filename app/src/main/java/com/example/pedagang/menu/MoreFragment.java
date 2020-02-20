package com.example.pedagang.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.pedagang.Login;
import com.example.pedagang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MoreFragment extends Fragment {

    ListView lv;
    ListAdapter adapter;
    SimpleAdapter Adapter;
    HashMap<String, String> map;
    ArrayList<HashMap<String, String>> mylist;
    String[] Pil;
    String[] Gbr;
    private TextView username, email;
    private CircleImageView img_avatar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference dbr;
    private StorageReference SR;
    private FirebaseStorage FS;
    private String id;
    private Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        username = (view.findViewById(R.id.username));
        email = (view.findViewById(R.id.email));
        img_avatar = (view.findViewById(R.id.image));

        loadUserInformation();

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv = (ListView) view.findViewById(R.id.lvl);

        Pil = new String[]{"Pesanan", "Feedback", "Edit Profile", "Logout"};
        Gbr = new String[]{Integer.toString(R.drawable.ic_notifications_black_24dp),
                Integer.toString(R.drawable.ic_feedback_black_24dp),
                Integer.toString(R.drawable.ic_settings_black_24dp),
                Integer.toString(R.drawable.ic_error_black_24dp)};

        mylist = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < Pil.length; i++) {
            map = new HashMap<String, String>();
            map.put("list", Pil[i]);
            map.put("gbr", Gbr[i]);
            mylist.add(map);
        }

        Adapter = new SimpleAdapter(getContext(), mylist, R.layout.isi_listmore,
                new String[]{"list","gbr"}, new int[]{R.id.tv_nama, R.id.imV});
        lv.setAdapter(Adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        //Notifikasi
                        Intent intentNotifikasi = new Intent(getContext(), Pesanan.class);
                        startActivity(intentNotifikasi);
                        break;
                    case 1:
                        //Feedback
                        Intent intentFeedback = new Intent(getContext(), Feedback.class);
                        startActivity(intentFeedback);
                        break;
                    case 2:
                        //Pengaturan
                        Intent intentProfile = new Intent(getContext(), EditProfile.class);
                        startActivity(intentProfile);
                        break;
                    case 3:
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(getContext(), "Anda Telah Logout", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getContext(), Login.class));
                        break;
                }
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    private void loadUserInformation(){

        if (mActivity == null) {
            return;
        }

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        id = mAuth.getUid();
        dbr = FirebaseDatabase.getInstance().getReference("Seller").child(id);
        SR = firebaseStorage.getReference();
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String user_name = dataSnapshot.child("name").getValue(String.class);
                String e_mail = dataSnapshot.child("email").getValue(String.class);

                username.setText(user_name);
                email.setText(e_mail);


                if (dataSnapshot.child("image").equals("t")){
                    Glide.with(getContext()).load(R.drawable.ic_add_a_photo_white_24dp).into(img_avatar);
                }
                else{
                    Glide.with(getContext()).load(dataSnapshot.child("avatar").getValue(String.class)).into(img_avatar);
                }
//                Glide.with(getContext()).load(dataSnapshot.child("avatar").getValue(String.class)).into(avatar_bg);

//                SR.child("img_Avatar").child(id).child("test").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        String gambar = uri.toString();
//
//                        Glide.with(getActivity()).load(gambar).into(img_avatar);
//                    }
//                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}