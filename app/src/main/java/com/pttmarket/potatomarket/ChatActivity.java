package com.pttmarket.potatomarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class ChatActivity extends AppCompatActivity {
    private String roomName;
    private EditText messageEditText;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ChatAdapter chatAdapter;
    private ListView chatListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        messageEditText = findViewById(R.id.messageEditText);
        chatListView = findViewById(R.id.chatListView);

        // ChatListActivity에서 전달한 채팅방 이름 가져오기
        roomName = getIntent().getStringExtra("roomName");

        chatAdapter = new ChatAdapter(this, R.layout.chat_message_item);
        chatListView.setAdapter(chatAdapter);

        // Firebase Realtime Database에서 채팅 메시지를 가져오는 코드
        databaseReference = FirebaseDatabase.getInstance().getReference("chat_rooms").child(roomName).child("messages");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                chatAdapter.add(chatMessage);
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        // 메시지를 보내는 버튼 구현
        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageEditText.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    sendMessage(messageText);
                }
            }
        });
    }

    private void sendMessage(String messageText) {
        DatabaseReference messagesRef = databaseReference.push();
        ChatMessage chatMessage = new ChatMessage(messageText, mAuth.getCurrentUser().getDisplayName());
        messagesRef.setValue(chatMessage);
        messageEditText.setText("");
    }

}
