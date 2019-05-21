package com.example.sikshavavestha;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    DatabaseReference reference;
    TextView t1,t2,t3,t4,t5;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        t1=(TextView)findViewById(R.id.name);
        t2=(TextView)findViewById(R.id.email);
        t3=(TextView)findViewById(R.id.gender);
        t4=(TextView)findViewById(R.id.age);
        t5=(TextView)findViewById(R.id.country);

        currentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String name=dataSnapshot.child("Name").getValue().toString();
                    String email=dataSnapshot.child("Email").getValue(String.class);
                    String gender=dataSnapshot.child("Gender").getValue(String.class);
                    String age=dataSnapshot.child("Age").getValue(String.class);
                    String country=dataSnapshot.child("Country").getValue(String.class);
//                }


                t1.setText(name);
                t2.setText(email);
                t3.setText(gender);
                t4.setText(age);
                t5.setText(country);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void edit(View view) {
        Intent mainIntent=new Intent(MainActivity.this, EditActivity.class);
        startActivity(mainIntent);
        finish();
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent mainIntent=new Intent(MainActivity.this, StartActivity.class);
        startActivity(mainIntent);
        finish();

    }
}
