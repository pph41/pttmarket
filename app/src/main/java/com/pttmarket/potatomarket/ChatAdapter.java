package com.pttmarket.potatomarket;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import com.bumptech.glide.Glide;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    // Add two constants for message types
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context context;
    private List<ChatMessage> messages;
    private FirebaseAuth mAuth;
    private String currentUserId;

    public ChatAdapter(Context context, List<ChatMessage> messages, FirebaseAuth auth, String currentUserId) {
        this.context = context;
        this.messages = messages;
        this.mAuth = auth;
        this.currentUserId = currentUserId;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage chatMessage = messages.get(position);
        if (chatMessage != null && holder != null) {
            if (holder.usernameTextView != null && holder.messageTextView != null) {
                holder.usernameTextView.setText(chatMessage.getSenderName());

                // Check if the message is a text message or an image message
                if (chatMessage.getImageUrl() != null && !chatMessage.getImageUrl().isEmpty()) {
                    // Image message
                    holder.messageTextView.setVisibility(View.GONE);
                    holder.imageMessageView.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(chatMessage.getImageUrl())
                            .into(holder.imageMessageView);
                } else {
                    // Text message
                    holder.messageTextView.setVisibility(View.VISIBLE);
                    holder.imageMessageView.setVisibility(View.GONE);
                    holder.messageTextView.setText(chatMessage.getMessageText());
                }
            } else {
                Log.e("ChatAdapter", "TextViews are null in onBindViewHolder");
            }
        } else {
            Log.e("ChatAdapter", "chatMessage or holder is null in onBindViewHolder");
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_sent, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_received, parent, false);
        }

        TextView usernameTextView = view.findViewById(R.id.usernameTextView);
        TextView messageTextView = view.findViewById(R.id.messageTextView);
        ImageView imageMessageView = view.findViewById(R.id.imageMessageView); // Add ImageView

        return new ChatViewHolder(view, usernameTextView, messageTextView, imageMessageView);
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }


    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = messages.get(position);

        if (chatMessage.getSenderName() != null && chatMessage.getSenderName().equals(currentUserId)) {
            // Sent message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // Received message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    public void add(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView messageTextView;
        ImageView imageMessageView; // Add ImageView

        public ChatViewHolder(@NonNull View itemView, TextView usernameTextView,
                              TextView messageTextView, ImageView imageMessageView) {
            super(itemView);
            this.usernameTextView = usernameTextView;
            this.messageTextView = messageTextView;
            this.imageMessageView = imageMessageView;
        }
    }
}
