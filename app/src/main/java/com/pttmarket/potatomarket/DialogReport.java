package com.pttmarket.potatomarket;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;
import com.google.firebase.database.ValueEventListener;

import com.pttmarket.potatomarket.R;

public class DialogReport extends android.app.Dialog {

    private CheckBox checkBox1, checkBox2;
    private Button submitButton;
    private DatabaseReference chatUsersRef; // 채팅방 참여자 정보를 저장하는 노드의 참조

    public DialogReport(android.content.Context context, String roomName, String currentUserEmail) {
        super(context);
        setContentView(R.layout.dialog_report);

        setTitle("신고하기");

        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle submit button click
                reportUser(roomName, currentUserEmail); // 상대방 유저를 신고
                dismiss();
            }
        });

        chatUsersRef = FirebaseDatabase.getInstance().getReference().child("ChatRooms").child(roomName).child("users");
    }

    private void reportUser(String roomName, String currentUserEmail) {
        // 채팅방에 참여한 사용자 중에서 자신의 이메일을 제외한 다른 사용자를 찾아 신고 처리
        chatUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String userEmail = userSnapshot.getValue(String.class);
                    if (userEmail != null && !userEmail.equals(currentUserEmail)) {
                        // 다른 사용자를 찾았으면 해당 사용자를 신고 처리
                        performReport(userEmail);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 에러 처리
            }
        });
    }

    private void performReport(String reportedUserEmail) {
        // 사용자를 신고 처리하는 코드 추가
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("UserAccount");

        userRef.orderByChild("email").equalTo(reportedUserEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Integer currentReportCount = userSnapshot.child("reportCount").getValue(Integer.class);
                        if (currentReportCount == null) {
                            currentReportCount = 0;
                        }

                        userSnapshot.getRef().child("reportCount").setValue(currentReportCount + 1);

                        // 신고 횟수가 2번 이상이면 상태를 차단으로 변경
                        if (currentReportCount + 1 >= 2) {
                            userSnapshot.getRef().child("status").setValue("blocked");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 에러 처리
            }
        });
    }
}

