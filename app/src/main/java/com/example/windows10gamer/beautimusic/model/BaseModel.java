package com.example.windows10gamer.beautimusic.model;


public abstract class BaseModel {
    private int id;
    private String nameArtist;

    public BaseModel(int id, String nameArtist) {
        this.id = id;
        this.nameArtist = nameArtist;
    }

    public String getNameArtist() {
        return nameArtist;
    }

    public void setNameArtist(String nameArtist) {
        this.nameArtist = nameArtist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
