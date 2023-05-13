package sontungmtp.project.diary.Model.Diary;

import java.util.ArrayList;

public class RootDiary {
    private ArrayList<Diary> diaries;

    public ArrayList<Diary> getDiaries() {
        return diaries;
    }

    public void setDiaries(ArrayList<Diary> diaries) {
        this.diaries = diaries;
    }

    public RootDiary() {
        diaries = new ArrayList<>();
    }
}
