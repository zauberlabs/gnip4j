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
package com.zaubersoftware.gnip4j.api.support.http;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.zaubersoftware.gnip4j.api.exception.OffendingRule;

public class Errors {
    private Error error;

    public final Error getError() {
        return error;
    }

    public final void setError(final Error error) {
        this.error = error;
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

        public void setRules(List<OffendingRule> rules) {
            this.rules = rules;
        }
    }
}

