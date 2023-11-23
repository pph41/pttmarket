package com.pttmarket.potatomarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MY_MESSAGE = 1;
    private static final int VIEW_TYPE_OTHER_MESSAGE = 2;

    private Context context;
    private ArrayList<ChatMessage> messageList;
    private String currentUserEmail;

    public ChatAdapter(ArrayList<ChatMessage> messageList, String currentUserEmail) {
        this.messageList = messageList;
        this.currentUserEmail = currentUserEmail;
    }

    public void addMessage(ChatMessage message) {
        messageList.add(message);
        notifyDataSetChanged(); // Notify the adapter that the data set has changed
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MY_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_message, parent, false);
            return new MyMessageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_other_message, parent, false);
            return new OtherMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);

        if (holder instanceof MyMessageViewHolder) {
            ((MyMessageViewHolder) holder).bind(message);
        } else if (holder instanceof OtherMessageViewHolder) {
            ((OtherMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messageList.get(position);
        return message.getSenderId().equals(currentUserEmail) ? VIEW_TYPE_MY_MESSAGE : VIEW_TYPE_OTHER_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // timestamp를 시간 문자열로 변환하는 메서드
    private static String convertTimestampToTime(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        // 시간과 분을 가져와서 문자열로 반환
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }

    // 나의 메시지를 표시하는 ViewHolder
    private static class MyMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;
        TextView nicknameTextView;
        TextView timeTextView;

        MyMessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.my_message_text_view);
            messageImageView = itemView.findViewById(R.id.my_message_image_view);
            nicknameTextView = itemView.findViewById(R.id.my_message_nickname_text_view);
            timeTextView = itemView.findViewById(R.id.my_message_time_text_view);  // 추가된 부분: 시간 표시 TextView
        }

        void bind(ChatMessage message) {
            if (message.getImageUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(message.getImageUrl())
                        .into(messageImageView);
                messageTextView.setVisibility(View.GONE);
            } else {
                messageTextView.setText(message.getMessage());
            }
            nicknameTextView.setText(message.getSenderNickname());

            // 시간 표시
            long timestamp = message.getTimestamp();
            String time = convertTimestampToTime(timestamp);
            timeTextView.setText(time);
        }
    }

    // 상대방의 메시지를 표시하는 ViewHolder
    private static class OtherMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;
        TextView nicknameTextView;
        TextView timeTextView;

        OtherMessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.other_message_text_view);
            messageImageView = itemView.findViewById(R.id.other_message_image_view);
            nicknameTextView = itemView.findViewById(R.id.other_message_nickname_text_view);
            timeTextView = itemView.findViewById(R.id.other_message_time_text_view);  // 추가된 부분: 시간 표시 TextView
        }

        void bind(ChatMessage message) {
            if (message.getImageUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(message.getImageUrl())
                        .into(messageImageView);
                messageTextView.setVisibility(View.GONE);
            } else {
                messageTextView.setText(message.getMessage());
            }
            nicknameTextView.setText(message.getSenderNickname());

            // 시간 표시
            long timestamp = message.getTimestamp();
            String time = convertTimestampToTime(timestamp);
            timeTextView.setText(time);
        }
    }
}
