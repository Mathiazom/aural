package com.mzom.aural.models;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Folder implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;


    public String getTitle() {
        return title;
    }

    public Folder(final String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
