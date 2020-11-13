package com.casper.testdrivendevelopment.dataprocessor;

import java.io.Serializable;

/**
 * Created by jszx on 2019/9/24.
 */

public class Book implements Serializable {
    private String name;
    private int image;

    public Book(String name, int imageResourceId) {
        this.name = name;
        this.image = imageResourceId;
    }

    public void setImage(int image) { this.image = image; }
    public int getCoverResourceId() {
        return image;
    }

    public String getTitle() {
        return name;
    }

    public void setName(String book_name) { this.name = book_name; }
}
