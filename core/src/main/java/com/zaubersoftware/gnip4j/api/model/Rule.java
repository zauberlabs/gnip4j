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
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonCreator;

@JsonAutoDetect
public final class Rule implements Serializable {
    private static final long serialVersionUID = -6252436995868989738L;
    private String value;
    private String tag;
    private Long id;
    
    public Rule() {
        // for unmarshall and compatibility
    }
   
    public Rule(final String value, final String tag) {
        this(value, tag, null);
    }
    
    public Rule(final String value, final String tag, final Long id) {
        this.value = value;
        this.tag = tag;
        this.id  = id;
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    
    public void setTag(final String value) {
        tag = value;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(final java.lang.Object obj) {
        boolean ret = false;
        
        if(this == obj) {
            ret = true;
        } else if(obj instanceof Rule) {
            final Rule r = ((Rule)obj);
            
            ret = Objects.equals(value, r.value) 
               && Objects.equals(tag,   r.tag) 
               && Objects.equals(id,    r.id)
               ;
        }
        return ret;
    }

    @Override
    public int hashCode() {
        int ret = 17;
        
        ret = 19 * ret + ((value == null) ? 0 : value.hashCode());
        ret = 19 * ret + ((tag == null)   ? 0 : tag.hashCode());
        ret = 19 * ret + ((id == null)    ? 0 : id.hashCode());
        
        return ret;
    }
    
    
    @Override
    public String toString() {
        /*
         * Outputs a json like the one gnip expects.
         * 
         * You might say WTF, we are crafting the json string by hand.
         * Thats because its just a toString method, and because i don't 
         * want extra dependencies.
         */
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        
        if(tag != null) {
            sb.append("\"tag\": \"");
            sb.append(escape(tag));
            sb.append('"');
        }
        
        if(value != null) {
            if(sb.length() != 0) {
                if(sb.length() > 1) {
                    sb.append(", ");
                }
            }
            sb.append("\"value\": \"");
            sb.append(escape(value));
            sb.append('"');
        }
        if(id != null) {
            if(sb.length() != 0) {
                if(sb.length() > 1) {
                    sb.append(", ");
                }
            }
            sb.append("\"id\": ");
            sb.append(id);
        }
        sb.append('}');
        
        return sb.toString();
    }
    
    private static String escape(final String s) {
        return s.replace("\"", "\\\"");
    }
}
