package com.pttmarket.potatomarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.provider.MediaStore;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private String roomName;
    private EditText messageEditText;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ChatAdapter chatAdapter;
    private RecyclerView chatRecyclerView;

    private Button attachButton;
    private LinearLayout attachmentButtonsLayout;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        messageEditText = findViewById(R.id.messageEditText);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        attachButton = findViewById(R.id.attachButton);
        attachmentButtonsLayout = findViewById(R.id.attachmentButtonsLayout);

        // 1@gwnu_ac_kr로 로그인한 것으로 가정
        String currentUserEmail = "1@gwnu.ac.kr";
        String otherUserEmail = "2@gwnu.ac.kr";
        String currentUserId = currentUserEmail.replace("@", "_").replace(".", "_");
        String otherUserId = otherUserEmail.replace("@", "_").replace(".", "_");

        // 채팅방 이름 생성
        roomName = getRoomName(currentUserId, otherUserId);

        // ChatActivity에서의 생성 부분
        chatAdapter = new ChatAdapter(this, new ArrayList<>(), mAuth, currentUserId);

        chatRecyclerView.setAdapter(chatAdapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Firebase Realtime Database에서 채팅 메시지를 가져오는 코드
        databaseReference = FirebaseDatabase.getInstance().getReference("chat_rooms").child(roomName).child("messages");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                chatAdapter.add(chatMessage);
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

        // + 버튼 클릭 시, 첨부 버튼들을 표시하거나 숨깁니다.
        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAttachmentButtons();
            }
        });

        // 사진 첨부 버튼 클릭 시 갤러리에서 이미지 선택
        Button attachPhotoButton = findViewById(R.id.attachPhotoButton);
        attachPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // 팝업 창으로 신고 이유 선택
        Button reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReportDialog();
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 뒤로가기 버튼 클릭 시 동작 정의
                finish(); // 현재 Activity를 닫음
            }
        });
    }

    // 첨부 버튼들을 표시하거나 숨기는 메서드
    private void toggleAttachmentButtons() {
        if (attachmentButtonsLayout.getVisibility() == View.VISIBLE) {
            attachmentButtonsLayout.setVisibility(View.GONE);
        } else {
            attachmentButtonsLayout.setVisibility(View.VISIBLE);
        }
    }

    // sendMessage 메서드 수정
    private void sendMessage(String messageText) {
        DatabaseReference messagesRef = databaseReference.push();
        ChatMessage chatMessage = new ChatMessage(messageText, mAuth.getCurrentUser().getDisplayName(), "");
        messagesRef.setValue(chatMessage);
        messageEditText.setText("");
    }

    // 갤러리 열기
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // 갤러리에서 선택한 이미지 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null) {
            Uri imageUri = data.getData();
            sendImageMessage(imageUri);
        }
    }

    // 이미지 메시지 전송
    private void sendImageMessage(Uri imageUri) {
        DatabaseReference messagesRef = databaseReference.push();
        ChatMessage chatMessage = new ChatMessage("", mAuth.getCurrentUser().getDisplayName(), imageUri.toString());
        messagesRef.setValue(chatMessage);
    }

    // 팝업 창으로 신고 이유 선택
    private void showReportDialog() {
        DialogReport dialog = new DialogReport(this);
        dialog.show();
    }

    // 채팅방 이름 생성 메서드 수정
    private String getRoomName(String user1, String user2) {
        String[] users = {user1, user2};
        java.util.Arrays.sort(users);
        return users[0] + "_" + users[1];
    }
}
