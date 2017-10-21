package com.example.windows10gamer.beautimusic.model;



public class Album extends BaseModel {
    private String nameAlbum;
    private String image;


    public Album(int id, String nameArtist, String nameAlbum, String image) {
        super(id, nameArtist);
        this.nameAlbum = nameAlbum;
        this.image = image;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
