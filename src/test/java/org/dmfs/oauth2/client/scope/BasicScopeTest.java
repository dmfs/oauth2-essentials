/*
 * Copyright 2016 dmfs GmbH
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

package org.dmfs.oauth2.client.scope;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class BasicScopeTest
{

    @Test
    public void testIsEmpty()
    {
        assertTrue(new BasicScope().isEmpty());
        assertFalse(new BasicScope("test").isEmpty());
        assertFalse(new BasicScope("test", "calendar").isEmpty());
    }


    @Test
    public void testHasToken()
    {
        assertFalse(new BasicScope().hasToken("test"));
        assertTrue(new BasicScope("test").hasToken("test"));
        assertFalse(new BasicScope("test").hasToken("calendar"));
        assertTrue(new BasicScope("test", "calendar").hasToken("test"));
        assertTrue(new BasicScope("test", "calendar").hasToken("calendar"));
        assertFalse(new BasicScope("test", "calendar").hasToken("foo"));
    }


    @Test
    public void testToString()
    {
        assertEquals("", new BasicScope().toString());
        assertEquals("test", new BasicScope("test").toString());
        assertEquals("test calendar", new BasicScope("test", "calendar").toString());
    }

}
