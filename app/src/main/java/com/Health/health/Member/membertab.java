package com.Health.health.Member;

import android.content.Context;
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
    EditText weighttext,fattext,muscletext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

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
                break;
            case 2:
                view = inflater.inflate(R.layout.list,frameLayout,false);

                MaterialCalendarView calendarView=view.findViewById(R.id.calendar);
                calendarView.state().edit()
                        .setFirstDayOfWeek(DayOfWeek.SUNDAY)
                        .setMaximumDate(CalendarDay.from(LocalDate.now().getYear(),LocalDate.now().getMonth().getValue(),LocalDate.now().getDayOfMonth()))
                        .setCalendarDisplayMode(CalendarMode.WEEKS)
                        .commit();
                weighttext=view.findViewById(R.id.inputtextedit1);
                fattext=view.findViewById(R.id.inputtextedit2);
                muscletext=view.findViewById(R.id.inputtextedit3);

                calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, CalendarDay date, boolean selected) {
                        if (selected==false){
                            weighttext.setClickable(false);
                            fattext.setClickable(false);
                            muscletext.setClickable(false);
                            Toast.makeText(membertab.this,"입력할 날짜를 선택해주세요.",Toast.LENGTH_SHORT).show();
                        }
                        int mYear = date.getYear();
                        int mMonth = date.getMonth();
                        int mDay = date.getDay();
                    }
                });

                weighttext.addTextChangedListener(textWatcher);
                fattext.addTextChangedListener(textWatcher);
                muscletext.addTextChangedListener(textWatcher);

                Log.e("WEIGHT",WEIGHT);
                Log.e("FAT",FAT);
                Log.e("MUSCLE",MUSCLE);
        }
        if(view !=null){
            frameLayout.addView(view);
        }
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            WEIGHT = weighttext.getText().toString();
            FAT = fattext.getText().toString();
            MUSCLE = muscletext.getText().toString();

            Log.e("WEIGHT", WEIGHT);
            Log.e("FAT", FAT);
            Log.e("MUSCLE", MUSCLE);
        }

        @Override
        public void afterTextChanged(Editable s) {
            WEIGHT=weighttext.getText().toString();
            FAT=fattext.getText().toString();
            MUSCLE=muscletext.getText().toString();
            HashMap hashMap = new HashMap();
            hashMap.put("weight",WEIGHT);
            hashMap.put("fat",FAT);
            hashMap.put("muscle",MUSCLE);

            String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase database =FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Users");
            reference.child(uid).child("User_data").setValue(hashMap);
        }
    };

}