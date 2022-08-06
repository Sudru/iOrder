package com.example.iorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iorder.R;
import com.example.iorder.model.FoodItem;

import java.util.ArrayList;

public class FoodItemsAdapter extends RecyclerView.Adapter<FoodItemsAdapter.ViewHolder> {
    ArrayList<FoodItem> foodItemArrayList;
    QuantityListener listener;

    public FoodItemsAdapter(ArrayList<FoodItem> foodItemArrayList,QuantityListener listener) {
        this.foodItemArrayList = foodItemArrayList;
        this.listener = listener;
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
        holder.quantity.setText(String.valueOf(food.getQuantity()));
        holder.addQuantity.setOnClickListener(v->{
            int q = food.getQuantity();
            food.setQuantity(q+1);
            holder.quantity.setText(String.valueOf(q+1));
            listener.onQuantityChanged(food);
        });
        holder.subtractQuantity.setOnClickListener(v->{
            int q = food.getQuantity();
            if(q>0) {
                food.setQuantity(q - 1);
                holder.quantity.setText(String.valueOf(q-1));
            }
            listener.onQuantityChanged(food);
        });


    }

    @Override
    public int getItemCount() {
        return foodItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName,itemPrice,quantity;
        Button addQuantity,subtractQuantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tv_itemName);
            itemPrice = itemView.findViewById(R.id.tv_itemPrice);
            addQuantity = itemView.findViewById(R.id.btn_add);
            subtractQuantity = itemView.findViewById(R.id.btn_subtract);
            quantity = itemView.findViewById(R.id.tv_quantity);
        }
    }
    public interface QuantityListener{
        void onQuantityChanged(FoodItem item);
    }
}
