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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.database.ValueEventListener;


import android.provider.MediaStore;
import android.widget.Toast;

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
    private String postAuthorEmail;
    private String currentUserEmail;
    private FirebaseStorage storage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        messageEditText = findViewById(R.id.messageEditText);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        attachButton = findViewById(R.id.attachButton);
        attachmentButtonsLayout = findViewById(R.id.attachmentButtonsLayout);

        // 채팅방 이름 생성(tv_product의 정보를 가져오기)
        Intent intent = getIntent();
        postAuthorEmail = intent.getStringExtra("postAuthorEmail");
        currentUserEmail = intent.getStringExtra("currentUserEmail");
        roomName = intent.getStringExtra("roomName"); // 수정

        // Firebase Realtime Database에서 채팅 메시지를 가져오는 코드
        databaseReference = FirebaseDatabase.getInstance().getReference("ChatRooms").child(roomName);
        ArrayList<ChatMessage> messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList, mAuth.getCurrentUser().getEmail());
        chatRecyclerView.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                ChatMessage message = snapshot.getValue(ChatMessage.class);
                chatAdapter.addMessage(message);
                chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
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

        // 시간표 공유 버튼 클릭 시
        storage = FirebaseStorage.getInstance();
        Button shareTimetableButton = findViewById(R.id.shareTimetableButton);
        shareTimetableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 시간표 이미지의 다운로드 URL을 가져오는 코드
                StorageReference storageRef = storage.getReference();
                StorageReference timetableRef = storageRef.child(currentUserEmail);

                timetableRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // 시간표 이미지의 다운로드 URL을 가져오는 데 성공한 경우
                        sendImageMessage(uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 시간표 이미지의 다운로드 URL을 가져오는 데 실패한 경우 또는 시간표 이미지가 없는 경우
                        Toast.makeText(getApplicationContext(), "시간표 이미지를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void sendMessage(String messageText) {
        // 채팅 메시지를 Firebase Realtime Database에 추가
        String messageId = databaseReference.push().getKey();
        String senderId = mAuth.getCurrentUser().getEmail();
        long timestamp = System.currentTimeMillis();

        // For text messages, imageUrl can be set to null
        ChatMessage message = new ChatMessage(messageId, senderId, roomName, messageText, timestamp, null);
        databaseReference.child(messageId).setValue(message);

        // 메시지 입력창 비우기
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
        // 이미지를 전송하는 코드
        // 채팅 메시지를 Firebase Realtime Database에 추가
        String messageId = databaseReference.push().getKey();
        String senderId = mAuth.getCurrentUser().getEmail();
        long timestamp = System.currentTimeMillis();

        DatabaseReference messagesRef = databaseReference.push();
        ChatMessage chatMessage = new ChatMessage(messageId, senderId, roomName, null, timestamp, imageUri.toString());
        messagesRef.setValue(chatMessage);
    }

    // 팝업 창으로 신고 이유 선택
    private void showReportDialog() {
        DialogReport dialog = new DialogReport(ChatActivity.this, roomName, currentUserEmail);
        dialog.show();
    }

}