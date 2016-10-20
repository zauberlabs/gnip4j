package com.zaubersoftware.gnip4j.api.model;

import static org.junit.Assert.*;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.zaubersoftware.gnip4j.api.impl.formats.JsonActivityFeedProcessor;

/**
 * @author Fredrik Olsson
 */
public class UrlDeserializerTest {
    private static final ObjectMapper mapper = JsonActivityFeedProcessor.getObjectMapper();

    @Test
    public void testUrl() throws Exception {

        // Test data taken from Gnip PowerTrack v2 documentation at:
        // http://support.gnip.com/apis/powertrack2.0/transition.html#Payload

        final String urlJson = "{\n" +
                "\"url\": \"https:\\/\\/t.co\\/b9ZdzRxzFK\",\n" +
                "\"expanded_url\": \"http:\\/\\/www.today.com\\/parents\\/joke-s-you-kid-11-family-friendly-april-fools-pranks-t83276\",\n" +
                "\"expanded_status\": 200,\n" +
                "\"expanded_url_title\": \"The joke's on you, kid: 11 family-friendly April Fools pranks\",\n" +
                "\"expanded_url_description\": \"If your kids are practical jokers, turn this April Fools' Day into a family affair.\"\n" +
                "}";

        final Url url = mapper.readValue(urlJson, Url.class);
        assertEquals("Original URL", "https://t.co/b9ZdzRxzFK", url.getUrl());
        assertEquals("Expanded URL", "http://www.today.com/parents/joke-s-you-kid-11-family-friendly-april-fools-pranks-t83276", url.getExpandedUrl());
        assertEquals("Expanded status", (Integer) 200, url.getExpandedStatus());
        assertEquals("Expanded URL title", "The joke's on you, kid: 11 family-friendly April Fools pranks", url.getExpandedUrlTitle());
        assertEquals("Expanded URL description", "If your kids are practical jokers, turn this April Fools' Day into a family affair.", url.getExpandedUrlDescription());
    }

}