package com.textifly.quickmudra.Model;

public class DrawerModel {
    int image;
    String name;

    public DrawerModel(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
