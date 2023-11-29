package com.pttmarket.potatomarket;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DialogReport extends Dialog {

    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5;
    private Button submitButton;
    private DatabaseReference chatUsersRef;
    private String roomName;

    public DialogReport(@NonNull Context context, String roomName, String reporterId) {
        super(context);
        setContentView(R.layout.dialog_report);

        setTitle("신고하기");

        this.roomName = roomName;

        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        checkBox5 = findViewById(R.id.checkBox5);

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(roomName, reporterId);
                dismiss();
            }
        });

        chatUsersRef = FirebaseDatabase.getInstance().getReference().child("ChatRooms").child(roomName).child("senderId");
    }

    private void sendEmail(String roomName, String reporterId) {
        String emailSubject = "감자마켓 유저를 신고합니다";

        // 내용
        StringBuilder emailBodyBuilder = new StringBuilder();
        emailBodyBuilder.append("신고할 닉네임: ").append("\n");
        emailBodyBuilder.append("채팅방 이름: ").append(roomName).append("\n");
        if (checkBox1.isChecked()) {
            emailBodyBuilder.append("비매너 사용자에요").append("\n");
        }
        if (checkBox2.isChecked()) {
            emailBodyBuilder.append("잠수를 탔어요").append("\n");
        }
        if (checkBox3.isChecked()) {
            emailBodyBuilder.append("사기 당했어요").append("\n");
        }
        if (checkBox4.isChecked()) {
            emailBodyBuilder.append("욕설, 비방을 해요").append("\n");
        }
        if (checkBox5.isChecked()) {
            emailBodyBuilder.append("다른 문제가 있어요: ");
        }

        String emailBody = emailBodyBuilder.toString();

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"seru0021@gmail.com"}); // 수신자 이메일 주소
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

        // 다이얼로그를 통해 여러 앱 선택
        getContext().startActivity(Intent.createChooser(emailIntent, "이메일 어플 선택"));
    }
}