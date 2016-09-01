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

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.zaubersoftware.gnip4j.api.impl.formats.JSONActivityUnmarshaller;


public class ExtendedStatusTest {
    @Test
    public void testExtendedActivities() throws IOException  {
        final Activity a = loadFile("com/zaubersoftware/gnip4j/payload/extended/attachment-from-gnip.js");
        final int[] x = a.getDisplayTextRange();
        assertArrayEquals(new int[]{0, 140}, x);
        
        final Object longObject = a.getLongObject();
        assertNotNull(longObject);
        assertEquals("this is a tweet with 140 characters of display range text, which means that with an "
                   + "image attachment the full tweet text has &gt;140 characters https://t.co/ws1QmqeYo6", 
                     longObject.getBody());
        assertArrayEquals(new int[]{0, 143}, longObject.getDisplayTextRange());
    }
    
    private static final JSONActivityUnmarshaller x = new JSONActivityUnmarshaller();
    static Activity loadFile(final String s) throws IOException {
        try(final InputStream is = ExtendedStatusTest.class.getClassLoader().getResourceAsStream(s)) {
            return x.unmarshall(IOUtils.toString(is, "utf-8"));
        }
    }
}
