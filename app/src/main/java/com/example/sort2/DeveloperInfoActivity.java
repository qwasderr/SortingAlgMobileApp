package com.example.sort2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
public class DeveloperInfoActivity extends AppCompatActivity {

    private ImageView imageDeveloper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_info);

        ImageView imageDeveloper = findViewById(R.id.imageDeveloper);
        TextView tvDeveloperName = findViewById(R.id.tvDeveloperName);
        TextView tvDeveloperInfo = findViewById(R.id.tvDeveloperInfo);
        Button btnBack = findViewById(R.id.btnBack);

        tvDeveloperName.setText("Максим Марков");
        tvDeveloperInfo.setText("Студент групи ТТП-41");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}