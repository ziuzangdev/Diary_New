package com.project.diary.Model.BackgroundDiaryManager;

import java.util.ArrayList;

public class PackageBackgroundDiary {
    private String namePackage;
    private ArrayList<BackgroundDiary> backgroundDiaryArrayList;

    public PackageBackgroundDiary(String namePackage, ArrayList<BackgroundDiary> backgroundDiaryArrayList) {
        this.namePackage = namePackage;
        this.backgroundDiaryArrayList = backgroundDiaryArrayList;
    }

    public String getNamePackage() {
        return namePackage;
    }

    public void setNamePackage(String namePackage) {
        this.namePackage = namePackage;
    }

    public ArrayList<BackgroundDiary> getBackgroundDiaryArrayList() {
        return backgroundDiaryArrayList;
    }

    public void setBackgroundDiaryArrayList(ArrayList<BackgroundDiary> backgroundDiaryArrayList) {
        this.backgroundDiaryArrayList = backgroundDiaryArrayList;
    }
}
