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

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 */
public final class Info implements Serializable {
    private static final long serialVersionUID = 1;
    private String message;
    private Date sent;
    @JsonProperty(value = "activity_count")
    private int activityCount;
    @JsonProperty(value = "minutes_failed")
    private List<Date> minutesFailed;

    SimpleDateFormat dateFormatter;

    public Info() {
        //2013-02-27T22:15:50+00:00
        this.dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ssXXX");
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public int getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(int activityCount) {
        this.activityCount = activityCount;
    }

    public List<Date> getMinutesFailed() {
        return minutesFailed;
    }

    public void setMinutesFailed(List<Date> minutesFailed) {
        this.minutesFailed = minutesFailed;
    }

    public String toString() {
        StringBuilder s = new StringBuilder("Info[");
        s.append("message:").append(this.message);
        s.append(", sent:").append(this.dateFormatter.format(this.sent));
        s.append(", activity_count:").append(this.activityCount);
        int size = 0;
        if (this.minutesFailed != null && (size = this.minutesFailed.size()) > 0) {
            s.append(", minutes_failed: [");
            int i = 0;
            for (Date minute_failed : this.minutesFailed) {
                s.append(this.dateFormatter.format(minute_failed));
                i++;
                if (i < size) {
                    s.append(", ");
                }
            }
            s.append("]");
        }
        s.append("]");
        return s.toString();
    }
}
