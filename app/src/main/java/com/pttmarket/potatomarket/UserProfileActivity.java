package com.pttmarket.potatomarket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class UserProfileActivity extends AppCompatActivity {
    private Uri imageUri;
    private ImageView imageView;
    private FirebaseStorage storage;
    private String email;
    private final int GALLERY_CODE = 10;
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        storage = FirebaseStorage.getInstance();
        imageView = findViewById(R.id.schedule);
        Button upload_schedule = findViewById(R.id.myScheduleButton);
        email = currentUser.getEmail();
        TextView my_id = findViewById(R.id.my_id);
        TextView my_email = findViewById(R.id.my_email);

        String email = currentUser.getEmail();
        int idx = email.indexOf("@");
        String name = email.substring(0, idx);
        my_id.setText(name);
        my_email.setText(email);

        FirebaseStorage storage_schedule = FirebaseStorage.getInstance();
        StorageReference storageRef = storage_schedule.getReference();
        storageRef.child("" + email).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "시간표를 등록해주세요!", Toast.LENGTH_SHORT).show();
            }
        });
        upload_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLERY_CODE);
            }
        });


        // 팔아요 버튼 클릭 시
        Button sellButton = findViewById(R.id.sellButton);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this, boardActivity.class));
            }
        });

        // 채팅 버튼 클릭 시
        Button chatButton = findViewById(R.id.chatButton);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this, ChatListActivity.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE) {
            if (data != null && data.getData() != null) {
                Uri file = data.getData();
                StorageReference storageRef = storage.getReference();
                StorageReference riversRef = storageRef.child(email);

                UploadTask uploadTask = riversRef.putFile(file);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // 업로드 성공
                        Toast.makeText(UserProfileActivity.this, "이미지 업로드 성공", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 업로드 실패
                        Toast.makeText(UserProfileActivity.this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "이미지를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
