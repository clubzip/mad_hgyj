package com.example.project1;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.List;

public class MemoDecorator implements DayViewDecorator {

    private List<CalendarDay> calendarDays = new ArrayList<> ();

    public MemoDecorator() {
    }

    public MemoDecorator(List<CalendarDay> calendarDays) {
        this.calendarDays = calendarDays;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return (this.calendarDays != null) && (this.calendarDays.contains(day));
//        return (this.calendarDays != null);
//        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
//        view.addSpan(new ForegroundColorSpan(Color.RED));
        view.addSpan(new DotSpan((float) 10.0, -65281)); // 날자밑에 점
    }

}
