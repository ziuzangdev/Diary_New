package sontungmtp.project.diary.Control.Activity;

import android.content.Context;

import androidx.annotation.WorkerThread;

import sontungmtp.project.diary.Control.Adapter.Diary.RcvCalandarDiaryAdapter;
import sontungmtp.project.diary.Model.Diary.Diary;
import sontungmtp.project.diary.Model.ThemeManager.AppThemeManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

/**
 * Class for controlling the CalandarActivity. Extends MainActivityControl to have access to its
 * diary management methods.
 *
 * @author [TrikayDev]
 * @since [12/30/2022]
 */
public class CalandarActivityControl extends MainActivityControl {
    private RcvCalandarDiaryAdapter rcvCalandarDiaryAdapter;

    private ArrayList<Diary> diariesOnDate;

    private AppThemeManager appThemeManager;

    @Override
    public AppThemeManager getAppThemeManager() {
        return appThemeManager;
    }

    /**
     * Initializes the list of diaries on the selected date. This method should be called in a
     * background thread.
     *
     * @param calendarDay The CalendarDay object representing the selected date
     */
    @WorkerThread
    public void initDiariesOnDate(CalendarDay calendarDay) {
        System.out.println("root: " + calendarDay.toString());
        if(diariesOnDate == null){
            diariesOnDate = new ArrayList<>();
        }
        diariesOnDate.clear();
        for(Diary diary : getDiaries()){
            System.out.println("sub: " + diary.getDate().toString());
            if(diary.getDate().getDay() == calendarDay.getDay() &&
                    diary.getDate().getMonth() == calendarDay.getMonth() &&
                    diary.getDate().getYear() == calendarDay.getYear()){
                    diariesOnDate.add(diary);
            }
        }
    }

    public ArrayList<Diary> getDiariesOnDate() {
        if(diariesOnDate == null){
            diariesOnDate = new ArrayList<>();
        }
        return diariesOnDate;
    }

    public void setDiariesOnDate(ArrayList<Diary> diariesOnDate) {
        this.diariesOnDate = diariesOnDate;
    }

    public RcvCalandarDiaryAdapter getRcvCalandarDiaryAdapter() {
        return rcvCalandarDiaryAdapter;
    }

    public void setRcvCalandarDiaryAdapter(RcvCalandarDiaryAdapter rcvCalandarDiaryAdapter) {
        this.rcvCalandarDiaryAdapter = rcvCalandarDiaryAdapter;
    }


    /**
     * Constructor for the CalandarActivityControl. Initializes the AppThemeManager for this control.
     *
     * @param context The context of the CalandarActivity
     */
    public CalandarActivityControl(Context context) {
        super(context);
        appThemeManager = new AppThemeManager(context);
    }
}
