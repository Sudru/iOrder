package com.example.iorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iorder.R;
import com.example.iorder.model.OrderedItem;

import java.util.ArrayList;

public class OrderedItemAdapter extends RecyclerView.Adapter<OrderedItemAdapter.ViewHolder> {
    ArrayList<OrderedItem> list;

    public OrderedItemAdapter(ArrayList<OrderedItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordered_item_view,parent,false);
         return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemName.setText(list.get(position).getName());
        holder.itemQuantity.setText(String.valueOf(list.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemName,itemQuantity;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            itemName = itemView.findViewById(R.id.tv_order_item_name);
            itemQuantity = itemView.findViewById(R.id.tv_ordered_item_quantity);
        }
    }
}
