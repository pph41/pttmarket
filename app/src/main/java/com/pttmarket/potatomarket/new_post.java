package com.pttmarket.potatomarket;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pttmarket.potatomarket.R;

import java.io.File;

public class new_post extends AppCompatActivity {
    //파베 db연결
    private final DatabaseReference root = FirebaseDatabase.getInstance().getReference("User");
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();
    private final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("UserAccount");
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private Uri imageUri;

    //연결준비 끝
    private int GALLEY_CODE = 10;
    private ImageView imageView;

    Button input_profile;
    EditText input_price,input_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_post);

        Button input_profile = findViewById(R.id.input_profile);
        Button upload_post = findViewById(R.id.upload_post);
        imageView = findViewById(R.id.iv_Profile);
        input_price = findViewById(R.id.input_price);
        input_product = findViewById(R.id.input_product);
        input_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/");
                activityResult.launch(galleryIntent);
            }
        });
        upload_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null){
                    uploadTOFirebase(imageUri);
                } else {
                    Toast.makeText(new_post.this, "사진을 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode()==RESULT_OK && result.getData() != null){
                        imageUri = result.getData().getData();
                        imageView.setImageURI(imageUri);
                    }
                }
            });

    private void uploadTOFirebase(Uri uri){
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String email = currentUser.getEmail();
                        int idx = email.indexOf("@");
                        String name = email.substring(0,idx);

                        User user = new User(uri.toString(), input_product.getText().toString(), input_price.getText().toString(), name, currentUser.getEmail());
                        //user.setEmailid(currentUser.getEmail()); // 추가
                        String modelld = root.push().getKey();
                        root.child(modelld).setValue(user);

                        Toast.makeText(new_post.this,"업로드성공",Toast.LENGTH_SHORT);

                        imageView.setImageResource(R.drawable.ic_launcher_background);

                        Intent intent = new Intent(getApplicationContext(), boardActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}