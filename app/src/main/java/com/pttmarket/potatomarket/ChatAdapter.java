package com.pttmarket.potatomarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends BaseAdapter {
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private Context context;
    private int layoutResourceId;

    public ChatAdapter(Context context, int layoutResourceId) {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
    }

    public void add(ChatMessage chatMessage) {
        chatMessages.add(chatMessage);
    }

    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutResourceId, parent, false);
        }

        TextView usernameTextView = convertView.findViewById(R.id.usernameTextView);
        TextView messageTextView = convertView.findViewById(R.id.messageTextView);

        ChatMessage chatMessage = chatMessages.get(position);
        usernameTextView.setText(chatMessage.getUsername());
        messageTextView.setText(chatMessage.getMessage());

        return convertView;
    }
}
