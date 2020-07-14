package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

public class CalenderMemo extends AppCompatActivity {
    private Button btn_calender;
    private Button btn_modify;
    private Button btn_delete;
//    private String got_memo ="없음";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_memo);
        MemoDecorator memoDecorator = new MemoDecorator();

        final Intent intent = getIntent();


        TextView year = (TextView) findViewById(R.id.year);
        TextView month = (TextView) findViewById(R.id.month);
        TextView day = (TextView) findViewById(R.id.day);
        TextView memo = (TextView) findViewById(R.id.memo);

        final String y = intent.getStringExtra("year");
        final String m = intent.getStringExtra("month");
        final String d = intent.getStringExtra("day");

        final String date = y+" "+m+" "+d;

        final CalenderDatabase db = Room.databaseBuilder(this, CalenderDatabase.class, "calendar_memo-db")
                .allowMainThreadQueries()
                .build();

        MemoSource debug = db.memoSourceDao().findByDate(date);
        if (debug != null) {
            memo.setText(debug.getMemo());
        }

        year.setText(y+"년");
        month.setText(m+"월");
        day.setText(d+"일");

        btn_calender = (Button) findViewById(R.id.btn_calender);
        btn_modify = (Button) findViewById(R.id.btn_modify);
        btn_delete = (Button) findViewById(R.id.btn_delete);

        //삭제 버튼
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemoSource check = db.memoSourceDao().findByDate(date);
                if(check == null){
                    Toast.makeText(getApplicationContext(), "삭제할 것이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    //메모 삭제
                    db.memoSourceDao().delete(check);


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

                    CalenderFrag.thisDay = CalendarDay.from(Integer.parseInt(y), Integer.parseInt(m)-1, Integer.parseInt(d));
                    CalenderFrag.thisDayDecorator = new ThisDayDecorator(CalenderFrag.thisDay);

                    CalenderFrag.materialCalendarView.clearSelection();
                    CalenderFrag.materialCalendarView.addDecorators(CalenderFrag.thisDayDecorator);

                    //토스트 띄우기
                    Toast.makeText(getApplicationContext(), "삭제 되었습니다.", Toast.LENGTH_SHORT).show();

                    //액티비티 종료
                    finish();
                }
            }
        });

        //수정 버튼
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemoSource check = db.memoSourceDao().findByDate(date);
                if(check == null){
                    Toast.makeText(getApplicationContext(), "수정할 것이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent2 = new Intent(CalenderMemo.this, EditCalenderMemo.class);
                    intent2.putExtra("year", y);
                    intent2.putExtra("month", m);
                    intent2.putExtra("day", d);
                    intent2.putExtra("hasBeen","Yes");
                    startActivity(intent2);
//                    Toast.makeText(getApplicationContext(), "수정 되었습니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        //추가 버튼
        btn_calender.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MemoSource check = db.memoSourceDao().findByDate(date);
                if(check == null){
                    Intent intent1 = new Intent(CalenderMemo.this, EditCalenderMemo.class);
                    intent1.putExtra("year", y);
                    intent1.putExtra("month", m);
                    intent1.putExtra("day", d);
                    intent1.putExtra("hasBeen","No");
                    startActivity(intent1);

                    //액티비티 종료
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "수정버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
