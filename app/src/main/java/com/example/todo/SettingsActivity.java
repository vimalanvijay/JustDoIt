package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        TextView temp=findViewById(R.id.tempPanel);
        temp.setText("Customisation, themes, control over time panel & progress panel and more...");

        Toast.makeText(this,"Coming Soon!",Toast.LENGTH_LONG).show();
    }
}
