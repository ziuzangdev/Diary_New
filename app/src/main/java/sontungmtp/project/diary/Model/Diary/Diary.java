package sontungmtp.project.diary.Model.Diary;


import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

public class Diary {

    private DiaryData diaryData;

    private boolean isDraft;
    private String tittle;
    private String id;
    private String status;
    private CalendarDay date;
    private int background;
    private ArrayList<String> mediaPaths;


    public boolean isDraft() {
        return isDraft;
    }

    public void setDraft(boolean draft) {
        isDraft = draft;
    }

    public DiaryData getDiaryData() {
        return diaryData;
    }

    public void setDiaryData(DiaryData diaryData) {
        this.diaryData = diaryData;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CalendarDay getDate() {
        return date;
    }

    public void setDate(CalendarDay date) {
        this.date = date;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public ArrayList<String> getMediaPaths() {
        if(mediaPaths == null){
            mediaPaths = new ArrayList<>();
        }
        return mediaPaths;
    }

    public void setMediaPaths(ArrayList<String> mediaPaths) {
        this.mediaPaths = mediaPaths;
    }

    public static class Builder {

        private DiaryData diaryData;
        private String tittle;
        private String id;
        private String status;
        private CalendarDay date;
        private int background;
        private ArrayList<String> mediaPaths;

        public Builder() {
        }

        Builder(DiaryData diaryData, String tittle, String id, String status, CalendarDay date, int background, ArrayList<String> mediaPaths) {
            this.diaryData = diaryData;
            this.tittle = tittle;
            this.id = id;
            this.status = status;
            this.date = date;
            this.background = background;
            this.mediaPaths = mediaPaths;
        }

        public Builder diaryData(DiaryData diaryData){
            this.diaryData = diaryData;
            return Builder.this;
        }

        public Builder tittle(String tittle){
            this.tittle = tittle;
            return Builder.this;
        }

        public Builder id(String id){
            this.id = id;
            return Builder.this;
        }

        public Builder status(String status){
            this.status = status;
            return Builder.this;
        }

        public Builder date(CalendarDay date){
            this.date = date;
            return Builder.this;
        }

        public Builder background(int background){
            this.background = background;
            return Builder.this;
        }

        public Builder mediaPaths(ArrayList<String> mediaPaths){
            this.mediaPaths = mediaPaths;
            return Builder.this;
        }

        public Diary build() {
            if(this.diaryData == null){
                throw new NullPointerException("The property \"diaryData\" is null. "
                        + "Please set the value by \"diaryData()\". "
                        + "The properties \"diaryData\", \"tittle\", \"status\" and \"date\" are required.");
            }
            if(this.tittle == null){
                throw new NullPointerException("The property \"tittle\" is null. "
                        + "Please set the value by \"tittle()\". "
                        + "The properties \"diaryData\", \"tittle\", \"status\" and \"date\" are required.");
            }
            if(this.status == null){
                throw new NullPointerException("The property \"status\" is null. "
                        + "Please set the value by \"status()\". "
                        + "The properties \"diaryData\", \"tittle\", \"status\" and \"date\" are required.");
            }
            if(this.date == null){
                throw new NullPointerException("The property \"date\" is null. "
                        + "Please set the value by \"date()\". "
                        + "The properties \"diaryData\", \"tittle\", \"status\" and \"date\" are required.");
            }
            return new Diary(this);
        }
    }

    private Diary(Builder builder) {
        this.diaryData = builder.diaryData;
        this.tittle = builder.tittle;
        this.id = builder.id;
        this.status = builder.status;
        this.date = builder.date;
        this.background = builder.background;
        this.mediaPaths = builder.mediaPaths;
        this.isDraft = false;
    }
}