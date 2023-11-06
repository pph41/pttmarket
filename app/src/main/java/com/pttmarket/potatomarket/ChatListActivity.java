package com.pttmarket.potatomarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {
    private ListView chatListView;
    private List<String> chatRoomList;
    private ArrayAdapter<String> chatRoomListAdapter;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        mAuth = FirebaseAuth.getInstance();
        chatListView = findViewById(R.id.chatListView);
        chatRoomList = new ArrayList<>();
        chatRoomListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatRoomList);
        chatListView.setAdapter(chatRoomListAdapter);

        // Firebase Realtime Database에서 채팅 목록을 가져오는 코드
        databaseReference = FirebaseDatabase.getInstance().getReference("chat_rooms");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatRoomList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 채팅방 이름 또는 정보를 가져와 chatRoomList에 추가
                    String roomName = snapshot.getKey();
                    chatRoomList.add(roomName);
                }
                chatRoomListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 처리 중 에러 발생 시 예외 처리
            }
        });

        // 채팅방 목록에서 항목을 클릭하면 해당 채팅방으로 이동
        chatListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedRoomName = chatRoomList.get(position);
            Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
            intent.putExtra("roomName", selectedRoomName);
            startActivity(intent);
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
