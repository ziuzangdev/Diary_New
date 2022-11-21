package com.project.diary.Model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DiaryCalandarView extends CalendarView {
    public DiaryCalandarView(@NonNull Context context) {
        super(context);
    }

    public DiaryCalandarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DiaryCalandarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DiaryCalandarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
