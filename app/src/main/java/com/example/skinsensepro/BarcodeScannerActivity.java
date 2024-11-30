package com.example.skinsensepro;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeScannerActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        startBarcodeScanner();
    }

    private void startBarcodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0); // Use the default camera
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String scannedCode = result.getContents();
                saveScannedProduct(scannedCode);
            } else {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void saveScannedProduct(String scannedCode) {
        String userId = auth.getCurrentUser().getUid();

        db.collection("product").document(scannedCode)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        db.collection("users").document(userId)
                                .update("scannedProducts", FieldValue.arrayUnion(scannedCode))
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Product added to scanned items", Toast.LENGTH_SHORT).show();
                                    finish(); // Return to the HomeFragment
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Error updating scanned items", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Product not found in database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error fetching product data", Toast.LENGTH_SHORT).show());
    }
}
