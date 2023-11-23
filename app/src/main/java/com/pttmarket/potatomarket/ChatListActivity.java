package com.pttmarket.potatomarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userChatReference;
    private CustomAdapter customAdapter;
    private RecyclerView chatListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        mAuth = FirebaseAuth.getInstance();
        chatListRecyclerView = findViewById(R.id.chatListView);

        userChatReference = FirebaseDatabase.getInstance().getReference("ChatRooms");

        ArrayList<User> chatList = new ArrayList<>();
        customAdapter = new CustomAdapter(chatList, this, new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click here
                User selectedChatRoom = chatList.get(position);
                String roomName = selectedChatRoom.getId();
                startChatActivity(roomName);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatListRecyclerView.setLayoutManager(layoutManager);
        chatListRecyclerView.setAdapter(customAdapter);

        userChatReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                // Assuming each child under "ChatRooms" represents a chat room
                String roomName = snapshot.getKey();

                // Create a User object or use your existing User class
                User chatRoom = new User();
                chatRoom.setId(roomName); // Assuming roomName can be used as an ID for now
                // Set other properties of the User object as needed

                chatList.add(chatRoom);
                customAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //팔아요 버튼
        Button sellButton = findViewById(R.id.sellButton);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatListActivity.this, boardActivity.class);
                startActivity(intent);
            }
        });

        // 채팅 버튼 클릭 시
        Button chatButton = findViewById(R.id.chatButton);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 채팅 버튼을 누를 때의 동작을 정의합니다.
                // 현재 액티비티에서 채팅 목록 화면으로 이동
                Intent intent = new Intent(ChatListActivity.this, ChatListActivity.class);
                startActivity(intent);
            }
        });

        // 내 정보 버튼 클릭 시
        Button myProfileButton = findViewById(R.id.myProfileButton);
        myProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 내 정보 버튼을 누를 때의 동작을 정의합니다.
                // 현재 액티비티에서 사용자 프로필 화면으로 이동
                Intent intent = new Intent(ChatListActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startChatActivity(String roomName) {
        // Start ChatActivity with the selected chat room
        Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
        intent.putExtra("roomName", roomName);
        startActivity(intent);
    }
}
