package com.example.pedagang.menu;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pedagang.DetailMakanan;
import com.example.pedagang.R;
import com.example.pedagang.model.Food;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.List;

//public class ListMakanan extends Fragment implements FoodAdapter.OnFoodClickListener {
//
public class ListMakanan extends Fragment implements FoodAdapter.OnItemClickListener {

    //
    private RecyclerView mDataList;
    private FoodAdapter mAdapter;
    private FirebaseStorage mStorage;
    private DatabaseReference dbr;
    private ValueEventListener mDBListener;
    private List<Food> mFoods;
    Food foods;
    private ProgressBar mprogressCircle;
    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.list_food, container, false);
        mDataList = (view.findViewById(R.id.data_barang));
        mDataList.setHasFixedSize(true);
        mprogressCircle = (view.findViewById(R.id.progress_circle));
        mDataList.setLayoutManager(new LinearLayoutManager(getContext()));



        mFoods = new ArrayList<>();

        mAdapter = new FoodAdapter(getContext(), mFoods);

        mDataList.setAdapter(mAdapter);

        mAdapter.setOnFoodClickListener(ListMakanan.this);



        mStorage = FirebaseStorage.getInstance();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String id = currentUser.getUid();
        dbr = FirebaseDatabase.getInstance().getReference("Food").child(id);



        mDBListener = dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mFoods.clear();

                for (DataSnapshot FoodSnapshot : dataSnapshot.getChildren()) {
                    Food Food = FoodSnapshot.getValue(Food.class);
                    Food.setkey(FoodSnapshot.getKey());
                    mFoods.add(Food);
                }

                mAdapter.notifyDataSetChanged();
                mprogressCircle.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mprogressCircle.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }


    public void onDetailClick(int position) {
        Toast.makeText(getContext(), "Detail Click at position : " + position, Toast.LENGTH_SHORT).show();
    }


    public void onDeleteClick(int position) {
        Food selectedFood = mFoods.get(position);
        final String selectedKey = selectedFood.getkey();

        StorageReference dataRef = mStorage.getReferenceFromUrl(selectedFood.getimage());
        dataRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dbr.child(selectedKey).removeValue();
                Toast.makeText(getContext(), "Food Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbr.removeEventListener(mDBListener);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onEditClick(Food food) {
        Intent i = new Intent(getActivity(), DetailMakanan.class);
        i.putExtra("name", food.getName());
        i.putExtra("price", food.getprice());
        i.putExtra("seller", food.getSeller());
        i.putExtra("image", food.getimage());

        startActivity(i);
    }


}

