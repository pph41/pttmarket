package com.pttmarket.potatomarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class ChatListActivity extends AppCompatActivity {
    private ImageView profileImageView;
    private TextView usernameTextView;
    private ListView chatListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        ListView chatListView = findViewById(R.id.chatListView);
        String[] chatRooms = {"User1", "User2", "User3"}; // 하드 코딩된 채팅방 목록

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatRooms);
        chatListView.setAdapter(adapter);

        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedChatRoom = chatRooms[position];
                Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
                intent.putExtra("chatRoom", selectedChatRoom);
                startActivity(intent);
            }
        });

        // 팔아요 버튼 클릭 시
        Button sellButton = findViewById(R.id.sellButton);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatListActivity.this, MainActivity.class));
            }
        });


        // 내 정보 버튼 클릭 시
        Button myProfileButton = findViewById(R.id.myProfileButton);
        myProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 내 정보 버튼을 누를 때의 동작을 정의합니다.
                // 예: 현재 액티비티에서 사용자 프로필 화면으로 이동
                // 아래 코드를 변경하여 다른 액티비티로 이동하세요.
                startActivity(new Intent(ChatListActivity.this, UserProfileActivity.class));
            }
        });
    }
}
