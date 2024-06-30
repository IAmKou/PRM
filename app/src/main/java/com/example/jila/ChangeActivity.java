package com.example.jila;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jila.databinding.ActivityChangeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeActivity extends AppCompatActivity {
    ActivityChangeBinding binding;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChangeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Change your password");
        progressDialog.setMessage("Please Wait");

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(ChangeActivity.this, MainActivity.class));
                finish();
            }
        });

        binding.changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = binding.password.getText().toString();
                String newPassword = binding.newPassword.getText().toString();
                String confirmPassword = binding.confirmPassword.getText().toString();

                if (password.isEmpty()) {
                    binding.password.setError("Enter Your Password");
                } else if (newPassword.isEmpty()) {
                    binding.newPassword.setError("Enter Your newPassword");
                } else if (newPassword.length() < 6) {
                    binding.newPassword.setError("Password must be at least 6 characters long");
                } else if (confirmPassword.isEmpty()) {
                    binding.confirmPassword.setError("Enter Your confirmPassword");
                } else if (!newPassword.equals(confirmPassword)) {
                    binding.confirmPassword.setError("Passwords do not match");
                } else {
                    changePassword(password, newPassword);
                }
            }
        });
    }

    private void changePassword(String currentPassword, String newPassword) {
        progressDialog.show();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChangeActivity.this, "Password changed successfully.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ChangeActivity.this, MainActivity.class));

                                } else {
                                    Toast.makeText(ChangeActivity.this, "Password change failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(ChangeActivity.this, "Invalid current password!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
