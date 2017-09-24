package com.example.windows10gamer.beautimusic.model;



public class Album {
    private int id;
    private String nameAlbum;
    private String nameArtist;
    private String image;


    public Album(int id,String ns,String na, String img) {
        this.id = id;
        this.nameAlbum = ns;
        this.nameArtist = na;
        this.image = img;
        //this.sumSong = ss;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //private int sumSong;


    public Album(){

    }

    public String getNameArtist() {
        return nameArtist;
    }

    public void setNameArtist(String nameArtist) {
        this.nameArtist = nameArtist;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }

//    public int getSumSong() {
//        return sumSong;
//    }
//
//    public void setSumSong(int sumSong) {
//        this.sumSong = sumSong;
//    }
}
