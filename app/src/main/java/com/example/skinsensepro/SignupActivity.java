package com.example.skinsensepro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        // Initialize FirebaseAuth and Firestore
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        EditText nameField = findViewById(R.id.createAccountName);
        EditText emailField = findViewById(R.id.createAccountEmail);
        EditText passwordField = findViewById(R.id.createAccountPassword);
        EditText confirmPasswordField = findViewById(R.id.createAccountRewritePassword);
        Button signupButton = findViewById(R.id.createAccountBTN);
        TextView loginLink = findViewById(R.id.loginLinkText);

        // Handle signup button click
        signupButton.setOnClickListener(v -> {
            String name = nameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();

            // Validate inputs
            if (TextUtils.isEmpty(name)) {
                nameField.setError("Name is required");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                emailField.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                passwordField.setError("Password is required");
                return;
            }
            if (!password.equals(confirmPassword)) {
                confirmPasswordField.setError("Passwords do not match");
                return;
            }
            if (password.length() < 6) {
                passwordField.setError("Password must be at least 6 characters");
                return;
            }

            // Create user with Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                // Save user data to Firestore
                                String userId = firebaseUser.getUid();
                                Map<String, Object> user = new HashMap<>();
                                user.put("name", name);
                                user.put("email", email);

                                firestore.collection("users").document(userId).set(user)
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(SignupActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                                            // Redirect to MainActivity
                                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(SignupActivity.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        });
                            }
                        } else {
                            Toast.makeText(SignupActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Redirect to login page
        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
