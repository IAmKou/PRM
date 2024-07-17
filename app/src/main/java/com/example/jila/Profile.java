package com.example.jila;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private TextView tvUsername, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        tvUsername = findViewById(R.id.textUserName);
        tvEmail = findViewById(R.id.textEmail);
        Button buttonEdit = findViewById(R.id.Edit);

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            tvEmail.setText(user.getEmail());  // This should set the email
            Log.d("Profile", "User email: " + user.getEmail());
            getUserInfo(user.getUid());
        } else {
            Log.d("Profile", "No user is currently signed in.");
        }

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, EditProfile.class);
                startActivity(intent);
            }
        });
    }

    private void getUserInfo(String userId) {
        firestore.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    String username = task.getResult().getString("name");
                    tvUsername.setText(username);
                } else {
                    // Handle the error
                }
            }
        });
    }


}














//package com.example.jila;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//
//public class Profile extends AppCompatActivity {
//
//    private FirebaseAuth auth;
//    private FirebaseFirestore firestore;
//    private TextView tvUsername, tvEmail;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_profile_screen);
//
//        auth = FirebaseAuth.getInstance();
//        firestore = FirebaseFirestore.getInstance();
//
//        tvUsername = findViewById(R.id.textUserName);
//        tvEmail = findViewById(R.id.textEmail);
//        Button buttonEdit = findViewById(R.id.Edit);
//
//
//
//        buttonEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Profile.this, EditProfile.class);
//                startActivity(intent);
//            }
//        });
//
//
//
//
//    }
//}