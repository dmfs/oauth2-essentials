/*
 * Copyright 2017 dmfs GmbH
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
import org.dmfs.rfc3986.parameters.ParameterType;
import org.dmfs.rfc3986.parameters.parametertypes.BasicParameterType;
import org.dmfs.rfc3986.parameters.valuetypes.TextValueType;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;


/**
 * @author marten
 */
public class XWwwFormUrlEncodedEntityTest
{
    @Test
    public void testContentType()
    {
        ParameterType<CharSequence> key = new BasicParameterType<>("key", TextValueType.INSTANCE);
        HttpRequestEntity entity = new XWwwFormUrlEncodedEntity(key.parameter("value"));
        assertEquals("application", entity.contentType().mainType());
        assertEquals("x-www-form-urlencoded", entity.contentType().subType());
    }


    @Test
    public void testContentLength() throws IOException
    {
        ParameterType<CharSequence> key = new BasicParameterType<>("key", TextValueType.INSTANCE);
        HttpRequestEntity entity = new XWwwFormUrlEncodedEntity(key.parameter("val"));
        assertEquals(7, entity.contentLength());

        HttpRequestEntity emptyEntity = new XWwwFormUrlEncodedEntity();
        assertEquals(0, emptyEntity.contentLength());
    }


    @Test
    public void testToString()
    {
        ParameterType<CharSequence> key = new BasicParameterType<>("key", TextValueType.INSTANCE);
        ParameterType<CharSequence> key1 = new BasicParameterType<>("key1", TextValueType.INSTANCE);
        ParameterType<CharSequence> key2 = new BasicParameterType<>("key2", TextValueType.INSTANCE);

        assertEquals("", new XWwwFormUrlEncodedEntity().toString());

        assertEquals("key=value", new XWwwFormUrlEncodedEntity(key.parameter("value")).toString());

        assertEquals("key=value&key1=value1", new XWwwFormUrlEncodedEntity(key.parameter("value"), key1.parameter("value1")).toString());

        assertEquals("key=value&key1=value1&key2=value2", new XWwwFormUrlEncodedEntity(
                key.parameter("value"),
                key1.parameter("value1"),
                key2.parameter("value2")).toString());
    }


    @Test
    public void testWriteToOutputStream() throws IOException
    {
        ParameterType<CharSequence> key = new BasicParameterType<>("key", TextValueType.INSTANCE);
        ParameterType<CharSequence> key1 = new BasicParameterType<>("key1", TextValueType.INSTANCE);

        // ARRANGE
        HttpRequestEntity entity = new XWwwFormUrlEncodedEntity(
                key.parameter("value"),
                key1.parameter("value1"));
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

        ParameterType<CharSequence> keyWithSpaces = new BasicParameterType<>("key and space", TextValueType.INSTANCE);
        assertEquals("key+and+space=value+with+spaces",
                new XWwwFormUrlEncodedEntity(keyWithSpaces.parameter("value with spaces")).toString());

        ParameterType<CharSequence> keyWithQuotes = new BasicParameterType<>("key\"doublequote", TextValueType.INSTANCE);
        assertEquals("key%22doublequote=",
                new XWwwFormUrlEncodedEntity(keyWithQuotes.parameter("")).toString());

        ParameterType<CharSequence> keyaa = new BasicParameterType<>("aa", TextValueType.INSTANCE);
        assertEquals("aa=%7B",
                new XWwwFormUrlEncodedEntity(keyaa.parameter("{")).toString());

        // equals ('=')
        ParameterType<CharSequence> keyWithEquals = new BasicParameterType<>("key=with=equals", TextValueType.INSTANCE);
        assertEquals("key%3Dwith%3Dequals=value%3Dwith%3Dequals",
                new XWwwFormUrlEncodedEntity(keyWithEquals.parameter("value=with=equals")).toString());

        // and ('&')
        ParameterType<CharSequence> keyWithAmpersand = new BasicParameterType<>("key&with&and", TextValueType.INSTANCE);
        assertEquals("key%26with%26and=value%26with%26and",
                new XWwwFormUrlEncodedEntity(keyWithAmpersand.parameter("value&with&and")).toString());
    }
}