package sontungmtp.project.diary.Model.Calendar;

import android.app.Service;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import sontungmtp.project.diary.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.LocalDate;

public class MyMaterialCalendarView extends MaterialCalendarView {
    private TextView txtToday;
    public MyMaterialCalendarView(Context context) {
        super(context);
    }

    public MyMaterialCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        final View content = inflater.inflate(R.layout.my_calendar_view, null, false);
        txtToday = content.findViewById(R.id.txtToday);
        
    }



}
