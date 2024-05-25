package com.example.proyecto_final;

// Clase para las Imagenes del ListView
public class ImagenItem {
    private final int imagenId;
    private final String text;
    private final String url;

    public ImagenItem(int imageId, String text, String url) {
        this.imagenId = imageId;
        this.text = text;
        this.url = url;
    }

    public int getImagenId() {
        return imagenId;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }
}