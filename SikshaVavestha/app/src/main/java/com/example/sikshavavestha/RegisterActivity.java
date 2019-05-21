package com.example.sikshavavestha;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout mDisplayName,mEmail,mPassword,mDisplayGender,mDisplayAge,mDisplayCountry;
    Button createBtn;
    Toolbar registerToolbar;
    FirebaseAuth mAuth;
    ProgressDialog mRegProgress;
    DatabaseReference mReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerToolbar=(Toolbar)findViewById(R.id.registerToolbar);
        setSupportActionBar(registerToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegProgress=new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();

        mDisplayName=(TextInputLayout) findViewById(R.id.regDisplayName);
        mEmail=(TextInputLayout)findViewById(R.id.regEmail);
        mPassword=(TextInputLayout)findViewById(R.id.regPassword);
        mDisplayGender=(TextInputLayout) findViewById(R.id.regDisplayGender);
        mDisplayAge=(TextInputLayout) findViewById(R.id.regDisplayAge);
        mDisplayCountry=(TextInputLayout) findViewById(R.id.regDisplayCountry);
        createBtn=(Button)findViewById(R.id.regCreateButton);
    }

    public void createAccountButton(View view) {
        String displayName,email,password,gender,age,country;
        displayName=mDisplayName.getEditText().getText().toString();
        email=mEmail.getEditText().getText().toString();
        password=mPassword.getEditText().getText().toString();
        gender=mDisplayGender.getEditText().getText().toString();
        age=mDisplayAge.getEditText().getText().toString();
        country=mDisplayCountry.getEditText().getText().toString();

        if(!TextUtils.isEmpty(displayName) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
            mRegProgress.setTitle("Registering User");
            mRegProgress.setMessage("Please wait while we create your account !");
            mRegProgress.setCanceledOnTouchOutside(false);
            mRegProgress.show();
            registerUser(displayName,email,password,gender,age,country);
        }else {
            Toast.makeText(getApplicationContext(),"Complete all input field first",Toast.LENGTH_SHORT).show();
        }

    }

    private void registerUser(final String displayName, final String email, String password, final String gender, final String age, final String country) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
                    String uId=currentUser.getUid();
                    mReference=FirebaseDatabase.getInstance().getReference().child("Users").child(uId);
                    String deviceToken= FirebaseInstanceId.getInstance().getToken();
                    Map<String,String> map=new HashMap<>();
                    map.put("Name",displayName);
                    map.put("Email",email);
                    map.put("Gender",gender);
                    map.put("Age",age);
                    map.put("Country",country);

                    mReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mRegProgress.dismiss();
                                Intent mainIntent=new Intent(RegisterActivity.this, MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            }
                        }
                    });

                }
                else {
                    mRegProgress.hide();
                    Toast.makeText(getApplicationContext(),"You got some error. Please check all the input fields and try again. ",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
