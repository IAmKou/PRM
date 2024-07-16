package com.example.jila;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfile extends AppCompatActivity {


    private EditText etUsername;
    private Button btnSave;

    private EditText etPassword;
    private EditText etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_screen);

        etUsername = findViewById(R.id.et_username);
        btnSave = findViewById(R.id.btn_save);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);

        // Load the current username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String currentUsername = sharedPreferences.getString("username", "");
        etUsername.setText(currentUsername);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (newUsername.isEmpty()) {
                    etUsername.setError("Enter Your Username");
                } else if (password.isEmpty()) {
                    etPassword.setError("Enter Your Password");
                } else if (!password.equals(confirmPassword)) {
                    etConfirmPassword.setError("Passwords do not match");
                    Toast.makeText(EditProfile.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Save the new username and password to SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", newUsername);
                    editor.putString("password", password); // Note: Storing passwords as plain text is not secure. Use proper encryption.
                    editor.apply();

                    Toast.makeText(EditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}