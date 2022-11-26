package com.project.diary.Model.Diary;

public class Diary {

    private DiaryData diaryData;
    private String tittle;
    private String id;
    private String status;
    private String date;

    public static class Builder {

        private DiaryData diaryData;
        private String tittle;
        private String id;
        private String status;
        private String date;

        public Builder() {
        }

        Builder(DiaryData diaryData, String tittle, String id, String status, String date) {
            this.diaryData = diaryData;
            this.tittle = tittle;
            this.id = id;
            this.status = status;
            this.date = date;
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

        public Builder date(String date){
            this.date = date;
            return Builder.this;
        }

        public Diary build() {
            if(this.diaryData == null){
                throw new NullPointerException("The property \"diaryData\" is null. "
                        + "Please set the value by \"diaryData()\". "
                        + "The properties \"diaryData\", \"tittle\", \"id\", \"status\" and \"date\" are required.");
            }
            if(this.tittle == null){
                throw new NullPointerException("The property \"tittle\" is null. "
                        + "Please set the value by \"tittle()\". "
                        + "The properties \"diaryData\", \"tittle\", \"id\", \"status\" and \"date\" are required.");
            }
//            if(this.id == null){
//                throw new NullPointerException("The property \"id\" is null. "
//                        + "Please set the value by \"id()\". "
//                        + "The properties \"diaryData\", \"tittle\", \"id\", \"status\" and \"date\" are required.");
//            }
            if(this.status == null){
                throw new NullPointerException("The property \"status\" is null. "
                        + "Please set the value by \"status()\". "
                        + "The properties \"diaryData\", \"tittle\", \"id\", \"status\" and \"date\" are required.");
            }
            if(this.date == null){
                throw new NullPointerException("The property \"date\" is null. "
                        + "Please set the value by \"date()\". "
                        + "The properties \"diaryData\", \"tittle\", \"id\", \"status\" and \"date\" are required.");
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
    }

    public void doSomething() {
        // do something
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

