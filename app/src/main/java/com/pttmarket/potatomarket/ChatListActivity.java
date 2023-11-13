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

        // 임의의 채팅방 및 메시지 생성
        createChatWithUser("1@gwnu_ac_kr", "이", "2@gwnu_ac_kr", "2");

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
                startActivity(new Intent(ChatListActivity.this, UserProfileActivity.class));
            }
        });
    }

    // 채팅방 생성 부분 수정
    private void createChatWithUser(String userEmail1, String userNickname1, String userEmail2, String userNickname2) {
        String roomName = getRoomName(userNickname1, userNickname2);

        // 이미 존재하는 채팅방인지 확인
        if (!chatRoomList.contains(roomName)) {
            // 채팅방이 존재하지 않으면 생성
            chatRoomList.add(roomName);
            chatRoomListAdapter.notifyDataSetChanged();

            // 메시지 전송
            DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chat_rooms").child(roomName).child("messages").push();
            chatRef.child("sender").setValue(userNickname2); // 2가 보낸 것으로 가정
            chatRef.child("message").setValue("안녕하세요."); // 2가 보낸 메시지로 가정

            // 상대방의 UID를 가져오기
            String otherUserId = getOtherUserId(roomName, mAuth.getCurrentUser().getUid());

            // 채팅방 목록에서 항목을 클릭하면 해당 채팅방으로 이동
            chatListView.setOnItemClickListener((parent, view, position, id) -> {
                String selectedRoomName = chatRoomList.get(position);
                Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
                intent.putExtra("roomName", selectedRoomName);
                // 상대방의 닉네임과 UID를 함께 전달
                intent.putExtra("otherUserId", otherUserId);
                startActivity(intent);
            });
        }
    }

    // 채팅방 이름에서 상대방 UID 가져오기
    private String getOtherUserId(String roomName, String myUserId) {
        String[] userIDs = roomName.split("_");
        for (String userID : userIDs) {
            if (!userID.equals(myUserId)) {
                return userID;
            }
        }
        return "";
    }

    // 채팅방 이름 생성 메서드 수정
    private String getRoomName(String userNickname1, String userNickname2) {
        String[] nicknames = {userNickname1, userNickname2};
        java.util.Arrays.sort(nicknames);
        return nicknames[0] + "_" + nicknames[1];
    }

}
