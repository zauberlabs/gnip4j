package com.zaubersoftware.gnip4j.api.model;

public class Sizes {
    Size thumb;
    Size small;
    Size medium;
    Size large;
    
    public Size getThumb() {
        return thumb;
    }
    public Sizes setThumb(Size thumb) {
        this.thumb = thumb;
        return this;
    }
    public Size getSmall() {
        return small;
    }
    public Sizes setSmall(Size small) {
        this.small = small;
        return this;
    }
    public Size getMedium() {
        return medium;
    }
    public Sizes setMedium(Size medium) {
        this.medium = medium;
        return this;
    }
    public Size getLarge() {
        return large;
    }
    public Sizes setLarge(Size large) {
        this.large = large;
        return this;
    }
}