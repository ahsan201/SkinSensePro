package com.example.skinsensepro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView heroImagesRecyclerView, productGrid;
    private HeroImagesAdapter heroImagesAdapter;
    private ProductGridAdapter productGridAdapter;
    private List<String> imageUrls;
    private List<Product> productList;

    private FirebaseFirestore db;

    private ImageButton bodycareCategory, haircareCategory, makeupCategory, skincareCategory;
    private ImageButton lastSelectedCategory;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView for hero images
        heroImagesRecyclerView = view.findViewById(R.id.heroImagesRecyclerView);
        heroImagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        imageUrls = new ArrayList<>();
        heroImagesAdapter = new HeroImagesAdapter(getContext(), imageUrls);
        heroImagesRecyclerView.setAdapter(heroImagesAdapter);

        // Initialize RecyclerView for product grid
        productGrid = view.findViewById(R.id.productGrid);
        productGrid.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns
        productList = new ArrayList<>();
        productGridAdapter = new ProductGridAdapter(getContext(), productList);
        productGrid.setAdapter(productGridAdapter);

        // Setup category buttons
        setupCategoryButtons(view);

        // Fetch hero images from Firebase Storage
        fetchHeroImages();

        // Show default category
        selectCategory(makeupCategory, "makeup");
    }

    private void fetchHeroImages() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference heroImagesRef = storage.getReference().child("heroImages");

        heroImagesRef.listAll().addOnSuccessListener(listResult -> {
            List<String> tempUrls = new ArrayList<>();

            for (StorageReference fileRef : listResult.getItems()) {
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    tempUrls.add(uri.toString());

                    // After all URLs are fetched, invert the order and update the adapter
                    if (tempUrls.size() == listResult.getItems().size()) {
                        imageUrls.clear();
                        for (int i = tempUrls.size() - 1; i >= 0; i--) {
                            imageUrls.add(tempUrls.get(i));
                        }
                        heroImagesAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Failed to load hero images: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void setupCategoryButtons(View view) {
        bodycareCategory = view.findViewById(R.id.bodycareCategory);
        haircareCategory = view.findViewById(R.id.haircareCategory);
        makeupCategory = view.findViewById(R.id.makeupCategory);
        skincareCategory = view.findViewById(R.id.skincareCategory);

        // Set click listeners for categories
        bodycareCategory.setOnClickListener(v -> selectCategory(bodycareCategory, "bodycare"));
        haircareCategory.setOnClickListener(v -> selectCategory(haircareCategory, "haircare"));
        makeupCategory.setOnClickListener(v -> selectCategory(makeupCategory, "makeup"));
        skincareCategory.setOnClickListener(v -> selectCategory(skincareCategory, "skincare"));
    }

    private void selectCategory(ImageButton selectedCategory, String category) {
        // Highlight the selected category
        if (lastSelectedCategory != null) {
            lastSelectedCategory.setBackgroundResource(0); // Remove previous selection border
        }
        selectedCategory.setBackgroundResource(R.drawable.category_border); // Add border to selected
        lastSelectedCategory = selectedCategory;

        // Fetch products for the selected category
        fetchProductsByCategory(category);
    }

    private void fetchProductsByCategory(String category) {
        db.collection("product")
                .whereEqualTo("productCategory", category)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Product product = document.toObject(Product.class);
                        if (product != null) {
                            product.setProductUid(document.getId()); // Set the document ID as the product UID
                            productList.add(product);
                        }
                    }
                    productGridAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load products: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
