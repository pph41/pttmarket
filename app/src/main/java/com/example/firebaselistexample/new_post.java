package com.example.firebaselistexample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class new_post extends AppCompatActivity {
    //파베 db연결
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    //연결준비 끝
    private int GALLEY_CODE = 10;

    private String imageUrl="";
    ImageView ivProfile;
    Button input_profile,upload_post;
    EditText input_price,input_product;

    private void Listener(){
        upload_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImg(imageUrl);
            }
        });
        //사진 선택
        input_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

                startActivityForResult(intent,GALLEY_CODE);
            }
        });
    }

    private void uploadImg(String uri){
        try {
            StorageReference storageRef = storage.getReference();

            Uri file = Uri.fromFile(new File(uri));
            final StorageReference reversRef = storageRef.child("images/"+file.getLastPathSegment());
            UploadTask uploadTask = reversRef.putFile(file);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return reversRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(new_post.this,"업로드 성공",Toast.LENGTH_SHORT).show();
                        @SuppressWarnings("VisibleForTests")
                        Uri downloadUri = task.getResult();

                        User User = new User();

                        User.setProfile(downloadUri.toString());
                        User.setPrice(input_price.getText().toString());
                        User.setProduct(input_product.getText().toString());

                        database.getReference().child("User").push().setValue(User);

                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }else {}
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_post);

        ivProfile = findViewById(R.id.iv_Profile);
        input_profile = findViewById(R.id.input_profile);
        upload_post = findViewById(R.id.upload_post);
        input_product = findViewById(R.id.input_product);
        input_price = findViewById(R.id.input_price);

        Listener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        if (requestCode == GALLEY_CODE){

            imageUrl = getRealPathFromUri(data.getData());
            RequestOptions cropOptions = new RequestOptions();
            Glide.with(getApplicationContext())
                    .load(imageUrl)
                    .apply(cropOptions.optionalCircleCrop())
                    .into(ivProfile);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getRealPathFromUri(Uri uri){

        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();

        int columnlndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String url = cursor.getString(columnlndex);
        cursor.close();

        return url;
    }

}