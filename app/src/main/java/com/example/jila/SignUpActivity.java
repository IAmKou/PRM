package com.example.jila;

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

import com.example.jila.Models.My_Models;
import com.example.jila.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.integrity.p;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Create your account");
        progressDialog.setMessage("Please Wait");


        binding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = binding.editName.getText().toString();
                String email = binding.editEmailAddress.getText().toString();
                String password = binding.editpassword.getText().toString();


                if (name.isEmpty()) {

                    binding.editName.setError("Enter Your Name");
                } else if (email.isEmpty()) {

                    binding.editEmailAddress.setError("Enter Your Email");

                } else if (password.isEmpty()) {

                    binding.editpassword.setError("Enter Your Password");

                } else if (password.length() < 6) {

                    binding.editpassword.setError("Password must be at least 6 characters long");
                } else {

                    progressDialog.show();

                    auth.createUserWithEmailAndPassword(binding.editEmailAddress.getText().toString(), binding.editpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                My_Models models = new My_Models(name, email, password);

                                String id = task.getResult().getUser().getUid();
                                firestore.collection("users").document().set(models).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if (task.isSuccessful()) {

                                            Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                                Toast.makeText(SignUpActivity.this, "Sign Up Successfully.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                finish();
                            } else {

                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(SignUpActivity.this, "Email already exists.", Toast.LENGTH_SHORT).show();
                                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(SignUpActivity.this, "Invalid email format.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });
                }
            }
        });

        binding.nextLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

            }
        });

    }
}