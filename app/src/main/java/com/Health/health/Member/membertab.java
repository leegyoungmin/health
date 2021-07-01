package com.Health.health.Member;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
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
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.Health.health.Camera.CameraBeta;
import com.Health.health.Camera.FoodCamera;
import com.Health.health.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.lang.ref.Reference;
import java.util.HashMap;

public class membertab extends AppCompatActivity{
    String WEIGHT = new String();
    String FAT=new String();
    String MUSCLE=new String();
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
                view =inflater.inflate(R.layout.list_2,frameLayout,false);
                MaterialCalendarView calendarView_2=view.findViewById(R.id.calendar);
                calendarView_2.state().edit()
                        .setFirstDayOfWeek(DayOfWeek.SUNDAY)
                        .setMaximumDate(CalendarDay.from(LocalDate.now().getYear(),LocalDate.now().getMonth().getValue(),LocalDate.now().getDayOfMonth()))
                        .setCalendarDisplayMode(CalendarMode.MONTHS)
                        .commit();
                break;
            case 2:
                break;
            case 3:
                view = inflater.inflate(R.layout.list,frameLayout,false);

                MaterialCalendarView calendarView=view.findViewById(R.id.calendar);
                calendarView.state().edit()
                        .setFirstDayOfWeek(DayOfWeek.SUNDAY)
                        .setMaximumDate(CalendarDay.from(LocalDate.now().getYear(),LocalDate.now().getMonth().getValue(),LocalDate.now().getDayOfMonth()))
                        .setCalendarDisplayMode(CalendarMode.WEEKS)
                        .commit();
                calendarView.setDateSelected(CalendarDay.today(),true);

                calendarView.setOnDateChangedListener(new OnDateSelectedListener() {

                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, CalendarDay date, boolean selected) {
                        Log.e("selected", String.valueOf(selected));

                        int mYear = date.getYear();
                        int mMonth = date.getMonth();
                        int mDay = date.getDay();

                    }
                });
                view.findViewById(R.id.food1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(membertab.this, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth);
                        dialog.setTitle("아침 식사").setItems(dialogitem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    int REQUEST_CODE = 200;
                                    Intent intent = new Intent(membertab.this, FoodCamera.class);
                                    intent.putExtra("camera", which);
                                    startActivityForResult(intent,200);
                                } else {
                                    int REQUEST_CODE = 200;
                                    Intent intent = new Intent(membertab.this, FoodCamera.class);
                                    intent.putExtra("camera", which);
                                    startActivityForResult(intent,200);
                                }
                            }
                        });
                        dialog.setCancelable(false);
                        dialog.show();
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
        if(resultcode== Activity.RESULT_OK){
            switch (RequestCode){
                case 200:

            }
        }
    }

}