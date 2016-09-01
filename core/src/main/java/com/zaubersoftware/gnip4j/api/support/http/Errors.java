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
package com.zaubersoftware.gnip4j.api.support.http;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.zaubersoftware.gnip4j.api.exception.OffendingRule;
import com.zaubersoftware.gnip4j.api.model.Rule;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Errors {
    private Error error;
    // since powertrack V2
    private Map<String, Number> summary;
    private List<RuleErrorDetail> detail;
    private Date sent;
    
    public final Error getError() {
        return error;
    }

    public final void setError(final Error error) {
        this.error = error;
    }

    /** @since Powertrack V2 */
    public Map<String, Number> getSummary() {
        return summary;
    }
    public void setSummary(final Map<String, Number> summary) {
        this.summary = summary;
    }
    
    /** @since Powertrack V2 */
    public List<RuleErrorDetail> getDetail() {
        return detail;
    }
    public void setDetail(final List<RuleErrorDetail> detail) {
        this.detail = detail;
    }

    /** @since Powertrack V2 */
    public Date getSent() {
        return sent;
    }
    public void setSent(final Date sent) {
        this.sent = sent;
    }
    
    public final String toHumanMessage() {
        String msg = null;
        
        if(error != null) {
            msg = error.getMessage();
        }
        
        if(detail != null) {
            if(msg == null && detail.size() == 1) {
                msg = detail.get(0).getMessage();
            } else {
                int n = msg == null ? 0 : msg.length() + 2;
                for(final RuleErrorDetail deta : detail) {
                    final String s = deta.getMessage();
                    if(s != null) {
                        n += s.length() + 2;
                    }
                }
                
                final StringBuilder sb = new StringBuilder(n);
                if(msg != null) {
                    sb.append(msg);
                }
                for(final RuleErrorDetail deta : detail) {
                    final String s = deta.getMessage();
                    if(s != null) {
                        if(deta.getRule() != null)
                        if(sb.length() != 0) {
                            sb.append('\n');
                        }
                        sb.append(s);
                    }
                }
                msg = sb.toString();
            }
        }
        
        
        return msg;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Error {
        private String message;

        @JsonProperty("detail")
        private List<OffendingRule> rules;

        public final String getMessage() {
            return message;
        }

        public final void setMessage(final String message) {
            this.message = message;
        }

        public List<OffendingRule> getRules() {
            return rules;
        }

        public void setRules(final List<OffendingRule> rules) {
            this.rules = rules;
        }
    }
    

    public static class RuleErrorDetail {
        private final boolean created;
        private final String message;
        private final Rule rule;
        
        @JsonCreator
        public RuleErrorDetail(
                @JsonProperty("created") final boolean created, 
                @JsonProperty("message") final String message, 
                @JsonProperty("rule") final Rule rule) {
            this.created = created;
            this.message = message;
            this.rule = rule;
        }

        public boolean isCreated() {
            return created;
        }

        public String getMessage() {
            return message;
        }

        public Rule getRule() {
            return rule;
        }
        
        public OffendingRule toOffendingRule() {
            OffendingRule  ret = null;
            if(message != null) {
                ret = new OffendingRule(rule, message);
            }
            return ret;
        }
    }

}

