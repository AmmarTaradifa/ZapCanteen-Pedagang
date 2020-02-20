package com.example.pedagang.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pedagang.R;

public class Feedback extends AppCompatActivity {

    Button btnkritik, btnsaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnkritik = (Button)findViewById(R.id.btnkritik);
        btnsaran = (Button) findViewById(R.id.btnsaran);

        btnkritik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Feedback.this,Feedback_Kritik.class);
                startActivity(intent);
            }
        });
        btnsaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Feedback.this,Feedback_Saran.class);
                startActivity(intent);
            }
        });

    }
}
