/*
 * Copyright 2016 Marten Gajda <marten@dmfs.org>
 *
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

package org.dmfs.oauth2.client.http.entities;

import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.oauth2.client.utils.ImmutableEntry;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;


/**
 * Unit test for {@link XWwwFormUrlEncodedEntity}.
 *
 * @author Gabor Keszthelyi
 */
public class XWwwFormUrlEncodedEntityTest
{

    @Test
    public void testImmutability_whenInputEntryArrayIsChanged_entityObjectShouldNotChange()
    {
        // ARRANGE
        Map.Entry<String, String>[] inputValues = new Map.Entry[2];
        ImmutableEntry entry0 = new ImmutableEntry("key0", "value0");
        ImmutableEntry entry1 = new ImmutableEntry("key1", "value1");
        inputValues[0] = entry0;
        inputValues[1] = entry1;

        // ACT
        HttpRequestEntity entity = new XWwwFormUrlEncodedEntity(inputValues);
        ImmutableEntry entry2 = new ImmutableEntry("key1-changed", "value1-changed");
        inputValues[1] = entry2;

        // ASSERT
        assertEquals("key0=value0&key1=value1", entity.toString());
    }


    @Test
    public void testContentType()
    {
        HttpRequestEntity entity = new XWwwFormUrlEncodedEntity(new ImmutableEntry("key", "value"));
        assertEquals("application", entity.contentType().mainType());
        assertEquals("x-www-form-urlencoded", entity.contentType().subType());
    }


    @Test
    public void testContentLength() throws IOException
    {
        HttpRequestEntity entity = new XWwwFormUrlEncodedEntity(new ImmutableEntry("key", "val"));
        assertEquals(7, entity.contentLength());

        HttpRequestEntity emptyEntity = new XWwwFormUrlEncodedEntity();
        assertEquals(0, emptyEntity.contentLength());
    }


    @Test
    public void testToString()
    {
        assertEquals("", new XWwwFormUrlEncodedEntity().toString());

        assertEquals("key=value", new XWwwFormUrlEncodedEntity(new ImmutableEntry("key", "value")).toString());

        assertEquals("key=value&key1=value1", new XWwwFormUrlEncodedEntity(
                new ImmutableEntry("key", "value"),
                new ImmutableEntry("key1", "value1")).toString());

        assertEquals("key=value&key1=value1&key2=value2", new XWwwFormUrlEncodedEntity(
                new ImmutableEntry("key", "value"),
                new ImmutableEntry("key1", "value1"),
                new ImmutableEntry("key2", "value2")).toString());
    }


    @Test(expected = Exception.class)
    public void testConstructorWithNull_shouldThrowException()
    {
        new XWwwFormUrlEncodedEntity(null);
    }


    @Test
    public void testWriteToOutputStream() throws IOException
    {
        // ARRANGE
        HttpRequestEntity entity = new XWwwFormUrlEncodedEntity(
                new ImmutableEntry("key", "value"),
                new ImmutableEntry("key1", "value1"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // ACT
        entity.writeContent(out);

        // ASSERT
        assertEquals("key=value&key1=value1", out.toString("UTF-8"));
        out.close();
    }


    @Test
    public void testUrlEncodingWithAFewSpecialCharacters()
    {
        assertEquals("key+and+space=value+with+spaces",
                new XWwwFormUrlEncodedEntity(new ImmutableEntry("key and space", "value with spaces")).toString());

        assertEquals("key%22doublequote=",
                new XWwwFormUrlEncodedEntity(new ImmutableEntry("key\"doublequote", "")).toString());

        assertEquals("aa=%7B",
                new XWwwFormUrlEncodedEntity(new ImmutableEntry("aa", "{")).toString());

        // equals ('=')
        assertEquals("key%3Dwith%3Dequals=value%3Dwith%3Dequals",
                new XWwwFormUrlEncodedEntity(new ImmutableEntry("key=with=equals", "value=with=equals")).toString());

        // and ('&')
        assertEquals("key%26with%26and=value%26with%26and",
                new XWwwFormUrlEncodedEntity(new ImmutableEntry("key&with&and", "value&with&and")).toString());
    }

    @Test
    public void testIgnoreEntries_whenEntryIsNull_orKeyIsNullOrEmpty_orValueIsNull()
    {
        HttpRequestEntity entity = new XWwwFormUrlEncodedEntity(
                new ImmutableEntry("key0", "value0"),
                new ImmutableEntry(null, "value1"),
                new ImmutableEntry("key2", null),
                new ImmutableEntry(null, null),
                new ImmutableEntry("key4", "value4"),
                new ImmutableEntry("", "value5"),
                null,
                new ImmutableEntry("key7", "value7")
        );

        assertEquals("key0=value0&key4=value4&key7=value7", entity.toString());
    }

}