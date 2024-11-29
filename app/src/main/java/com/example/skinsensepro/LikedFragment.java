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

public class LikedFragment extends Fragment {

    private RecyclerView likedRecyclerView;
    private ProductGridAdapter likedProductAdapter;
    private List<Product> likedProductList;

    private FirebaseFirestore db;
    private String userId;

    public LikedFragment() {
        // Required empty public constructor
    }

    public static LikedFragment newInstance() {
        return new LikedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_liked, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView
        likedRecyclerView = view.findViewById(R.id.likedRecyclerView);
        likedRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        likedProductList = new ArrayList<>();
        likedProductAdapter = new ProductGridAdapter(getContext(), likedProductList);
        likedRecyclerView.setAdapter(likedProductAdapter);

        // Fetch liked products
        fetchLikedProducts();
    }

    private void fetchLikedProducts() {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.get("likedProducts") != null) {
                        List<String> likedProductIds = (List<String>) documentSnapshot.get("likedProducts");

                        if (!likedProductIds.isEmpty()) {
                            fetchProductDetails(likedProductIds);
                        } else {
                            Toast.makeText(getContext(), "No liked products found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to fetch liked products: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchProductDetails(List<String> likedProductIds) {
        likedProductList.clear(); // Clear the list before fetching

        for (String productId : likedProductIds) {
            db.collection("product").document(productId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Product product = documentSnapshot.toObject(Product.class);
                            if (product != null) {
                                product.setProductUid(documentSnapshot.getId()); // Set the UID
                                likedProductList.add(product);
                                likedProductAdapter.notifyDataSetChanged();
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to load product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
