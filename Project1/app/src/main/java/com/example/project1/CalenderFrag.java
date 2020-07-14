package com.example.project1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalenderFrag extends Fragment {
    private View view;

    //날짜 꾸미는 데코레이터
    public static OneDayDecorator oneDayDecorator = new OneDayDecorator();
    public static ThisDayDecorator thisDayDecorator = new ThisDayDecorator();
    public static CalendarDay thisDay = CalendarDay.today();
    public static MemoDecorator memoDecorator = new MemoDecorator();
    public static List<MemoSource> memoSourceList = new ArrayList<>();
    public static MaterialCalendarView materialCalendarView;

    public static CalenderFrag newInstance() {
        CalenderFrag calenderFrag = new CalenderFrag();
        return calenderFrag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calender, container, false);
        materialCalendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);

        //db 불러오기 및 생성
        final CalenderDatabase db = Room.databaseBuilder(getActivity(), CalenderDatabase.class, "calendar_memo-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        //db에 저장된 메모된 날짜 불러오기
        memoSourceList = db.memoSourceDao().getAll();

        //메모된 날짜 확인하여 리스트 만듬
        if (memoSourceList != null) {
            List<CalendarDay> memoDays = new ArrayList<>();

            for(int i = 0; i<memoSourceList.size(); i++){
                MemoSource temp = memoSourceList.get(i);
                String dateWithSpace = temp.getDate();
                String[] YMD = dateWithSpace.split(" ");
                int int1 = Integer.parseInt(YMD[0]);
                int int2 = Integer.parseInt(YMD[1]);
                int int3 = Integer.parseInt(YMD[2]);
                memoDays.add(CalendarDay.from(int1+1-1, int2-1, int3+1-1));
            }
            //메모된 날짜를 표시할 데코레이터 형성
            memoDecorator = new MemoDecorator(memoDays);
        } else{
            //메모된 날짜 없으면 그냥 아무것도 데코하지 않음
            memoDecorator = new MemoDecorator(new ArrayList<CalendarDay>());
        }

        //달력 형성
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        //decorate 추가
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator,
                memoDecorator);

        final String[] result = {"2017,03,18","2017,04,18","2017,05,18","2017,06,18"};

//        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //날짜 받아오기
                int year = date.getYear();
                int month = date.getMonth();
                int day = date.getDay();

                //데코 초기화
                materialCalendarView.removeDecorators();

                //db에 저장된 메모된 날짜 불러오기
                memoSourceList = db.memoSourceDao().getAll();

                //메모된 날짜 체크하기
                if (memoSourceList != null) {
                    List<CalendarDay> memoDays = new ArrayList<>();

                    for(int i = 0; i<memoSourceList.size();i++){
                        MemoSource temp = memoSourceList.get(i);
                        String dateWithSpace = temp.getDate();
                        String[] YMD = dateWithSpace.split(" ");
                        int int1 = Integer.parseInt(YMD[0]);
                        int int2 = Integer.parseInt(YMD[1]);
                        int int3 = Integer.parseInt(YMD[2]);
                        memoDays.add(CalendarDay.from(int1+1-1, int2-1, int3+1-1));
                    }
                    //메모된 날짜 데코 형성
                    memoDecorator = new MemoDecorator(memoDays);
                } else{
                    memoDecorator = new MemoDecorator(new ArrayList<CalendarDay>());
                }

                //데코 적용
                materialCalendarView.addDecorators(
                        new SundayDecorator(),
                        new SaturdayDecorator(),
                        oneDayDecorator,
                        memoDecorator);

                thisDay = CalendarDay.from(year, month, day);
                thisDayDecorator = new ThisDayDecorator(thisDay);

                materialCalendarView.clearSelection();
                materialCalendarView.addDecorators(thisDayDecorator);

                materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                        //다른 날짜를 눌렀을 때
                        if (!date.equals(thisDay)){
                            int year = date.getYear();
                            int month = date.getMonth();
                            int day = date.getDay();

                            //데코 초기화
                            materialCalendarView.removeDecorators();

                            //메모된 정보 모두 불러오기
                            memoSourceList = db.memoSourceDao().getAll();

                            //메모된 날짜 확인하기
                            if (memoSourceList != null) {
                                List<CalendarDay> memoDays = new ArrayList<>();

                                for(int i = 0; i<memoSourceList.size();i++){
                                    MemoSource temp = memoSourceList.get(i);
                                    String dateWithSpace = temp.getDate();
                                    String[] YMD = dateWithSpace.split(" ");
                                    int int1 = Integer.parseInt(YMD[0]);
                                    int int2 = Integer.parseInt(YMD[1]);
                                    int int3 = Integer.parseInt(YMD[2]);
                                    memoDays.add(CalendarDay.from(int1+1-1, int2-1, int3+1-1));
                                }
                                //메모 데코 형성
                                memoDecorator = new MemoDecorator(memoDays);
                            } else{
                                memoDecorator = new MemoDecorator(new ArrayList<CalendarDay>());
                            }

                            materialCalendarView.addDecorators(
                                    new SundayDecorator(),
                                    new SaturdayDecorator(),
                                    oneDayDecorator,
                                    memoDecorator);

                            thisDay = CalendarDay.from(year, month, day);
                            thisDayDecorator = new ThisDayDecorator(thisDay);

                            materialCalendarView.clearSelection();
                            materialCalendarView.addDecorators(thisDayDecorator);
                        }

                        //같은 날짜를 다시 한 번 더 눌렀을 때
                        else{
                            int year = date.getYear();
                            int month = date.getMonth();
                            int day = date.getDay();

                            materialCalendarView.clearSelection();
                            Intent intent = new Intent(getActivity(), CalenderMemo.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("year", date.getYear()+"");
                            intent.putExtra("month", (date.getMonth()+1)+"");
                            intent.putExtra("day", date.getDay()+"");
                            startActivity(intent);

//                            //데코 초기화
//                            materialCalendarView.removeDecorators();
//
//                            //memo된 날짜 체크하기
//                            memoSourceList = db.memoSourceDao().getAll();
//
//                            if (memoSourceList != null) {
//                                List<CalendarDay> memoDays = new ArrayList<>();
//
//                                for(int i = 0; i<memoSourceList.size();i++){
//                                    MemoSource temp = memoSourceList.get(i);
//                                    String dateWithSpace = temp.getDate();
//                                    String[] YMD = dateWithSpace.split(" ");
//                                    int int1 = Integer.parseInt(YMD[0]);
//                                    int int2 = Integer.parseInt(YMD[1]);
//                                    int int3 = Integer.parseInt(YMD[2]);
//                                    memoDays.add(CalendarDay.from(int1+1-1, int2-1, int3+1-1));
//                                }
//                                memoDecorator = new MemoDecorator(memoDays);
//                            } else{
//                                memoDecorator = new MemoDecorator(new ArrayList<CalendarDay>());
//                            }
//
//                            materialCalendarView.addDecorators(
//                                    new SundayDecorator(),
//                                    new SaturdayDecorator(),
//                                    oneDayDecorator,
//                                    memoDecorator);
//
//                            thisDay = CalendarDay.from(year, month, day);
//                            thisDayDecorator = new ThisDayDecorator(thisDay);
//
//                            materialCalendarView.clearSelection();
//                            materialCalendarView.addDecorators(thisDayDecorator);
                        }
                    }
                });

            }
        });

        return view;
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result) {
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for (int i = 0; i < Time_Result.length; i++) {
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result[i].split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);
                dates.add(day);
                calendar.set(year, month - 1, dayy);
            }


            return dates;
        }
    }




}
