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

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.zaubersoftware.gnip4j.api.exception.OffendingRule;
import com.zaubersoftware.gnip4j.api.support.http.Errors.RuleErrorDetail;

/**
 * Unit test to check the {@link Errors} parse from js files with the json response from the server. The idea
 * is to check that both v1 and v2 response of powertrack are correctly parsed to have backwards
 * compatibility.
 *
 * @author Marcelo
 */
public class ErrorsTest {
    private static final ObjectMapper m = new ObjectMapper();

    @Test
    public final void test_v1() throws JsonParseException, JsonMappingException, IOException {
        final Errors errors = m.readValue(ExampleFileDirectory.POWERTRACK_RULE_ERROR_V1.asString(), Errors.class);
        assertNotNull(errors);
        assertNotNull(errors.getError());
        final String error = "Rule '-rule1' is invalid.  Rules must contain a non-negation term (at position 1)\nRules must contain at least one positive, non-stopword clause (at position 1)\nRule '-rule14' is invalid.  Rules must contain a non-negation term (at position 1)\nRules must contain at least one positive, non-stopword clause (at position 1)\n";
        assertEquals(error,
                errors.getError().getMessage());
        assertNull(errors.getError().getRules());
        assertEquals(error, errors.toHumanMessage());
    }

    @Test
    public final void test_v2() throws JsonParseException, JsonMappingException, IOException {
        final Errors errors = m.readValue(ExampleFileDirectory.POWERTRACK_RULE_ERROR_V2.asString(), Errors.class);
        assertNotNull(errors);
        assertNotNull(errors.getError());
        assertEquals("One or more rules are invalid", errors.getError().getMessage());
        final List<OffendingRule> rules = errors.getError().getRules();
        assertNotNull(rules);
        assertEquals(2, rules.size());
        OffendingRule offendingRule = rules.get(0);
        assertEquals(
                "Rules must contain a non-negation term (at position 1)\nRules must contain at least one positive, non-stopword clause (at position 1)\n",
                offendingRule.getErrorMessage());
        assertEquals("-rule1", offendingRule.getOffendingRule().getValue());
        assertNull(offendingRule.getOffendingRule().getTag());
        offendingRule = rules.get(1);
        assertEquals(
                "Rules must contain a non-negation term (at position 1)\nRules must contain at least one positive, non-stopword clause (at position 1)\n",
                offendingRule.getErrorMessage());
        assertEquals("-rule14", offendingRule.getOffendingRule().getValue());
        assertNull(offendingRule.getOffendingRule().getTag());
        assertEquals("One or more rules are invalid", errors.toHumanMessage());
    }
    
    @Test
    public final void test_v2_bis() throws Exception {
        final Errors errors = m.readValue(ExampleFileDirectory.POWERTRACK_RULE_ERROR_V2BIS.asString(), Errors.class);
        assertEquals(new Date(1466016139741L), errors.getSent());
        assertEquals(2, errors.getSummary().size());
        assertEquals(0, errors.getSummary().get("created"));
        assertEquals(3, errors.getSummary().get("not_created"));
        final List<RuleErrorDetail> detail = errors.getDetail();
        assertNotNull(detail);
        assertEquals(3, detail.size());
        
        assertNotNull(errors);
        final String []msgs = new String[]{
                "no viable alternative at character '⚽' (at position 12)\n\n",
                null,
                null,
        };
        for(int i = 0 ; i < detail.size() ; i++) {
            final RuleErrorDetail r = detail.get(i);
            assertFalse(r.isCreated());
            assertEquals(msgs[i], r.getMessage());
            assertNotNull(r.getRule());
        }
        assertEquals("no viable alternative at character '⚽' (at position 12)\n\n", errors.toHumanMessage());
    }

    enum ExampleFileDirectory {
        POWERTRACK_RULE_ERROR_V1("/com/zaubersoftware/gnip4j/powertrack/powertrack_rule_error_v1.js"), 
        POWERTRACK_RULE_ERROR_V2("/com/zaubersoftware/gnip4j/powertrack/powertrack_rule_error_v2.js"),
        POWERTRACK_RULE_ERROR_V2BIS("/com/zaubersoftware/gnip4j/powertrack/powertrack_rule_error_v2-bis.js"),
        ;

        private static final String FILES_ENCODING = "utf-8";
        private final String filePath;

        // caches used to read once
        private String fullContentCache;
        private List<String> linesCache;

        private ExampleFileDirectory(final String filePath) {
            this.filePath = Objects.requireNonNull(filePath);
        }

        /** @return the file content as a String */
        public String asString() throws IOException {
            if (fullContentCache == null) {
                Reader stream = null;
                try {
                    stream = getStream();
                    fullContentCache = IOUtils.toString(stream);
                } catch (final IOException e) {
                    IOUtils.closeQuietly(stream);
                    throw e;
                }
            }
            return fullContentCache;
        }

        /**
         * @return the file content tokenized by line
         * @throws IOException
         */
        public List<String> asLines() throws IOException {
            if (linesCache == null) {
                Reader stream = null;
                try {
                    stream = getStream();
                    linesCache = IOUtils.readLines(stream);
                } catch (final IOException e) {
                    IOUtils.closeQuietly(stream);
                    throw e;
                }
            }
            return linesCache;
        }

        private InputStreamReader getStream() throws UnsupportedEncodingException {
            return new InputStreamReader(getClass().getResourceAsStream(filePath), FILES_ENCODING);
        }
    }

}
