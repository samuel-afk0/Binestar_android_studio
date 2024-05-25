package com.example.proyecto_final;

public class ConsejoModelo {
    private String title;
    private String text1;
    private String text2;
    private  String img;
    // Constructor vacío necesario para Firestore
    public ConsejoModelo() {}
    public ConsejoModelo(String title, String text1, String text2, String img) {
        this.title = title;
        this.text1 = text1;
        this.text2 = text2;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }
    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}