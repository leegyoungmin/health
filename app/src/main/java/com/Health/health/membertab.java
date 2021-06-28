package com.Health.health;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class membertab extends AppCompatActivity {
    static final String[] LIST_MY={"LIST1","LIST2","LIST3"};
    static final String[] LIST_CHAT={"CHAT1","CHAT2","CHAT3"};
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membertab);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("신체 변화");
        changeView(0);


        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("tab_position", String.valueOf(tab.getPosition()));
                String title = tab.getText().toString();
                actionBar.setTitle(title);
                actionBar.setDisplayShowHomeEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
                int pos=tab.getPosition();
                changeView(pos);
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
        ListView listView1 = (ListView)findViewById(R.id.listview1);
        ListView listView2 = (ListView)findViewById(R.id.listview2);
        TextView textView = (TextView)findViewById(R.id.textView);
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.constrate1);
        TextView datepicker=(TextView)findViewById(R.id.datepicker);
        TextInputLayout textInputLayout1 = (TextInputLayout)findViewById(R.id.textinput1);
        TextInputLayout textInputLayout2 = (TextInputLayout)findViewById(R.id.textinput2);
        TextInputLayout textInputLayout3 = (TextInputLayout)findViewById(R.id.textinput3);
        EditText weight = (EditText)findViewById(R.id.weight);
        EditText fat_rate = (EditText) findViewById(R.id.fatrate);
        EditText muscle_mass =(EditText)findViewById(R.id.musclemass);
        switch (index){
            case 0:
                ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1,LIST_MY);
                listView1.setAdapter(adapter1);
                listView1.setVisibility(View.VISIBLE);
                listView2.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                datepicker.setVisibility(View.GONE);
                textInputLayout1.setVisibility(View.GONE);
                textInputLayout2.setVisibility(View.GONE);
                break;
            case 1:
                ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1,LIST_CHAT);
                listView2.setAdapter(adapter2);
                listView2.setVisibility(View.VISIBLE);
                listView1.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                datepicker.setVisibility(View.GONE);
                textInputLayout1.setVisibility(View.GONE);
                textInputLayout2.setVisibility(View.GONE);
                break;
            case 2:
                listView1.setVisibility(View.GONE);
                listView2.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                datepicker.setVisibility(View.VISIBLE);
                textInputLayout1.setVisibility(View.VISIBLE);
                textInputLayout2.setVisibility(View.VISIBLE);
                textInputLayout3.setVisibility(View.VISIBLE);
                
                Calendar c=Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog =new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datepicker.setText(year+"년"+(month+1)+"월"+dayOfMonth+"일");
                        date=datepicker.getText().toString();
                        Log.e("date",date);
                    }
                },mYear,mMonth,mDay);
                weight.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String weight=s.toString();
                        Log.e("user_weight",weight);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                fat_rate.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String fat_rate = s.toString();
                        Log.e("user_fatrate",fat_rate);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                muscle_mass.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String muscle_mass =s.toString();
                        Log.e("muscle_mass",muscle_mass);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                datepicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(datepicker .isClickable()){
                            datePickerDialog.show();
                        }
                    }
                });
                break;
        }
    }
}