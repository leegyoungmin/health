package com.Health.health.Member;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.Health.health.Camera.FoodCamera;
import com.Health.health.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.YearMonth;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static android.content.ContentValues.TAG;


public class FoodActivity extends AppCompatActivity {
    private static final int CAMERA_PERM_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final int GALLERY_REQUEST_CODE = 105;

    final String[] dialogMessage = new String[]{"카메라로 찍기","갤러리에서 선택하기"};

    String title;
    Button save;
    TextView real_title;
    private String currentPhotoPath;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        title =getIntent().getStringExtra("title");
        real_title=findViewById(R.id.title);
        real_title.setText(title);

        Button save=(Button)findViewById(R.id.save);

        imageView = findViewById(R.id.food_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(FoodActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
                        .setItems(dialogMessage, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0){
                                    askCameraPermission();
                                }
                                else if(i==1){
                                    Intent gallay = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(gallay,GALLERY_REQUEST_CODE);
                                }
                            }
                        });
                dialog.show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(FoodActivity.this,membertab.class);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    public void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA},CAMERA_PERM_CODE);
        }else{
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==CAMERA_PERM_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else{
                Toast.makeText(this,"Camera Permission is Required to Use camera.",Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST_CODE){
            if(resultCode== Activity.RESULT_OK){
                File f=new File(currentPhotoPath);

                //selectedImage.setImageURI(Uri.fromFile(f));
                Log.d(TAG,"Absolute Url of Image is "+ Uri.fromFile(f));
                Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri=Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                Log.e("name",f.getName());

//
                //Intent getIntent=getIntent();
                //String userCoreId=getIntent.getStringExtra("uid");
                //Log.e("usercoreid",userCoreId);

                //URI저장
                //firebaseDatabase.getReference().child("Users").child(userCoreId).setValue(contentUri);

                uploadImageToFirebase(f.getName(),contentUri);

            }
        }
        if(requestCode==GALLERY_REQUEST_CODE){
            if(resultCode== Activity.RESULT_OK){

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                Uri contentUri=data.getData();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String imageFileName="food"+uid+"."+getFileExt(contentUri);
                Log.d(TAG,"onActivityResult: Gallery Image Uri:  "+imageFileName);
                //selectedImage.setImageURI(contentUri);
//
                //Intent getIntent=getIntent();
                //String userCoreId=getIntent.getStringExtra("uid");
                //파일명저장
                //firebaseDatabase.getReference().child("Users").child(userCoreId).setValue(imageFileName);

                Log.e("example_1",LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
                uploadImageToFirebase(imageFileName,contentUri);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                database.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("foodPhoto").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        Log.e("foodPhoto",snapshot.getValue().toString());

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        }

    }

    public void uploadImageToFirebase(String name, Uri contentUri) {

        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(currentuser,"checking");
        StorageReference image = FirebaseStorage.getInstance().getReference().child("food/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageView);
                        Log.e("URI", String.valueOf(uri));
                        Log.d("tag","onSuccess: Uploaded Image URI is"+uri.toString());
                        Log.e("real_title",real_title.getText().toString());
                        if(title.equals("아침 식사")){
                            FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(currentuser)
                                    .child("foodPhoto")
                                    .child(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                                    .child("1")
                                    .setValue(uri.toString());
                        }
                        else if(title.equals("점심 식사")){
                            FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(currentuser)
                                    .child("foodPhoto")
                                    .child(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                                    .child("2")
                                    .setValue(uri.toString());
                        }
                        else if(title.equals("저녁 식사")){
                            FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(currentuser)
                                    .child("foodPhoto")
                                    .child(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                                    .child("3")
                                    .setValue(uri.toString());
                        }
                        else{
                            Toast.makeText(FoodActivity.this,"예기치 못한 오류가 발생하였습니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.e("에러",e.toString());
            }
        });
    }



    public String getFileExt(Uri contentUri) {
        ContentResolver c=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    public File createImageFile() throws IOException {
        String timeStamp=LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String imageFileName="TEST_"+timeStamp+"_";
        File storageDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //겔러리 저장 안할 경우
        //File storageDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );


        currentPhotoPath=image.getAbsolutePath();
        return image;
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
}