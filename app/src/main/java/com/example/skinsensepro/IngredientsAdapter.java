package com.example.skinsensepro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private Context context;
    private List<Ingredient> ingredients;

    public IngredientsAdapter(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);

        holder.ingredientName.setText(ingredient.getName());
        holder.ingredientRating.setText(String.valueOf(ingredient.getRiskRating()));

        // Set indicator color
        if (ingredient.getRiskRating() < 50) {
            holder.indicator.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.red));
        } else if (ingredient.getRiskRating() < 80) {
            holder.indicator.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.yellow));
        } else {
            holder.indicator.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));
        }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientName, ingredientRating;
        View indicator;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredientName);
            ingredientRating = itemView.findViewById(R.id.ingredientRating);
            indicator = itemView.findViewById(R.id.indicator);
        }
    }
}
