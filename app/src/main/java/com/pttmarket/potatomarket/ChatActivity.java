package com.pttmarket.potatomarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {

    private String chatRoomName; // 채팅방 이름을 저장할 변수
    private ListView chatRecyclerView;
    private EditText messageEditText;
    private Button sendButton;
    private Button attachButton;
    private Button backButton;
    private LinearLayout attachmentButtonsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 채팅방 이름을 인텐트로부터 전달받음
        Intent intent = getIntent();
        if (intent != null) {
            chatRoomName = intent.getStringExtra("chatRoom");
        }

        // 채팅방 이름을 화면에 표시
        setTitle(chatRoomName);

        // UI 요소 초기화
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        attachButton = findViewById(R.id.attachButton);
        attachmentButtonsLayout = findViewById(R.id.attachmentButtonsLayout);
        backButton = findViewById(R.id.backButton);

        // + 버튼 클릭 시, 첨부 버튼들을 표시하거나 숨깁니다.
        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAttachmentButtons();
            }
        });

        // 메시지 전송 버튼 클릭 시, 메시지를 전송합니다.
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        //backButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
                // 뒤로가기 버튼 클릭 시 동작 정의
        //        finish(); // 현재 Activity를 닫음
        //    }
        //});

    }

    // 첨부 버튼들을 표시하거나 숨기는 메서드
    private void toggleAttachmentButtons() {
        if (attachmentButtonsLayout.getVisibility() == View.VISIBLE) {
            attachmentButtonsLayout.setVisibility(View.GONE);
        } else {
            attachmentButtonsLayout.setVisibility(View.VISIBLE);
        }
    }

    // 메시지를 전송하는 메서드 (이 부분은 실제 메시지 전송 기능으로 구현해야 합니다.)
    private void sendMessage() {
        String message = messageEditText.getText().toString();
        // 메시지 전송 로직을 추가하세요.
        // 채팅 대화 목록에 메시지를 추가하고 화면을 업데이트합니다.
        // 이 부분은 실제 서버와의 통신 또는 데이터베이스 연동이 필요합니다.
        // 예를 들어, 어떤 채팅 메시지 모델을 만들고 데이터를 저장하는 방식으로 구현할 수 있습니다.
        // 여기에서는 간단한 예제로 메시지를 출력하는 것으로 대체합니다.
        // chatAdapter.add(message); // chatAdapter는 ListView용 어댑터입니다.
        // messageEditText.setText(""); // 메시지 입력란 비우기
    }
}
