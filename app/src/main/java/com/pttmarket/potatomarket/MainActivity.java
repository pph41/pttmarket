package com.pttmarket.potatomarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //팔아요 버튼
        Button sellButton = findViewById(R.id.sellButton);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, boardActivity.class);
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
                Intent intent = new Intent(MainActivity.this, ChatListActivity.class);
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
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
