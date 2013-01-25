/**
 * 
 */
package com.zaubersoftware.gnip4j.api.model;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    public Media setType(String type) {
        this.type = type;
        return this;
    }
    public List<Integer> getIndices() {
        return indices;
    }
    public Media setIndices(List<Integer> indices) {
        this.indices = indices;
        return this;
    }
    public Sizes getSizes() {
        return sizes;
    }
    public Media setSizes(Sizes sizes) {
        this.sizes = sizes;
        return this;
    }
    public String getUrl() {
        return url;
    }
    public Media setUrl(String url) {
        this.url = url;
        return this;
    }
    public String getMediaUrl() {
        return mediaUrl;
    }
    public Media setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
        return this;
    }
    public String getDisplayUrl() {
        return displayUrl;
    }
    public Media setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
        return this;
    }
    public String getMediaUrlHttps() {
        return mediaUrlHttps;
    }
    public Media setMediaUrlHttps(String mediaUrlHttps) {
        this.mediaUrlHttps = mediaUrlHttps;
        return this;
    }
    public BigInteger getId() {
        return id;
    }
    public Media setId(BigInteger id) {
        this.id = id;
        return this;
    }
    public String getIdStr() {
        return idStr;
    }
    public Media setIdStr(String idStr) {
        this.idStr = idStr;
        return this;
    }
    public String getExpandedUrl() {
        return expandedUrl;
    }
    public Media setExpandedUrl(String expandedUrl) {
        this.expandedUrl = expandedUrl;
        return this;
    }
}
