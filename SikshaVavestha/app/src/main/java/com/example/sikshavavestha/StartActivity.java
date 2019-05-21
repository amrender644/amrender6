package com.example.sikshavavestha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void registrationButton(View view) {
        Intent regIntent=new Intent(StartActivity.this,RegisterActivity.class);
        startActivity(regIntent);
        finish();
    }

    public void loginPageButton(View view) {
        Intent regIntent=new Intent(StartActivity.this,LoginActivity.class);
        startActivity(regIntent);
        finish();
    }
}
