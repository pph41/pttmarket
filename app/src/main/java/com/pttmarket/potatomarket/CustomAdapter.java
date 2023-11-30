package com.pttmarket.potatomarket;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<User> arrayList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Constructor
    public CustomAdapter(ArrayList<User> arrayList, Context context, OnItemClickListener onItemClickListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        // Set click listener for the whole item view
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            }
        });

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

        // 채팅 버튼 클릭 시
        holder.chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getAdapterPosition()를 사용하여 현재 위치를 가져옴
                int currentPosition = holder.getAdapterPosition();

                if (currentPosition != RecyclerView.NO_POSITION) {
                    // 게시자의 이메일과 현재 사용자의 이메일을 가져오기
                    String postAuthorEmail = arrayList.get(currentPosition).getEmailid();
                    String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    String productName = arrayList.get(currentPosition).getProduct();

                    // ChatActivity 시작
                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
                    intent.putExtra("postAuthorEmail", postAuthorEmail);
                    intent.putExtra("currentUserEmail", currentUserEmail);
                    intent.putExtra("roomName", productName); // 여기를 수정함
                    view.getContext().startActivity(intent);
                }
            }
        });


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
        Button chat_btn;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profile = itemView.findViewById(R.id.profile);
            this.tv_id = itemView.findViewById(R.id.tv_id);
            this.tv_product = itemView.findViewById(R.id.tv_product);
            this.tv_price = itemView.findViewById(R.id.tv_price);
            this.chat_btn = itemView.findViewById(R.id.chat_btn);
        }
    }
}