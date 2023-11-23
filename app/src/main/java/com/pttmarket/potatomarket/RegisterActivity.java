package com.pttmarket.potatomarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef; //리얼타임 db
    private EditText mEtEmail, mEtPwd, mEtNname; //가입.xml input값
    private Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


        // xml의 input들을 변수로 받아온다.
        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);
        mEtNname = findViewById(R.id.et_nname);
        mBtnRegister = findViewById(R.id.btn_register);

        //회원가입버튼 이벤트 리스너
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // 회원가입 처리, 학교이메일 강제 도메인 처리
                String strEmail = mEtEmail.getText().toString();//+"@gwnu.ac.kr";
                String strPwd = mEtPwd.getText().toString();
                String strNname = mEtNname.getText().toString();
                //파베 인증

                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task){
                       if (task.isSuccessful()) {
                           FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                           UserAccount account = new UserAccount();
                           account.setIdToken(firebaseUser.getUid());
                           account.setEmailId(firebaseUser.getEmail());
                           account.setPassword(strPwd);
                           account.setNickname(strNname);
                           //.se
                           //db에 계정등록, Uid 링크

                           mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                           // 작동 시 메시지 출력
                           /* 이메일 인증 구현 부분, 추후 학교 이메일로 인증가능 확인 후 추가 예정
                           Toast.makeText(RegisterActivity.this, "이메일 인증링크를 확인해 주세요", Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(RegisterActivity.this, EmailAuthActivity.class);
                            */
                           Toast.makeText(RegisterActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                           startActivity(intent);

                       }else {
                           Toast.makeText(RegisterActivity.this,"회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                       }
                   }

                });


            }
        });
    }
}