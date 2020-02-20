package com.example.pedagang.menu;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.pedagang.Login;
import com.example.pedagang.R;
import com.example.pedagang.Register;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends Fragment {

    private TextView username;
    private DatabaseReference dbr;
    private FirebaseAuth mAuth;
    private String id;
    private FirebaseUser currentUser;
    private CardView addProduk, listFood, feedback, profile;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.home_item, container, false);
        View view = inflater.inflate(R.layout.home_item, container, false);
        username = (view.findViewById(R.id.name));
        addProduk = (view.findViewById(R.id.add_item));
        listFood = (view.findViewById(R.id.listItem));
        feedback = (view.findViewById(R.id.feedback));
        profile = (view.findViewById(R.id.profile));


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        id = currentUser.getUid();


        loadUserInformation();
        editprofile();
        logout();
        feedback();
        pesanan();

        return view;
    }


    private void loadUserInformation() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String id = mAuth.getUid();
        dbr = FirebaseDatabase.getInstance().getReference("Seller").child(id);
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String user_name = dataSnapshot.child("name").getValue(String.class);
                username.setText(user_name);

//                if (dataSnapshot.exists()){
//                    countItems = (int) dataSnapshot.getChildrenCount();
//                    username.setText(Integer.toString(countItems) + "Items");
//                }
//                else{
//                    username.setText("0 Items");
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void pesanan() {
        addProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add =new Intent(getActivity(),Pesanan.class);
                startActivity(add);
            }
        });
    }

    private void editprofile() {
        listFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent list =new Intent(getActivity(),EditProfile.class);
                startActivity(list);
            }
        });
    }

    private void feedback() {
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent feed =new Intent(getActivity(),Feedback.class);
                startActivity(feed);
            }
        });
    }

    private void logout() {
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Anda Telah Logout", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), Login.class));
            }
        });
    }

    }

