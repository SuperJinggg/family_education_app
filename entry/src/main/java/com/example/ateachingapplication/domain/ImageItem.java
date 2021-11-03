package com.example.ateachingapplication.domain;

import java.io.Serializable;

public class ImageItem implements Serializable {
    private int image;

    public ImageItem(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
