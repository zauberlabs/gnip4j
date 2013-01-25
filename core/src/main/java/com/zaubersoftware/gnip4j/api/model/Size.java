package com.zaubersoftware.gnip4j.api.model;

public class Size {
    String resize;
    int h;
    int w;
    public String getResize() {
        return resize;
    }
    public Size setResize(String resize) {
        this.resize = resize;
        return this;
    }
    public int getH() {
        return h;
    }
    public Size setH(int h) {
        this.h = h;
        return this;
    }
    public int getW() {
        return w;
    }
    public Size setW(int w) {
        this.w = w;
        return this;
    }
}