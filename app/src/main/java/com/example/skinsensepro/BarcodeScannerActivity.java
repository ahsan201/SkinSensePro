package com.example.skinsensepro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeScannerActivity extends AppCompatActivity {

    private Button scanButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize the scan button
        scanButton = findViewById(R.id.scan_button);

        // Set click listener for the scan button
        scanButton.setOnClickListener(v -> startBarcodeScanning());
    }

    // Start the barcode scanner
    private void startBarcodeScanning() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a barcode"); // Scanner message
        integrator.setCameraId(0); // 0 for rear camera
        integrator.setBeepEnabled(true); // Play beep sound on successful scan
        integrator.setBarcodeImageEnabled(true); // Save barcode image
        integrator.setOrientationLocked(false); // Allow rotation
        integrator.initiateScan(); // Start scanning
    }

    // Handle the scanning result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() != null) {
                // Barcode successfully scanned
                String scannedBarcode = result.getContents();
                Toast.makeText(this, "Scanned: " + scannedBarcode, Toast.LENGTH_SHORT).show();

                // Fetch product details using the scanned barcode
                fetchProductDetailsByBarcode(scannedBarcode);
            } else {
                // Scan canceled
                Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Fetch product details from Firestore using the scanned barcode
    private void fetchProductDetailsByBarcode(String barcode) {
        db.collection("product").document(barcode)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Product product = documentSnapshot.toObject(Product.class);
                        if (product != null) {
                            // Navigate to ProductDetailsActivity with the product data
                            Intent intent = new Intent(this, ProductDetailsActivity.class);
                            intent.putExtra("product", product);
                            startActivity(intent);
                        }
                    } else {
                        // Product not found
                        Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Toast.makeText(this, "Failed to fetch product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
