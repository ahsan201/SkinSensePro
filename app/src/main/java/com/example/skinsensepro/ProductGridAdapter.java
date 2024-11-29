package com.example.skinsensepro;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductGridAdapter extends RecyclerView.Adapter<ProductGridAdapter.ProductViewHolder> {

    private final Context context;
    private List<Product> productList;

    public ProductGridAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void updateProductList(List<Product> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_grid_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getProductName());
        holder.productPrice.setText("$" + product.getProductPrice());

        int rating = product.getProductAverageRateing();
        holder.productRating.setText(rating + "/100");

        if (rating < 50) {
            holder.ratingIndicator.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.red));
        } else if (rating < 80) {
            holder.ratingIndicator.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.yellow));
        } else {
            holder.ratingIndicator.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));
        }

        Glide.with(context).load(product.getProductImage()).into(holder.productImage);

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Product UID: " + product.getProductUid(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productRating;
        ImageView productImage;
        View ratingIndicator;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productRating = itemView.findViewById(R.id.productRating);
            productImage = itemView.findViewById(R.id.productImage);
            ratingIndicator = itemView.findViewById(R.id.ratingIndicator);
        }
    }
}
