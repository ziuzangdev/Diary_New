package sontungmtp.project.diary.Control.Activity;

import android.content.Context;

import androidx.annotation.WorkerThread;

import sontungmtp.project.diary.Control.Adapter.Diary.RcvDiaryAdapter;
import sontungmtp.project.diary.Control.RootControl;
import sontungmtp.project.diary.Model.Diary.Diary;
import sontungmtp.project.diary.Model.SQLite.SQLite;
import sontungmtp.project.diary.Model.ThemeManager.AppThemeManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * The MainActivityControl class extends the RootControl class and is responsible for managing the data and functionality of the main activity.
 * It provides access to the diary data, an instance of the RcvDiaryAdapter class, and an instance of the AppThemeManager class.
 * @author [TrikayDev]
 * @since [12/30/2022]
 */
public class MainActivityControl extends RootControl {
    private ArrayList<Diary> diaries;

    private ArrayList<Diary> diariesSearch;

    private SQLite sqLite;

    private boolean isRunning;

    private RcvDiaryAdapter rcvDiaryAdapter;

    private AppThemeManager appThemeManager;

    /**
     * Returns an instance of the AppThemeManager class.
     *
     * @return an AppThemeManager instance
     */
    public AppThemeManager getAppThemeManager() {
        return appThemeManager;
    }

    /**
     * Returns an instance of the RcvDiaryAdapter class.
     *
     * @return an RcvDiaryAdapter instance
     */
    public RcvDiaryAdapter getRcvDiaryAdapter() {
        return rcvDiaryAdapter;
    }

    /**
     * Sets an instance of the RcvDiaryAdapter class.
     *
     * @param rcvDiaryAdapter an instance of the RcvDiaryAdapter class
     */
    public void setRcvDiaryAdapter(RcvDiaryAdapter rcvDiaryAdapter) {
        this.rcvDiaryAdapter = rcvDiaryAdapter;
    }

    /**
     * Returns the search results for diaries.
     *
     * @return an ArrayList of Diary objects representing the search results
     */
    public ArrayList<Diary> getDiariesSearch() {
        if(diariesSearch == null){
            diariesSearch = new ArrayList<>();
        }
        return diariesSearch;
    }

    /**
     * Sets the search results for diaries.
     *
     * @param diariesSearch an ArrayList of Diary objects representing the search results
     */
    public void setDiariesSearch(ArrayList<Diary> diariesSearch) {
        this.diariesSearch = diariesSearch;
    }

    /**
     * Returns the status of the activity.
     *
     * @return true if the activity is running, false otherwise
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Sets the status of the activity.
     *
     * @param running true to set the activity as running, false otherwise
     */
    public void setRunning(boolean running) {
        isRunning = running;
    }

    /**
     * Returns the list of diaries.
     *
     * @return an ArrayList of Diary objects
     */
    public ArrayList<Diary> getDiaries() {
        if(diaries == null){
            diaries = new ArrayList<>();
        }
        return diaries;
    }

    /**
     * Constructs a new MainActivityControl object and initializes the sqLite and appThemeManager fields.
     *
     * @param context a Context object used to access application-specific resources
     */
    public MainActivityControl(Context context) {
        super(context);
        isRunning = false;
        sqLite = new SQLite(context);
        appThemeManager = new AppThemeManager(context);
    }
    /**
     * Initializes the RecyclerView adapter with the list of diaries.
     * This method should be called on a worker thread.
     */
    @WorkerThread
    public void InitRcvDiaryItem(){
        isRunning = true;
        if(diaries != null){
            diaries.clear();
        }
        ArrayList<Diary> diaries1 = sqLite.getSqLiteControl().readData("Diary");
        diaries = new ArrayList<>();
        for(int i = diaries1.size() - 1; i>=0; i--){
            diaries.add(diaries1.get(i));
        }
        sortArrayListByCalendarDay(diaries, true);
        isRunning = false;
    }

    /**
     * Sorts the given list of diaries by the CalendarDay field in ascending or descending order.
     *
     * @param arrayList an ArrayList of Diary objects to be sorted
     * @param isNewest true to sort in descending order (newest first), false to sort in ascending order (oldest first)
     */
    public void sortArrayListByCalendarDay(ArrayList<Diary> arrayList, boolean isNewest) {
        if(isNewest){
            arrayList.sort(new Comparator<Diary>() {
                @Override
                public int compare(Diary o1, Diary o2) {
                    CalendarDay cd1 = o1.getDate();
                    CalendarDay cd2 = o2.getDate();
                    if (cd1.isBefore(cd2)) {
                        return 1;
                    } else if (cd1.isAfter(cd2)) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
        }else{
            arrayList.sort(new Comparator<Diary>() {
                @Override
                public int compare(Diary o1, Diary o2) {
                    CalendarDay cd1 = o1.getDate();
                    CalendarDay cd2 = o2.getDate();
                    if (cd1.isBefore(cd2)) {
                        return -1;
                    } else if (cd1.isAfter(cd2)) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
        }

    }
}
