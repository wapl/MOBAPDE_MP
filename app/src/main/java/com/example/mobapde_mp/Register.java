package com.example.mobapde_mp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {


    EditText txtEmail;
    EditText txtPassword;
    Button btnRegister;
    FirebaseAuth auth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtEmail=findViewById(R.id.txtLogEmail);
        txtPassword=findViewById(R.id.txtLogPassword);
        btnRegister=findViewById(R.id.Login);
        progressBar=findViewById(R.id.LoginBar);

        auth=FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=txtEmail.getText().toString();
                String password=txtPassword.getText().toString();
                if(TextUtils.isEmpty(email))
                {
                    txtEmail.setError("EMAIL IS REQUIRED");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    txtPassword.setError("PASSWORD IS REQUIRED");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            Intent intent=new Intent(Register.this,MapsActivity.class );
                            startActivity(intent);
                        }
                    }
                });

            }
        });
    }

}
