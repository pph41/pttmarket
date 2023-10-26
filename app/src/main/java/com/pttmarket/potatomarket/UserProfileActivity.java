package com.pttmarket.potatomarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 팔아요 버튼 클릭 시
        Button sellButton = findViewById(R.id.sellButton);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 팔아요 버튼을 누를 때의 동작을 정의합니다.
                // 예: 현재 액티비티에서 다른 액티비티로 이동
                // 아래 코드를 변경하여 다른 액티비티로 이동하세요.
                startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
            }
        });

        // 채팅 버튼 클릭 시
        Button chatButton = findViewById(R.id.chatButton);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 채팅 버튼을 누를 때의 동작을 정의합니다.
                // 예: 현재 액티비티에서 채팅 목록 화면으로 이동
                // 아래 코드를 변경하여 다른 액티비티로 이동하세요.
                startActivity(new Intent(UserProfileActivity.this, ChatListActivity.class));
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
                Intent intent = new Intent(UserProfileActivity.this, ChatListActivity.class);
                startActivity(intent);
            }
        });
    }
    // 클래스 내용 추가
}