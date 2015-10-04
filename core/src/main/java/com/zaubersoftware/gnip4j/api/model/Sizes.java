/**
 * Copyright (c) 2011-2012 Zauber S.A. <http://www.zaubersoftware.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zaubersoftware.gnip4j.api.model;

import java.io.Serializable;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 */
public final class Sizes implements Serializable {
    
    private Size large;
    private Size medium;
    private Size small;
    private Size thumb;
    
    public Size getLarge() {
        return large;
    }
    
    public Size getMedium() {
        return medium;
    }

    public Size getSmall() {
        return small;
    }
    
    public Size getThumb() {
        return thumb;
    }

    public void setLarge(Size large) {
        this.large = large;
    }

    public void setMedium(Size medium) {
        this.medium = medium;
    }

    public void setSmall(Size small) {
        this.small = small;
    }

    public void setThumb(Size thumb) {
        this.thumb = thumb;
    }



}
