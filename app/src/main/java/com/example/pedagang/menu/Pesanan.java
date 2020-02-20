package com.example.pedagang.menu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pedagang.R;

public class Pesanan extends AppCompatActivity {

    public static final int NOTIF_REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan);
    }
}
