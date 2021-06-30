package com.Health.health.Member;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import org.threeten.bp.LocalDate;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.Health.health.R;
import com.google.android.material.tabs.TabLayout;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class membertab extends AppCompatActivity implements View.OnClickListener{
    static final String[] LIST_MY={"LIST1","LIST2","LIST3"};
    static final String[] LIST_CHAT={"CHAT1","CHAT2","CHAT3"};
    String date;
    MaterialCalendarView materialCalendarView;
    CalendarDay calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membertab);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("신체 변화");
        changeView(0);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("tab_position", String.valueOf(tab.getPosition()));
                String title = tab.getText().toString();
                actionBar.setTitle(title);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
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
        ScrollView scrollView1 =(ScrollView)findViewById(R.id.scrollView1);

        switch (index){
            case 0:
                ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1,LIST_MY);
                listView1.setAdapter(adapter1);
                listView1.setVisibility(View.VISIBLE);
                listView2.setVisibility(View.GONE);
                scrollView1.setVisibility(View.GONE);

                break;
            case 1:
                ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1,LIST_CHAT);
                listView2.setAdapter(adapter2);
                listView2.setVisibility(View.VISIBLE);
                listView1.setVisibility(View.GONE);
                scrollView1.setVisibility(View.GONE);

                break;
            case 2:
                int mYear=LocalDate.now().getYear();
                int mMonth = LocalDate.now().getMonth().getValue();
                int mDay=LocalDate.now().getDayOfMonth();
                listView1.setVisibility(View.GONE);
                listView2.setVisibility(View.GONE);
                scrollView1.setVisibility(View.VISIBLE);
                materialCalendarView=(MaterialCalendarView) scrollView1.findViewById(R.id.calendar);
                materialCalendarView.state().edit()
                        .setCalendarDisplayMode(CalendarMode.WEEKS)
                        .setMaximumDate(CalendarDay.from(mYear,mMonth,mDay)).commit();
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


        }
    }
}