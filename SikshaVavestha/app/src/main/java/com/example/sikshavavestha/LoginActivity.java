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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout mEmail,mPassword;
    Toolbar loginToolbar;
    ProgressDialog mLoginProgress;
    FirebaseAuth mAuth;
    private DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginToolbar=(Toolbar)findViewById(R.id.registerToolbar);
        setSupportActionBar(loginToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoginProgress=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        userReference= FirebaseDatabase.getInstance().getReference().child("Users");

        mEmail=(TextInputLayout)findViewById(R.id.loginEmail);
        mPassword=(TextInputLayout)findViewById(R.id.loginPassword);
//        loginBtn=(Button)findViewById(R.id.loginButton);
    }
    public void loginButton1(View view) {
        String email,password;
        email=mEmail.getEditText().getText().toString();
        password=mPassword.getEditText().getText().toString();

        if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
            mLoginProgress.setTitle("Logging In");
            mLoginProgress.setMessage("Please wait while we check your account !");
            mLoginProgress.setCanceledOnTouchOutside(false);
            mLoginProgress.show();
            loginUser(email,password);
        }
        else {
            Toast.makeText(getApplicationContext(),"Complete all input field first",Toast.LENGTH_SHORT).show();
        }

    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mLoginProgress.dismiss();
                    String deviceToken= FirebaseInstanceId.getInstance().getToken();
                    String currentUserId=mAuth.getCurrentUser().getUid();
                    userReference.child(currentUserId).child("deviceToken").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        }
                    });
                }
                else {
                    mLoginProgress.hide();
                    Toast.makeText(getApplicationContext(),"You got some error. Please check all the input fields and try again. ",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
