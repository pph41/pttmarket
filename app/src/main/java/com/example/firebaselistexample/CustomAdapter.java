package com.example.firebaselistexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<User> arrayList;
    private Context context;

    public CustomAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProfile())
                .into(holder.profile);
        holder.tv_id.setText(arrayList.get(position).getId());
        holder.tv_product.setText(arrayList.get(position).getProduct());
        holder.tv_price.setText(arrayList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        //삼항연산자 arraylist 가 null가 참이면 왼쪽 거짓이면 오른쪽
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView tv_id;
        TextView tv_product;
        TextView tv_price;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profile = itemView.findViewById(R.id.profile);
            this.tv_id = itemView.findViewById(R.id.tv_id);
            this.tv_product = itemView.findViewById(R.id.tv_product);
            this.tv_price = itemView.findViewById(R.id.tv_price);
        }
    }
}
