package com.example.skinsensepro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {

    private boolean isLiked = false; // Track the liked state
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get references to UI elements
        ImageView productImage = findViewById(R.id.productImage);
        TextView productName = findViewById(R.id.productName);
        TextView productPrice = findViewById(R.id.productPrice);
        TextView productAverageRating = findViewById(R.id.productAverageRating);
        View ratingIndicator = findViewById(R.id.ratingIndicator);
        TextView productDescription = findViewById(R.id.productDescription);
        RecyclerView ingredientRecyclerView = findViewById(R.id.ingredientsRecyclerView);
        Button getProductButton = findViewById(R.id.getProductButton);
        ImageView likedProductBTN = findViewById(R.id.likedProductBTN);

        // Get the product from intent
        Product product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            // Populate UI with product details
            Glide.with(this).load(product.getProductImage()).into(productImage);
            productName.setText(product.getProductName());
            productPrice.setText("$" + product.getProductPrice());
            productAverageRating.setText(product.getProductAverageRateing() + "/100");
            productDescription.setText(product.getProductDescription());

            // Set the rating indicator color
            setRatingIndicatorColor(ratingIndicator, product.getProductAverageRateing());

            // Set up the "Get Product" button
            getProductButton.setOnClickListener(v -> openProductLink(product.getProductLink()));

            // Set up RecyclerView for ingredients
            setupIngredientRecyclerView(ingredientRecyclerView, product.getProductIngredient());

            // Check if the product is liked
            checkIfProductLiked(product.getProductUid(), likedProductBTN);

            // Set up like button functionality
            likedProductBTN.setOnClickListener(v -> {
                if (isLiked) {
                    removeFromLikedProducts(product.getProductUid(), likedProductBTN);
                } else {
                    addToLikedProducts(product.getProductUid(), likedProductBTN);
                }
            });
        } else {
            Toast.makeText(this, "Product details not found", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if no product details are provided
        }
    }

    private void setRatingIndicatorColor(View ratingIndicator, int averageRating) {
        if (averageRating < 50) {
            ratingIndicator.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.red));
        } else if (averageRating < 80) {
            ratingIndicator.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.yellow));
        } else {
            ratingIndicator.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
        }
    }

    private void openProductLink(String productLink) {
        if (productLink != null && !productLink.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(productLink));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Product link not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupIngredientRecyclerView(RecyclerView recyclerView, Map<String, Integer> ingredientMap) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Ingredient> ingredients = new ArrayList<>();

        if (ingredientMap != null) {
            for (Map.Entry<String, Integer> entry : ingredientMap.entrySet()) {
                ingredients.add(new Ingredient(entry.getKey(), entry.getValue()));
            }

            // Sort ingredients by risk level
            Collections.sort(ingredients, (a, b) -> Integer.compare(b.getRiskRating(), a.getRiskRating()));
        }

        IngredientsAdapter adapter = new IngredientsAdapter(this, ingredients);
        recyclerView.setAdapter(adapter);
    }

    private void checkIfProductLiked(String productUid, ImageView likedProductBTN) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.get("likedProducts") != null) {
                List<String> likedProducts = (List<String>) documentSnapshot.get("likedProducts");
                if (likedProducts.contains(productUid)) {
                    isLiked = true;
                    likedProductBTN.setImageResource(R.drawable.liked_filled);
                } else {
                    isLiked = false;
                    likedProductBTN.setImageResource(R.drawable.liked_holo);
                }
            }
        }).addOnFailureListener(e -> Log.e("FirebaseError", "Failed to check liked status", e));
    }

    private void addToLikedProducts(String productUid, ImageView likedProductBTN) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId)
                .update("likedProducts", FieldValue.arrayUnion(productUid))
                .addOnSuccessListener(aVoid -> {
                    isLiked = true;
                    likedProductBTN.setImageResource(R.drawable.liked_filled);
                    Toast.makeText(this, "Product added to liked items", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Log.e("FirebaseError", "Failed to add product to likedProducts", e));
    }

    private void removeFromLikedProducts(String productUid, ImageView likedProductBTN) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId)
                .update("likedProducts", FieldValue.arrayRemove(productUid))
                .addOnSuccessListener(aVoid -> {
                    isLiked = false;
                    likedProductBTN.setImageResource(R.drawable.liked_holo);
                    Toast.makeText(this, "Product removed from liked items", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Log.e("FirebaseError", "Failed to remove product from likedProducts", e));
    }
}
