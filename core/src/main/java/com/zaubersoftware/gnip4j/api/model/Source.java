/**
 * Copyright (c) 2011-2016 Zauber S.A. <http://flowics.com/>
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Source
 *
 * @author Juan F. Codagnone
 * @since Dec 12, 2012
 */
public class Source implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private Date updated;
    private List<Links> links;
    private Gnip gnip;
    
    public final String getTitle() {
        return title;
    }

    public final void setTitle(final String title) {
        this.title = title;
    }

    public final Date getUpdated() {
        return updated;
    }

    public final void setUpdated(final Date updated) {
        this.updated = updated;
    }
    
    
    public final List<Links> getLinks() {
        if (links == null) {
            links = new ArrayList<Links>();
        }
        return links;
    }
    
    public final Gnip getGnip() {
        if(gnip == null) {
            gnip = new Gnip();
        }
        return gnip;
    }

    public final void setGnip(final Gnip value) {
        gnip = value;
    }

}
