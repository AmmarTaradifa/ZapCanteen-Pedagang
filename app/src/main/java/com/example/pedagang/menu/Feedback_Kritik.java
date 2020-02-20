package com.example.pedagang.menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pedagang.R;
import com.google.android.material.textfield.TextInputEditText;

public class Feedback_Kritik extends AppCompatActivity {

    private TextInputEditText etkritik;
    private Button UploadButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kritik);

        etkritik = (TextInputEditText) findViewById(R.id.edittext);
        UploadButton = (Button)findViewById(R.id.uploadbutton);

        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String pesan = etkritik.getText().toString();

               Intent kirimWA = new Intent(Intent.ACTION_SEND);
               kirimWA.setType("text/plain");
               kirimWA.putExtra(Intent.EXTRA_TEXT, pesan);
               kirimWA.putExtra("jid", "6281224850964" + "@s.whatsapp.net");
               kirimWA.setPackage("com.whatsapp");

               startActivity(kirimWA);
            }
        });

}
    private void openWhatsApp() {
        String url = "https://api.whatsapp.com/send?phone=" + "6281224850964";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        i.putExtra(Intent.EXTRA_TEXT, ""+etkritik.getText());
        startActivity(i);
    }
}