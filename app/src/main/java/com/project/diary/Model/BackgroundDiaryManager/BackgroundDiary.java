package com.project.diary.Model.BackgroundDiaryManager;

/**
 * This class represents a diary background.
 * It includes the ID of the background image and the resource ID of the background.
 */
public class BackgroundDiary {
    private String idImage;
    private int resIdBackground;

    /**
     * Constructs a new BackgroundDiary object with the specified ID and resource ID.
     * @param idImage the ID of the background image
     * @param resIdBackground the resource ID of the background
     */
    public BackgroundDiary(String idImage, int resIdBackground) {
        this.idImage = idImage;
        this.resIdBackground = resIdBackground;
    }

    /**
     * Returns the ID of the background image.
     * @return the ID of the background image
     */
    public String getIdImage() {
        return idImage;
    }

    /**
     * Sets the ID of the background image.
     * @param idImage the ID of the background image
     */
    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    /**
     * Returns the resource ID of the background.
     * @return the resource ID of the background
     */
    public int getResIdBackground() {
        return resIdBackground;
    }

    /**
     * Sets the resource ID of the background.
     * @param resIdBackground the resource ID of the background
     */
    public void setResIdBackground(int resIdBackground) {
        this.resIdBackground = resIdBackground;
    }
}
