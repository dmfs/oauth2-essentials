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

package org.dmfs.oauth2.client.http.decorators;

import net.iharder.Base64;
import org.dmfs.httpessentials.converters.PlainStringHeaderConverter;
import org.dmfs.httpessentials.headers.BasicSingletonHeaderType;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.headers.HttpHeaders;
import org.dmfs.httpessentials.headers.SingletonHeaderType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Functional test for {@link BasicAuthHeaderDecoration}.
 *
 * @author Gabor Keszthelyi
 */
public class BasicAuthHeaderDecorationTest
{
    // TODO: use a generic authorization header instead (once we have one)
    private final SingletonHeaderType<String> AUTHORIZATION_HEADER_TYPE = new BasicSingletonHeaderType<String>(
            "Authorization",
            new PlainStringHeaderConverter());


    @Test
    public void test_thatCorrectAuthHeaderGetsAdded_andOtherHeaderIsKept() throws Exception
    {
        // ARRANGE
        Headers original = EmptyHeaders.INSTANCE.withHeader(HttpHeaders.CONTENT_LENGTH.entity(45));
        BasicAuthHeaderDecoration decoration = new BasicAuthHeaderDecoration("user_name", "pw");

        // ACT
        Headers result = decoration.decorated(original);

        // ASSERT
        String expectedHeader = "Basic " + Base64.encodeBytes("user_name:pw".getBytes("UTF-8"));
        assertEquals(expectedHeader, result.header(AUTHORIZATION_HEADER_TYPE).value());
        assertTrue(result.contains(HttpHeaders.CONTENT_LENGTH));
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_whenUsernameIsNull_shouldThrowException() throws Exception
    {
        new BasicAuthHeaderDecoration(null, "pw");
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_whenPasswordIsNull_shouldThrowException() throws Exception
    {
        new BasicAuthHeaderDecoration("user name", null);
    }


    @Test
    public void test_thatExistingAuthHeaderIsOverridden() throws Exception
    {
        // ARRANGE
        Headers original = EmptyHeaders.INSTANCE.withHeader(AUTHORIZATION_HEADER_TYPE.entity("dummy auth header value"));
        BasicAuthHeaderDecoration decoration = new BasicAuthHeaderDecoration("user_name", "pw");

        // ACT
        Headers result = decoration.decorated(original);

        // ASSERT
        String expectedHeader = "Basic " + Base64.encodeBytes("user_name:pw".getBytes("UTF-8"));
        assertEquals(expectedHeader, result.header(AUTHORIZATION_HEADER_TYPE).value());
    }

}