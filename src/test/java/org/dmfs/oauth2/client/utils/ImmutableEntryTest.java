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

package org.dmfs.oauth2.client.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Unit test for {@link ImmutableEntry}.
 *
 * @author Gabor Keszthelyi
 */
public class ImmutableEntryTest
{

    @Test
    public void testThatNullKeyIsAllowed()
    {
        assertEquals(null, new ImmutableEntry(null, "value").getKey());
    }


    @Test
    public void testThatNullValueIsAllowed()
    {
        assertEquals(null, new ImmutableEntry("key", null).getValue());
    }


    @Test
    public void testGetters()
    {
        ImmutableEntry entry = new ImmutableEntry("key", "value");
        assertEquals("key", entry.getKey());
        assertEquals("value", entry.getValue());
    }


    @Test(expected = UnsupportedOperationException.class)
    public void testThatSetValueThrowsException()
    {
        new ImmutableEntry("key", "value").setValue("new value");
    }

}