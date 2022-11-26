package com.project.diary.Model.Diary;

public class DiaryData {

    private String data;

    public static class Builder {

        private String data;

        public Builder() {
        }

        Builder(String data) {
            this.data = data;
        }

        public Builder data(String data){
            this.data = data;
            return Builder.this;
        }

        public DiaryData build() {
            if(this.data == null){
                throw new NullPointerException("The property \"data\" is null. "
                        + "Please set the value by \"data()\". "
                        + "The property \"data\" is required.");
            }

            return new DiaryData(this);
        }
    }

    private DiaryData(Builder builder) {
        this.data = builder.data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}