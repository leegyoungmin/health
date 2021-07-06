package com.Health.health.Member;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.Health.health.Camera.CameraBeta;
import com.Health.health.Camera.FoodCamera;
import com.Health.health.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.Reference;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class membertab extends AppCompatActivity{
    String WEIGHT = new String();
    String FAT=new String();
    String MUSCLE=new String();
    String selectedDay;
    ImageButton imageButton1,imageButton2,imageButton3;
    final CharSequence[] dialogitem={"카메라로 찍기","갤러리에서 사진 가져오기"};
//    Boolean calendar_state;
//    EditText weighttext,fattext,muscletext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        if(savedInstanceState !=null){
            onResume();
        }
        changeView(0);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabhost);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeView(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void changeView(int index){
        LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.framelayout1);
        if(frameLayout.getChildCount()>0){
            frameLayout.removeViewAt(0);
        }

        View view=null;
        switch (index){
            case 0:
                break;
            case 1:
            case 2:
                break;
            case 3:

                view = inflater.inflate(R.layout.list,frameLayout,false);
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child();
                MaterialCalendarView calendarView=view.findViewById(R.id.calendar);
                calendarView.state().edit()
                        .setFirstDayOfWeek(DayOfWeek.SUNDAY)
                        .setMaximumDate(CalendarDay.from(LocalDate.now().getYear(),LocalDate.now().getMonth().getValue(),LocalDate.now().getDayOfMonth()))
                        .setCalendarDisplayMode(CalendarMode.WEEKS)
                        .commit();
                calendarView.setDateSelected(CalendarDay.today(),true);
                selectedDay=CalendarDay.today().getYear()+"-"+CalendarDay.today().getMonth()+"-"+CalendarDay.today().getDay();
                Log.e("selected_day",selectedDay);

                calendarView.setOnDateChangedListener(new OnDateSelectedListener() {

                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, CalendarDay date, boolean selected) {
                        Log.e("selected", String.valueOf(selected));

                        int mYear = date.getYear();
                        int mMonth = date.getMonth();
                        int mDay = date.getDay();
                        selectedDay=mYear+"-"+mMonth+"-"+mDay;
                        Log.e("selected_day",selectedDay);

                    }
                });
                String[] title = new String[]{"아침 식사","점심 식사","저녁 식사"};
                imageButton1=view.findViewById(R.id.food1);
                imageButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent food = new Intent(membertab.this,FoodActivity.class);
                        food.putExtra("title",title[0]);
                        startActivityForResult(food,200);
                    }
                });
                imageButton2=view.findViewById(R.id.food2);
                imageButton2.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent food = new Intent(membertab.this,FoodActivity.class);
                        food.putExtra("title",title[1]);
                        startActivity(food);
                    }
                });
                imageButton3=view.findViewById(R.id.food3);
                imageButton3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent food = new Intent(membertab.this,FoodActivity.class);
                        food.putExtra("title",title[2]);
                        startActivity(food);
                    }
                });

                Log.e("WEIGHT",WEIGHT);
                Log.e("FAT",FAT);
                Log.e("MUSCLE",MUSCLE);
        }
        if(view !=null){
            frameLayout.addView(view);
        }
    }
    @Override
    public void onActivityResult(int RequestCode,int resultcode,Intent data){
        super.onActivityResult(RequestCode,resultcode,data);
        if(resultcode== Activity.RESULT_OK) {
            switch (RequestCode) {
                case 200:
//                    DatabaseReference mUri = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("foodPhotoe").child(selectedDay).child("1");
//                    loadImageTask imageTask = new loadImageTask(mUri.toString());
//                    imageButton1.setImageBitmap(imageTask.doInBackground());
                    break;
            }
        }
    }

//    public class loadImageTask extends AsyncTask<Bitmap,Void, Bitmap>{
//        private String url;
//        public loadImageTask(String url){
//            this.url=url;
//        }
//
//        @Override
//        protected Bitmap doInBackground(Bitmap... bitmaps) {
//            Bitmap imgBitmap = null;
//            try{
//                URL url1 = new URL(url);
//                URLConnection connection = url1.openConnection();
//                connection.connect();
//                int nSize = connection.getContentLength();
//                BufferedInputStream bis = new BufferedInputStream(connection.getInputStream(),nSize);
//                imgBitmap = BitmapFactory.decodeStream(bis);
//                bis.close();
//            }
//            catch (IOException e){
//                e.printStackTrace();
//            }
//            return imgBitmap;
//        }
//    }
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