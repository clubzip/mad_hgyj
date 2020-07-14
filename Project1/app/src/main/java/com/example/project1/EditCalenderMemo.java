package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

public class EditCalenderMemo extends AppCompatActivity {
    private EditText edit_text;
    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_calender_memo);



        final Intent intent = getIntent();
        final String year = intent.getStringExtra("year");
        final String month = intent.getStringExtra("month");
        final String day = intent.getStringExtra("day");
        final String hasBeen = intent.getStringExtra("hasBeen");

        final String date = year+" " + month+" " + day;

        edit_text = (EditText) findViewById(R.id.edit_text);
        btn_add = (Button) findViewById(R.id.btn_add);

        final CalenderDatabase db = Room.databaseBuilder(this, CalenderDatabase.class, "calendar_memo-db")
                .allowMainThreadQueries()
                .build();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasBeen.equals("No")) {
                    //db에 메모 추가됨
                    db.memoSourceDao().insert(new MemoSource(year, month, day, edit_text.getText().toString()));

                    //다시 데코 리셋하는 과정 - CalenderFrag.java 에서 정보를 가져옴
                    //데코 초기화
                    CalenderFrag.materialCalendarView.removeDecorators();

                    //db에 저장된 메모된 날짜 불러오기
                    CalenderFrag.memoSourceList = db.memoSourceDao().getAll();

                    //메모된 날짜 확인하여 리스트 만듬
                    if (CalenderFrag.memoSourceList != null) {
                        List<CalendarDay> memoDays = new ArrayList<>();

                        for(int i = 0; i< CalenderFrag.memoSourceList.size(); i++){
                            MemoSource temp = CalenderFrag.memoSourceList.get(i);
                            String dateWithSpace = temp.getDate();
                            String[] YMD = dateWithSpace.split(" ");
                            int int1 = Integer.parseInt(YMD[0]);
                            int int2 = Integer.parseInt(YMD[1]);
                            int int3 = Integer.parseInt(YMD[2]);
                            memoDays.add(CalendarDay.from(int1+1-1, int2-1, int3+1-1));
                        }
                        //메모된 날짜를 표시할 데코레이터 형성
                        CalenderFrag.memoDecorator = new MemoDecorator(memoDays);
                    } else{
                        //메모된 날짜 없으면 그냥 아무것도 데코하지 않음
                        CalenderFrag.memoDecorator = new MemoDecorator(new ArrayList<CalendarDay>());
                    }

                    //데코 적용
                    CalenderFrag.materialCalendarView.addDecorators(
                            new SundayDecorator(),
                            new SaturdayDecorator(),
                            CalenderFrag.oneDayDecorator,
                            CalenderFrag.memoDecorator);

                    CalenderFrag.thisDay = CalendarDay.from(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
                    CalenderFrag.thisDayDecorator = new ThisDayDecorator(CalenderFrag.thisDay);

                    CalenderFrag.materialCalendarView.clearSelection();
                    CalenderFrag.materialCalendarView.addDecorators(CalenderFrag.thisDayDecorator);

                    //토스트 띄우기
                    Toast.makeText(getApplicationContext(), "일정이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    MemoSource memoSource = db.memoSourceDao().findByDate(date);
                    memoSource.setMemo(edit_text.getText().toString());
                    db.memoSourceDao().update(memoSource);
                    //토스트 띄우기
                    Toast.makeText(getApplicationContext(), "일정이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                }
                edit_text.setText("");
                finish();
            }
        });
    }


}
