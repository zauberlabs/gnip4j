/**
 * 
 */
package com.zaubersoftware.gnip4j.api.model;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

class Size {
    String resize;
    int h;
    int w;
    public String getResize() {
        return resize;
    }
    public void setResize(String resize) {
        this.resize = resize;
    }
    public int getH() {
        return h;
    }
    public void setH(int h) {
        this.h = h;
    }
    public int getW() {
        return w;
    }
    public void setW(int w) {
        this.w = w;
    }
}

class Sizes {
    Size thumb;
    Size small;
    Size medium;
    Size large;
    
    public Size getThumb() {
        return thumb;
    }
    public void setThumb(Size thumb) {
        this.thumb = thumb;
    }
    public Size getSmall() {
        return small;
    }
    public void setSmall(Size small) {
        this.small = small;
    }
    public Size getMedium() {
        return medium;
    }
    public void setMedium(Size medium) {
        this.medium = medium;
    }
    public Size getLarge() {
        return large;
    }
    public void setLarge(Size large) {
        this.large = large;
    }
}

/**
 * @author Derrick Burns <derrick.burns@rincaro.com>
 *
 */
public class Media {
    String type;
    List<Integer> indices;
    Sizes sizes;
    String url;
    @JsonProperty(value = "media_url") 
    String mediaUrl;
    @JsonProperty(value = "display_url") 
    String displayUrl;
    @JsonProperty(value = "media_url_https") 
    String mediaUrlHttps;
    BigInteger id;
    @JsonProperty(value = "id_str") 
    String idStr;
    @JsonProperty(value = "expanded_url") 
    String expandedUrl;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public List<Integer> getIndices() {
        return indices;
    }
    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }
    public Sizes getSizes() {
        return sizes;
    }
    public void setSizes(Sizes sizes) {
        this.sizes = sizes;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getMediaUrl() {
        return mediaUrl;
    }
    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
    public String getDisplayUrl() {
        return displayUrl;
    }
    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }
    public String getMediaUrlHttps() {
        return mediaUrlHttps;
    }
    public void setMediaUrlHttps(String mediaUrlHttps) {
        this.mediaUrlHttps = mediaUrlHttps;
    }
    public BigInteger getId() {
        return id;
    }
    public void setId(BigInteger id) {
        this.id = id;
    }
    public String getIdStr() {
        return idStr;
    }
    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }
    public String getExpandedUrl() {
        return expandedUrl;
    }
    public void setExpandedUrl(String expandedUrl) {
        this.expandedUrl = expandedUrl;
    }
}
