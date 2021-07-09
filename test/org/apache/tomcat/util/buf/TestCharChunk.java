/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.tomcat.util.buf;

import org.apache.tomcat.util.buf.CharChunk.CharOutputChannel;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Test cases for {@link CharChunk}.
 */
public class TestCharChunk {

    @Test
    public void testIndexOf_String() {
        char[] chars = "Hello\u00a0world".toCharArray();
        final int len = chars.length;

        CharChunk cc = new CharChunk();
        cc.setChars(chars, 0, len);

        Assert.assertEquals(0, cc.indexOf("Hello", 0, "Hello".length(), 0));
        Assert.assertEquals(2, cc.indexOf("ll", 0, 2, 0));
        Assert.assertEquals(2, cc.indexOf("Hello", 2, 2, 0));
    }


    public class Sink implements CharOutputChannel {

        @Override
        public void realWriteChars(char[] cbuf, int off, int len) throws IOException {
            // NO-OP
        }
    }
}
