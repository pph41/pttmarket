package com.pttmarket.potatomarket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmailAuthActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef; //리얼타임 db
    private Button mBtnEAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailauth);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


        // xml의 input들을 변수로 받아온다.

        mBtnEAuth = findViewById(R.id.btn_eauth);

        //회원가입버튼 이벤트 리스너
        mBtnEAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                firebaseUser.sendEmailVerification().addOnCompleteListener(EmailAuthActivity.this, (OnCompleteListener<Void>) task -> {
                    if (task.isSuccessful() ) {
                        Toast.makeText ( getApplicationContext (), "확인 이메일 전송 대상: " + firebaseUser.getEmail(), Toast.LENGTH_SHORT ).show();
                    }else{
                        Toast.makeText(EmailAuthActivity.this,"유효한 이메일이 아닙니다.", Toast.LENGTH_SHORT).show();
                    }

                });


            }
        });
    }
}



