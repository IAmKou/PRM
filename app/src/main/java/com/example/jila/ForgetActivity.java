package com.example.jila;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jila.databinding.ActivityForgetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ForgetActivity extends AppCompatActivity {

    ActivityForgetBinding binding;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = ActivityForgetBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Create your account");
        progressDialog.setMessage("Please Wait");


        binding.forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editEmailAddress.getText().toString();
                progressDialog.dismiss();
                if (email.isEmpty()){

                    binding.editEmailAddress.setError("Enter Your Email");
                }else{

                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                        progressDialog.dismiss();
                            Toast.makeText(ForgetActivity.this, "Please Check Your Email", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgetActivity.this, LoginActivity.class));
                        }else{

                            progressDialog.dismiss();
                            Toast.makeText(ForgetActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                }
            }
        });

       binding.login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(ForgetActivity.this, LoginActivity.class));
           }
       });
    }
}