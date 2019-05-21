package com.example.sikshavavestha;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    DatabaseReference reference;
    EditText t1,t2,t3,t4,t5;
    private String currentUserId;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        t1=(EditText) findViewById(R.id.name);
        t2=(EditText) findViewById(R.id.email);
        t3=(EditText) findViewById(R.id.gender);
        t4=(EditText) findViewById(R.id.age);
        t5=(EditText) findViewById(R.id.country);

        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name=dataSnapshot.child("Name").getValue().toString();
                String email=dataSnapshot.child("Email").getValue(String.class);
                String gender=dataSnapshot.child("Gender").getValue(String.class);
                String age=dataSnapshot.child("Age").getValue(String.class);
                String country=dataSnapshot.child("Country").getValue(String.class);

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

    public void save(View view) {
        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
        String uId=currentUser.getUid();
        mReference=FirebaseDatabase.getInstance().getReference().child("Users").child(uId);
        String deviceToken= FirebaseInstanceId.getInstance().getToken();
        Map<String,String> map=new HashMap<>();
        map.put("Name",t1.getText().toString());
        map.put("Email",t2.getText().toString());
        map.put("Gender",t3.getText().toString());
        map.put("Age",t4.getText().toString());
        map.put("Country",t5.getText().toString());

        mReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent mainIntent=new Intent(EditActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
            }
        });
    }

    public void cancle(View view) {
        Intent mainIntent=new Intent(EditActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
