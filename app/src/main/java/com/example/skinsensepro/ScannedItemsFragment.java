package com.example.skinsensepro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ScannedItemsFragment extends Fragment {

    private RecyclerView scannedProductGrid;
    private ProductGridAdapter productGridAdapter;
    private List<Product> scannedProducts;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanned, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        scannedProducts = new ArrayList<>();
        productGridAdapter = new ProductGridAdapter(getContext(), scannedProducts);

        scannedProductGrid = view.findViewById(R.id.scannedProductGrid);
        scannedProductGrid.setLayoutManager(new GridLayoutManager(getContext(), 2));
        scannedProductGrid.setAdapter(productGridAdapter);

        loadScannedProducts();
    }

    private void loadScannedProducts() {
        String userId = auth.getCurrentUser().getUid();

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<String> scannedProductUids = (List<String>) documentSnapshot.get("scannedProducts");

                    if (scannedProductUids != null && !scannedProductUids.isEmpty()) {
                        fetchScannedProducts(scannedProductUids);
                    } else {
                        Toast.makeText(getContext(), "No scanned products found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error fetching user data", Toast.LENGTH_SHORT).show());
    }

    private void fetchScannedProducts(List<String> productUids) {
        scannedProducts.clear();

        for (String uid : productUids) {
            db.collection("product").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Product product = documentSnapshot.toObject(Product.class);
                        if (product != null) {
                            scannedProducts.add(product);
                        }
                        productGridAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error fetching product: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}
