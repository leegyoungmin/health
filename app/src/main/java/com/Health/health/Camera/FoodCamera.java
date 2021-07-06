package com.Health.health.Camera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.Health.health.Member.membertab;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class FoodCamera extends AppCompatActivity {
    private static final int CAMERA_PERM_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final int GALLERY_REQUEST_CODE = 105;
    private ImageView selectedImage;
    private Button cameraBtn,saveBtn;
    private String currentPhotoPath;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodcamera);

        int camera=getIntent().getIntExtra("camera",0);
        Log.e("camera", String.valueOf(camera));
        if(camera==0){
            askCameraPermission();
        }
        else{
            //Toast.makeText(MainActivity.this,"Gallery Btn is Clicked.",Toast.LENGTH_SHORT).show();
            Intent gallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery,GALLERY_REQUEST_CODE);
        }

        selectedImage=findViewById(R.id.image);
        cameraBtn=findViewById(R.id.camera);
        saveBtn=findViewById(R.id.save);


        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"Camera Btn is Clicked.",Toast.LENGTH_SHORT).show();
                askCameraPermission();      //카메라 권한 여부
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });




    }
    //카메라 권한 여부
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
                Log.d(TAG,"Absolute Url of Image is "+Uri.fromFile(f));
                Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri=Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
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

                DatabaseReference reference = firebaseDatabase.getReference();

                Uri contentUri=data.getData();
                selectedImage.setImageURI(contentUri);
                String timeStamp=new SimpleDateFormat("yyyyMMdd").format(new Date());
                String imageFileName="food"+timeStamp+"."+getFileExt(contentUri);
                Log.d(TAG,"onActivityResult: Gallery Image Uri:  "+imageFileName);
                //selectedImage.setImageURI(contentUri);
//
                //Intent getIntent=getIntent();
                //String userCoreId=getIntent.getStringExtra("uid");
                //파일명저장
                //firebaseDatabase.getReference().child("Users").child(userCoreId).setValue(imageFileName);
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

        String currentuser = mAuth.getCurrentUser().getUid();
        Log.d(currentuser,"checking");
        StorageReference image=storageReference.child("pictures/"+name);

        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.e("URI", String.valueOf(uri));
                        Log.d("tag","onSuccess: Uploaded Image URI is"+uri.toString());
                        Picasso.get().load(uri).into(selectedImage);
                        firebaseDatabase
                                .getReference("Users")
                                .child(currentuser)
                                .child("foodPhoto")
                                .setValue(uri.toString());
                    }
                });
                Toast.makeText(FoodCamera.this,"식단이 저장되었습니다.",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(FoodCamera.this,"식단을 저장하는데 실패했습니다.",Toast.LENGTH_SHORT).show();
            }
        });
    }



    public String getFileExt(Uri contentUri) {
        ContentResolver c=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    public File createImageFile() throws IOException {
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(this.getClass().getName(),"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(this.getClass().getName(),"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(this.getClass().getName(),"onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(this.getClass().getName(),"onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(this.getClass().getName(),"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(this.getClass().getName(),"onDestroy");
    }

}