package com.example.jila;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jila.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating your account");
        progressDialog.setMessage("Please Wait");

        binding.newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

            }
        });
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = binding.editEmailAddress.getText().toString();
                String password = binding.editpassword.getText().toString();


                if (email.isEmpty()) {

                    binding.editEmailAddress.setError("Enter Your Email");
                } else if (password.isEmpty()) {

                    binding.editpassword.setError("Enter Your Password");

                } else {

                    progressDialog.show();

                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                String userId = auth.getCurrentUser().getUid();
                                checkUserRole(userId);

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Account or password is incorrect !", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });


        binding.forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, ForgetActivity.class));

            }
        });
    }

    private void saveEmailToSharedPreferences(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();
    }
    private void checkUserRole(String userId) {
        firestore.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    String role = task.getResult().getString("role");
                    progressDialog.dismiss();
                    if ("admin".equals(role)) {
                        startActivity(new Intent(LoginActivity.this, AdminDashboard.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Failed to retrieve user role!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}