package com.example.iorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iorder.R;
import com.example.iorder.model.FoodItem;

import java.util.ArrayList;

public class FoodItemsAdapter extends RecyclerView.Adapter<FoodItemsAdapter.ViewHolder> {
    ArrayList<FoodItem> foodItemArrayList;

    public FoodItemsAdapter(ArrayList<FoodItem> foodItemArrayList) {
        this.foodItemArrayList = foodItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem food  = foodItemArrayList.get(position);
        holder.itemName.setText(food.getName());
        holder.itemPrice.setText("Rs. "+String.valueOf(food.getPrice()));

    }

    @Override
    public int getItemCount() {
        return foodItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName,itemPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tv_itemName);
            itemPrice = itemView.findViewById(R.id.tv_itemPrice);
        }
    }
}
