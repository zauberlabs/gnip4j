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

import org.codehaus.jackson.annotate.JsonAutoDetect;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 */
@JsonAutoDetect
public final class Rule implements Serializable {
    private static final long serialVersionUID = -6252436995868989738L;
    private String value;
    private String tag;

    public Rule() {
        // for unmarshall and compatibility
    }
    
    public Rule(final String value, final String tag) {
        this.value = value;
        this.tag = tag;
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
    
    @Override
    public boolean equals(final java.lang.Object obj) {
        boolean ret = false;
        
        if(this == obj) {
            ret = true;
        } else if(obj instanceof Rule) {
            final Rule r = ((Rule)obj);
            
            ret = equals(value, r.value) && equals(tag, r.tag); 
        }
        return ret;
    }

    @Override
    public int hashCode() {
        int ret = 17;
        
        ret = 19 * ret + ((value == null) ? 0 : value.hashCode());
        ret = 19 * ret + ((tag == null) ? 0 : tag.hashCode());
        return ret;
    }
    
    private static boolean equals(final String s1, final String s2) {
        boolean ret = false;
        if(s1 == null && s2 == null) {
            ret = true;
        } else if(s1 == null || s2 == null) {
            ret = false;
        } else {
            ret = s1.equals(s2);
        }
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
        
        sb.append('}');
        
        return sb.toString();
    }
    
    private static String escape(final String s) {
        return s.replace("\"", "\\\"");
    }
}
