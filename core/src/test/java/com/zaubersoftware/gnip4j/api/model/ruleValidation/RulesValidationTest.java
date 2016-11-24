package com.zaubersoftware.gnip4j.api.model.ruleValidation;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.zaubersoftware.gnip4j.api.impl.formats.JsonActivityFeedProcessor;
import com.zaubersoftware.gnip4j.api.model.Rule;

public class RulesValidationTest {
    private static final ObjectMapper mapper = JsonActivityFeedProcessor.getObjectMapper();

    @Test
    public void test() throws JsonProcessingException, IOException {
        final RulesValidation r = mapper.reader(RulesValidation.class).readValue(getClass().getClassLoader()
                .getResourceAsStream("com/zaubersoftware/gnip4j/payload/rules_validation.js"));
        final RuleValidationSummary summary = r.getSummary();
        assertEquals(new Integer(0), summary.getValid());
        assertEquals(new Integer(1), summary.getNotValid());
        
        final List<RuleValidation> details = r.getDetail();
        assertEquals(1, details.size());
        final RuleValidation validation = details.iterator().next();
        assertFalse(validation.isValid());
        assertEquals("Cannot parse rule at '<EOF>' (position 9)\n", validation.getMessage());
        
        final Rule rule = validation.getRule();
        assertEquals("aaaaa OR", rule.getValue());
        assertNull(rule.getTag());
    }

}
