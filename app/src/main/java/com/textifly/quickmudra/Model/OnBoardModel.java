package com.textifly.quickmudra.Model;

public class OnBoardModel {
    int image;
    String desc;

    public OnBoardModel(int image, String desc) {
        this.image = image;
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }

    public String getDesc() {
        return desc;
    }
}
